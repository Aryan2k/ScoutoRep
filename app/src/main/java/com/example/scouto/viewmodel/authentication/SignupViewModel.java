package com.example.scouto.viewmodel.authentication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.scouto.repository.AuthenticationRepository;
import com.example.scouto.utils.Data;
import com.example.scouto.utils.Resource;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignupViewModel extends ViewModel {
    private final AuthenticationRepository repository;

    @Inject
    public SignupViewModel(AuthenticationRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Resource<Data<FirebaseUser>>> signupUserLiveData = new MutableLiveData<>();

    public void signupUser(String email, String password) {
        Resource<Data<FirebaseUser>> resource = new Resource<>(null, null, null);
        signupUserLiveData.setValue(resource.loading(new Data<>()));
        repository.signupUser(email, password, signupUserLiveData);
    }
}
