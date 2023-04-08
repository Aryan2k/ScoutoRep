package com.example.scouto.viewmodel.authentication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.scouto.repository.AuthenticationRepository;
import com.example.scouto.utils.Data;
import com.example.scouto.utils.RequestStatus;
import com.example.scouto.utils.Resource;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SigninViewModel extends ViewModel {

    private final AuthenticationRepository repository;

    @Inject
    public SigninViewModel(AuthenticationRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Resource<Data<FirebaseUser>>> signinUserLiveData = new MutableLiveData<>();

    public void signinUser(String email, String password) {
        Resource<Data<FirebaseUser>> resource = new Resource<>(RequestStatus.LOADING, null, null);
        signinUserLiveData.postValue(resource);
        repository.signinUser(email, password, signinUserLiveData);
    }
}
