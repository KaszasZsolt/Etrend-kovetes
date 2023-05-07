package com.example.etrend_kovetes;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;

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

    public void fogyasztasHozzaad(Etel etel, OnCompleteListener<Void> listener){
        etel.setEtelId(db.collection("fogyasztas").document().getId());
        db.collection("fogyasztas").document(etel.getEtelId())
                .set(etel).addOnCompleteListener(listener);
    }
    public void getAllFogyasztas(String id, OnSuccessListener<List<Etel>> listener) {
        mItemsData.clear();
        db.collection("fogyasztas").whereEqualTo("id", id).orderBy("createDate").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document:queryDocumentSnapshots){
                if (document.get("name")!=null){
                    Etel etel=document.toObject(Etel.class);
                    mItemsData.add(etel);
                }
            }

            listener.onSuccess(mItemsData);
        });
    }

    public void updateEtel(Etel etel, OnCompleteListener<Void> listener) {
        db.collection("fogyasztas").document(etel.getEtelId())
                .update("amount", etel.getAmount(),"amounttype",etel.getAmounttype(),"name",etel.getName(),"createDate",etel.getCreateDate()).addOnCompleteListener(listener);
    }
    public void deleteEtel(Etel etel, OnCompleteListener<Void> listener) {
        db.collection("fogyasztas").document(etel.getEtelId())
                .delete().addOnCompleteListener(listener);
    }

}