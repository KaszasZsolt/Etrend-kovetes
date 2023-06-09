package com.example.etrend_kovetes;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthService extends Service {
    private FirebaseAuth mAuth;
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        AuthService getService() {
            return AuthService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public FirebaseUser getCurrentUser() {
        this.onCreate();
        return mAuth.getCurrentUser();
    }

    public void registerUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        this.onCreate();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        this.onCreate();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void logout() {

        this.onCreate();
        mAuth.signOut();
    }
}