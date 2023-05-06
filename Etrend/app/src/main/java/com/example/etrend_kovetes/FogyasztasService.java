package com.example.etrend_kovetes;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FogyasztasService extends Service {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Etel> mItemsData=new ArrayList<>();
    public FogyasztasService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void fogyasztasHozzaad(Etel etel, OnCompleteListener<DocumentReference> listener){
        db.collection("fogyasztas")
                .add(etel).addOnCompleteListener(listener);
    }
    public void getAllFogyasztas(String id) {
        mItemsData.clear();
        db.collection("fogyasztas").whereEqualTo("id", id).orderBy("createDate").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document:queryDocumentSnapshots){
                if (document.get("name")!=null){
                    Etel etel=document.toObject(Etel.class);
                    mItemsData.add(etel);
                }
            }
            if (mItemsData.size() == 0) {
                getAllFogyasztas(id);
            }

            for (Etel etel : mItemsData) {
                Log.d(TAG, "Etel neve: " + etel.getName());
                Log.d(TAG, "Etel mennyisége: " + etel.getAmount());
            }
            Log.d(TAG, "Current fogyasztas in fogyasztasok: " + mItemsData);
        });
    }
}