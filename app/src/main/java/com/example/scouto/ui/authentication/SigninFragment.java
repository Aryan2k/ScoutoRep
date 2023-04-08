package com.example.scouto.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.example.scouto.R;
import com.example.scouto.databinding.FragmentSigninBinding;
import com.example.scouto.ui.home.HomeActivity;
import com.example.scouto.utils.Data;
import com.example.scouto.utils.FormValidator;
import com.example.scouto.utils.FunctionUtils;
import com.example.scouto.utils.Resource;
import com.example.scouto.viewmodel.authentication.SigninViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SigninFragment extends Fragment {

    private FragmentSigninBinding binding;
    private SigninViewModel viewModel;

    public SigninFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSigninBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(SigninViewModel.class);
        FunctionUtils.focusScreen(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpClickListener();
        handleSigninUserLiveDataStatusChange();
    }

    private void setUpClickListener() {
        binding.signupTxt.setOnClickListener(view -> FunctionUtils.navigate(requireView(), null, R.id.action_signinFragment_to_signupFragment));
        binding.forgotPasswordTxt.setOnClickListener(view -> FunctionUtils.navigate(requireView(), null, R.id.action_signinFragment_to_forgotPasswordFragment));
        binding.signinBtn.setOnClickListener(view -> signinUser());
    }

    private void handleSigninUserLiveDataStatusChange() {
        androidx.lifecycle.Observer<Resource<Data<FirebaseUser>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    binding.signinBtn.setClickable(false);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.signinBtnTxt.setText(getString(R.string.verifying));
                    break;
                case SUCCESS:
                    binding.signinBtn.setClickable(true);
                    FunctionUtils.toast(requireContext(), "Signed In Successfully", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(requireContext(), HomeActivity.class);
                    startActivity(intent);
                    break;
                case EXCEPTION:
                    binding.signinBtn.setClickable(true);
                    FirebaseAuth.getInstance().signOut();
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.signinBtnTxt.setText(getString(R.string.continue_txt));
                    FunctionUtils.snackBar(requireView(), resource.getMessage() != null ? resource.getMessage() : getString(R.string.some_error_occurred), Snackbar.LENGTH_SHORT).show();
                    break;
            }
        };
        viewModel.signinUserLiveData.observe(getViewLifecycleOwner(), observer);
    }

    private void signinUser() {
        String email = Objects.requireNonNull(binding.emailEdit.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passwordEdit.getText()).toString().trim();
        if (!validateForm(email, password))
            return;
        Log.e("abc", "signinUser: ");
        FunctionUtils.hideKeyboard(requireContext(), requireView());
        viewModel.signinUser(email, password);
    }

    private Boolean validateForm(String email, String password) {
        binding.emailContainer.setErrorEnabled(false);
        binding.passwordContainer.setErrorEnabled(false);
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
        return true;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}