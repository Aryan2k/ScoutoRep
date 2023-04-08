package com.example.scouto.viewmodel.authentication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.scouto.repository.AuthenticationRepository;
import com.example.scouto.utils.Data;
import com.example.scouto.utils.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ForgotPasswordViewModel extends ViewModel {

    private final AuthenticationRepository repository;

    @Inject
    public ForgotPasswordViewModel(AuthenticationRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Resource<Data<Boolean>>> sendResetPasswordLinkLiveData = new MutableLiveData<>();

    public void sendResetPasswordLink(String email) {
        Resource<Data<Boolean>> resource = new Resource<>(null, null, null);
        sendResetPasswordLinkLiveData.setValue(resource.loading(new Data<>()));

        repository.sendResetPasswordLink(email, sendResetPasswordLinkLiveData);
    }
}
