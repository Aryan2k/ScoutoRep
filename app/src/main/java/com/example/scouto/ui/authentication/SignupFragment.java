package com.example.scouto.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.example.scouto.R;
import com.example.scouto.databinding.FragmentSignupBinding;
import com.example.scouto.ui.home.HomeActivity;
import com.example.scouto.utils.Data;
import com.example.scouto.utils.FormValidator;
import com.example.scouto.utils.FunctionUtils;
import com.example.scouto.utils.Resource;
import com.example.scouto.viewmodel.authentication.SignupViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private SignupViewModel viewModel;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        FunctionUtils.focusScreen(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpClickListener();
        handleLiveDataStatusChange();
    }

    private void setUpClickListener() {
        binding.continueBtn.setOnClickListener(view -> signupUser());
        binding.signinTxt.setOnClickListener(view -> FunctionUtils.navigate(requireView(), null, R.id.action_signupFragment_to_signinFragment));
    }

    private void signupUser() {
        String email = Objects.requireNonNull(binding.emailEdit.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passwordEdit.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.confirmPasswordEdit.getText()).toString().trim();
        if (!validateForm(email, password, confirmPassword))
            return;
        FunctionUtils.hideKeyboard(requireContext(), requireView());
        viewModel.signupUser(email, password);
    }

    private void handleLiveDataStatusChange() {
        androidx.lifecycle.Observer<Resource<Data<FirebaseUser>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    binding.continueBtn.setClickable(false);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.continueBtnTxt.setText(getString(R.string.verifying));
                    break;
                case SUCCESS:
                    binding.continueBtn.setClickable(true);
                    FunctionUtils.toast(requireContext(), "Signed Up Successfully", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(requireContext(), HomeActivity.class);
                    startActivity(intent);
                    break;
                case EXCEPTION:
                    binding.continueBtn.setClickable(true);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.continueBtnTxt.setText(getString(R.string.continue_txt));
                    FunctionUtils.snackBar(requireView(), resource.getMessage() != null ? resource.getMessage() : getString(R.string.some_error_occurred), Snackbar.LENGTH_SHORT).show();
                    break;
            }
        };
        viewModel.signupUserLiveData.observe(getViewLifecycleOwner(), observer);
    }

    private boolean validateForm(String email, String password, String confirmPassword) {
        binding.emailContainer.setErrorEnabled(false);
        binding.passwordContainer.setErrorEnabled(false);
        binding.confirmPasswordContainer.setErrorEnabled(false);
        //validate email
        String emailVerificationResult = FormValidator.validateEmail(requireContext(), email);
        if (!emailVerificationResult.isEmpty()) {
            binding.emailContainer.setErrorEnabled(true);
            binding.emailContainer.setError(emailVerificationResult);
            FunctionUtils.animateView(binding.emailContainer, 500L, 0, Techniques.Shake);
            FunctionUtils.vibrateDevice(requireContext(), 300L);
            return false;
        }
        //validate password
        String passwordValidationResult = FormValidator.validatePassword(requireContext(), password);
        if (!passwordValidationResult.isEmpty()) {
            binding.passwordContainer.setErrorEnabled(true);
            binding.passwordContainer.setError(passwordValidationResult);
            FunctionUtils.animateView(binding.passwordContainer, 500L, 0, Techniques.Shake);
            FunctionUtils.vibrateDevice(requireContext(), 300L);
            return false;
        }
        //validate confirm password
        Boolean confirmPasswordValidationResult = FormValidator.validateConfirmPassword(password, confirmPassword);
        if (!confirmPasswordValidationResult) {
            binding.confirmPasswordContainer.setErrorEnabled(true);
            binding.confirmPasswordContainer.setError(getString(R.string.password_not_match));
            FunctionUtils.animateView(binding.confirmPasswordContainer, 500L, 0, Techniques.Shake);
            FunctionUtils.vibrateDevice(requireContext(), 300L);
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}