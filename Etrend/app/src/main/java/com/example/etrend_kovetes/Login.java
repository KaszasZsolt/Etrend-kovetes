package com.example.etrend_kovetes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView textView;
    AuthService authService = new AuthService();
    @Override
    public void onStart() {
        super.onStart();

        if(authService.getCurrentUser() != null){
            Intent intent=new Intent((getApplicationContext()),Main.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        buttonLogin=findViewById(R.id.btn_login);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.registerNow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                progressBar.setVisibility(View.VISIBLE);
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText().toString());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Emailcím megadása kötelező", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Jelszó megadása kötelező", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }


                authService.loginUser(email, password, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = authService.getCurrentUser();
                            Toast.makeText(Login.this, "Sikeres bejelentkezés.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent((getApplicationContext()),Main.class);
                            startActivity(intent);
                            finish();
                        } else {

                            String localizedMessage = task.getException().getLocalizedMessage();
                            if(localizedMessage.equals("The email address is badly formatted."))
                                Toast.makeText(getApplicationContext(), "Az emailcím rosszul van formázva.", Toast.LENGTH_SHORT).show();
                            else if(localizedMessage.equals("The password is invalid or the user does not have a password."))
                                Toast.makeText(getApplicationContext(), "A jelszó érvénytelen.", Toast.LENGTH_SHORT).show();
                            else if(localizedMessage.equals("There is no user record corresponding to this identifier. The user may have been deleted."))
                                Toast.makeText(getApplicationContext(), "Nincs ilyen felhasználó.", Toast.LENGTH_SHORT).show();
                            else{
                                Toast.makeText(getApplicationContext(), "Hibás bejelentkezés.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });


            }
        });


    }
}