package com.example.scouto.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.scouto.utils.Data;
import com.example.scouto.utils.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import javax.inject.Inject;

public class AuthenticationRepository {
    FirebaseFirestore database;
    FirebaseAuth auth;

    @Inject
    public AuthenticationRepository() {
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void signinUser(String email, String password, MutableLiveData<Resource<Data<FirebaseUser>>> signinUserLiveData) {
        Resource<Data<FirebaseUser>> resource = new Resource<>(null, null, null);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        signinUserLiveData.postValue(resource.success(new Data<>()));
                    } else {
                        signinUserLiveData.postValue(resource.exception(new Data<>(), Objects.requireNonNull(task.getException()).getMessage()));
                    }
                });
    }


    public void signupUser(String email, String password, MutableLiveData<Resource<Data<FirebaseUser>>> signupUserLiveData) {
        Resource<Data<FirebaseUser>> resource = new Resource<>(null, null, null);
        Data<FirebaseUser> data = new Data<>();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        data.set(task.getResult().getUser());
                        signupUserLiveData.setValue(resource.success(data));
                    } else {
                        signupUserLiveData.setValue(resource.exception(new Data<>(), Objects.requireNonNull(task.getException()).getMessage()));
                    }
                });
    }

    public void sendResetPasswordLink(String email, MutableLiveData<Resource<Data<Boolean>>> sendResetPasswordLinkLiveData) {
        Resource<Data<Boolean>> resource = new Resource<>(null, null, null);
        Data<Boolean> data = new Data<>();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                data.set(true);
                sendResetPasswordLinkLiveData.setValue(resource.success(data));
            } else {
                sendResetPasswordLinkLiveData.setValue(resource.exception(new Data<>(), Objects.requireNonNull(task.getException()).getMessage()));
            }
        });
    }
}
