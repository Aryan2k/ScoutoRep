package com.example.scouto.ui.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.example.scouto.R;
import com.example.scouto.databinding.FragmentForgotPasswordBinding;
import com.example.scouto.utils.Data;
import com.example.scouto.utils.FormValidator;
import com.example.scouto.utils.FunctionUtils;
import com.example.scouto.utils.Resource;
import com.example.scouto.viewmodel.authentication.ForgotPasswordViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private ForgotPasswordViewModel viewModel;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
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
        binding.submitBtn.setOnClickListener(view -> sendResetPasswordLink());
    }

    public void sendResetPasswordLink() {
        String email = Objects.requireNonNull(binding.emailEdit.getText()).toString().trim();
        if (!validateForm(email))
            return;
        FunctionUtils.hideKeyboard(requireContext(), requireView());
        viewModel.sendResetPasswordLink(email);
    }

    private void handleLiveDataStatusChange() {
        androidx.lifecycle.Observer<Resource<Data<Boolean>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    binding.submitBtn.setClickable(false);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.forgotPasswordBtnTxt.setText(getString(R.string.verifying));
                    break;
                case SUCCESS:
                    binding.submitBtn.setClickable(true);
                    FunctionUtils.snackBar(requireView(), getString(R.string.a_reset_password_link_has_been_sent_to_the_registered_email_address), Snackbar.LENGTH_SHORT).show();
                    FunctionUtils.navigate(requireView(), null, R.id.action_forgotPasswordFragment_to_signinFragment);
                    break;
                case EXCEPTION:
                    binding.submitBtn.setClickable(true);
                    FirebaseAuth.getInstance().signOut();
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.forgotPasswordBtnTxt.setText(getString(R.string.continue_txt));
                    FunctionUtils.snackBar(requireView(), resource.getMessage() != null ? resource.getMessage() : getString(R.string.some_error_occurred), Snackbar.LENGTH_SHORT).show();
                    break;
            }
        };
        viewModel.sendResetPasswordLinkLiveData.observe(getViewLifecycleOwner(), observer);
    }

    private boolean validateForm(String email) {
        binding.emailContainer.setErrorEnabled(false);
        //validate email
        String emailVerificationResult = FormValidator.validateEmail(requireContext(), email);
        if (!emailVerificationResult.isEmpty()) {
            binding.emailContainer.setErrorEnabled(true);
            binding.emailContainer.setError(emailVerificationResult);
            FunctionUtils.animateView(binding.emailContainer, 500L, 0, Techniques.Shake);
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