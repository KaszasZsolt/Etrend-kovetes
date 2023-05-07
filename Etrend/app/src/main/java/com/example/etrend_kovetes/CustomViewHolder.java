package com.example.etrend_kovetes;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    public TextView mMibolText;
    public TextView mMikorText;
    public TextView mMennyitText;
    public ImageButton mSzerkeztText;
    public ImageButton saveChange;
    public ImageButton deleteAll;

    public TextView mmertekegyseg;
    public ViewSwitcher switchlist_weight;
    public ViewSwitcher switchEditWhat;
    public ViewSwitcher switchlist_mertekegyseg;

    public TextView editWhat;
    public TextView editWeight;
    public Spinner switchspinner;

    public ProgressBar progressBar;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        mMibolText=itemView.findViewById(R.id.list_what);
        mMikorText=itemView.findViewById(R.id.list_when);
        mmertekegyseg=itemView.findViewById(R.id.list_mertekegyseg);
        mMennyitText=itemView.findViewById(R.id.list_weight);

        switchlist_weight=(ViewSwitcher) itemView.findViewById(R.id.switchlist_weight);
        switchEditWhat=(ViewSwitcher)  itemView.findViewById(R.id.switchEditWhat);
        switchlist_mertekegyseg=(ViewSwitcher)  itemView.findViewById(R.id.switchlist_mertekegyseg);

        editWhat=itemView.findViewById(R.id.editWhat);
        editWeight=itemView.findViewById(R.id.editWeight);


        switchspinner = (Spinner)itemView.findViewById(R.id.switchlist_mertekegysegertek);
        switchspinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        mSzerkeztText=itemView.findViewById(R.id.bSearch);
        saveChange=itemView.findViewById(R.id.saveChange);
        deleteAll=itemView.findViewById(R.id.deleteAll);
        progressBar=itemView.findViewById(R.id.progressBar);

    }


}
