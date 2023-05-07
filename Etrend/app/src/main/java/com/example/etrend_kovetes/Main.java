package com.example.etrend_kovetes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class Main extends AppCompatActivity {


    RecyclerView recyclerView;
    List<Etel> myModelList;
    CustomAdapter customAdapter;
    private RecyclerView mRecyclerView;



    AuthService authService = new AuthService();
    Button fogyasztashozad;
    ProgressBar progressBar;
    FogyasztasService fogyasztasService = new FogyasztasService();
    EditText editTextmibol, editTextmennyit;
    private CollectionReference mItems;
    private Spinner mertekegyseg;

    private ArrayList<Etel> mItemsData;
    private FogyasztasAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private int gridNumber=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mRecyclerView=findViewById(R.id.recyclerView);
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
                Etel etel=new Etel(authService.getCurrentUser().getUid(),mibol,mennyit, "", mertekegyseg.getSelectedItem().toString(),System.currentTimeMillis());
                fogyasztasService.fogyasztasHozzaad(etel, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser user = authService.getCurrentUser();
                                Toast.makeText(Main.this, "Sikeres hozzáadás",
                                        Toast.LENGTH_SHORT).show();
                                displayItems();
                            } else {
                                String localizedMessage = task.getException().getLocalizedMessage();
                                    Toast.makeText(getApplicationContext(), localizedMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                );

            }
        });


        mRecyclerView = findViewById(R.id.recyclerView);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));
        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();
        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new FogyasztasAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");



        displayItems();
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

    }

    private void displayItems() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        myModelList=new ArrayList<>();
        Context contexfor=this;
        fogyasztasService.getAllFogyasztas(authService.getCurrentUser().getUid(), new OnSuccessListener<List<Etel>>() {
            @Override
            public void onSuccess(List<Etel> fogyasztasList) {
                if (fogyasztasList == null) {
                    return;
                }


                myModelList=new ArrayList<>();
                for (Etel document : fogyasztasList) {
                    Etel item = document;
                    myModelList.add(item);
                }

                customAdapter=new CustomAdapter(contexfor,myModelList);
                recyclerView.setAdapter(customAdapter);

            }

        });


    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Snackbar snackbar=Snackbar.make(mRecyclerView,"Mező törölve.",Snackbar.LENGTH_LONG);
            snackbar.show();


        }
    };




    private void initializeData() {
        fogyasztasService.getAllFogyasztas(authService.getCurrentUser().getUid(), new OnSuccessListener<List<Etel>>() {
            @Override
            public void onSuccess(List<Etel> fogyasztasList) {
                if (fogyasztasList == null) {
                    return;
                }
                mItemsData.clear();
                mItemsData= (ArrayList<Etel>) fogyasztasList;
                mAdapter.notifyDataSetChanged();

                Log.d(TAG, "Current fogyasztas in fogyasztasok: " + mAdapter.getItemCount());

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
                Intent intent2 = new Intent(getApplicationContext(), Main.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.menu_profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(getApplicationContext(), profil_setting.class);
                startActivity(intent3);
                finish();
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


