package com.example.etrend_kovetes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class Register extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword,editTextPasswordagain;
    Button buttonReg;
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
        setContentView(R.layout.activity_register);
        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        editTextPasswordagain=findViewById(R.id.passwordagain);
        buttonReg=findViewById(R.id.btn_register);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.loginNow);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password,passwordagain;
                progressBar.setVisibility(View.VISIBLE);
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText().toString());
                passwordagain=String.valueOf(editTextPasswordagain.getText().toString());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Emailcím megadása kötelező", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Jelszó megadása kötelező", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                if (TextUtils.isEmpty(passwordagain)){
                    Toast.makeText(Register.this, "Jelszó megerősítése kötelező", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                if (!password.equals(passwordagain)){
                    Toast.makeText(Register.this, "Nem egyezik a jelszó", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                authService.registerUser(email, password, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "A fiók elkészült.",
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
                                    else if(localizedMessage.equals("The given password is invalid. [ Password should be at least 6 characters ]"))
                                        Toast.makeText(getApplicationContext(), "A jelszó hossza legalább 6 karakter.", Toast.LENGTH_SHORT).show();
                                    else if(localizedMessage.equals("The email address is already in use by another account."))
                                        Toast.makeText(getApplicationContext(), "Az email cím már foglalt.", Toast.LENGTH_SHORT).show();
                                    else{
                                        Toast.makeText(getApplicationContext(), "Hibás regisztráció.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                });
            }
        });
    }
}