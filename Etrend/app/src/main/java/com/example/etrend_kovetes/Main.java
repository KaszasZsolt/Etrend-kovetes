package com.example.etrend_kovetes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;


public class Main extends AppCompatActivity {
    AuthService authService = new AuthService();
    Button fogyasztashozad;
    ProgressBar progressBar;
    FogyasztasService fogyasztasService = new FogyasztasService();
    EditText editTextmibol, editTextmennyit;
    private Spinner mertekegyseg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        addListenerOnSpinnerItemSelection();

        if (authService.getCurrentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        fogyasztashozad = findViewById(R.id.fogyasztashozad);
        editTextmibol=findViewById(R.id.mibol);
        editTextmennyit=findViewById(R.id.mennyit);
        progressBar=findViewById(R.id.progressBar);

        fogyasztashozad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fogyasztasService.getAllFogyasztas(authService.getCurrentUser().getUid());
                String mibol, mennyit;
                mibol = String.valueOf(editTextmibol.getText());
                mennyit = String.valueOf(editTextmennyit.getText());
                progressBar.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(mibol)){
                    Toast.makeText(Main.this, "Írd be, hogy mit fogyasztottál.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(mennyit)){
                    Toast.makeText(Main.this, "Írd be, hogy mennyit fogyasztottál.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                Etel etel=new Etel(authService.getCurrentUser().getUid(),mibol,mennyit,mertekegyseg.getSelectedItem().toString(),System.currentTimeMillis());
                fogyasztasService.fogyasztasHozzaad(etel, new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser user = authService.getCurrentUser();
                                Toast.makeText(Main.this, "Sikeres hozzáadás",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                String localizedMessage = task.getException().getLocalizedMessage();
                                    Toast.makeText(getApplicationContext(), localizedMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                );

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(this, "Kijelentkezés", Toast.LENGTH_SHORT).show();

                authService.logout();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_main:
                Toast.makeText(this, "main", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addListenerOnSpinnerItemSelection() {
        mertekegyseg = (Spinner) findViewById(R.id.mertekegyseg);
        mertekegyseg.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
}


