package com.example.etrend_kovetes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;
    private List<Etel> list;

    FogyasztasService fogyasztasService = new FogyasztasService();
    private int lastPosition=-1;


    public CustomAdapter(Context context, List<Etel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_list,parent,false));

    }



    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.mMibolText.setText(list.get(position).getName());
        holder.mMikorText.setText(convertMillisToDateTime(list.get(position).getCreateDate()));
        holder.mMennyitText.setText(list.get(position).getAmount());
        holder.mmertekegyseg.setText(list.get(position).getAmounttype());

        holder.mSzerkeztText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("acticit","szerkeztclikce "+list.get(position).getEtelId());
                holder.switchEditWhat.showNext();
                holder.switchlist_weight.showNext();
                holder.switchlist_mertekegyseg.showNext();
                if(holder.saveChange.getVisibility()==View.GONE){
                    holder.saveChange.setVisibility(View.VISIBLE);
                }
                else if (holder.saveChange.getVisibility()==View.VISIBLE){
                    holder.saveChange.setVisibility(View.GONE);
                }
                holder.editWeight.setText( list.get(position).getAmount());
                holder.editWhat.setText(list.get(position).getName());

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.mertekegysegek, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.switchspinner.setAdapter(adapter);
                String compareValue = list.get(position).getAmounttype();
                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    holder.switchspinner.setSelection(spinnerPosition);
                }
            }
        });

        holder.saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.editWhat.getText()==""){
                    return;
                }
                Spinner mertekegyseg = (Spinner) holder.switchlist_mertekegyseg.findViewById(R.id.switchlist_mertekegysegertek);

                Etel etel=new Etel(list.get(position).getId(), holder.editWhat.getText().toString(),holder.editWeight.getText().toString(), list.get(position).getEtelId(), mertekegyseg.getSelectedItem().toString(),list.get(position).getCreateDate());

                holder.progressBar.setVisibility(View.VISIBLE);
                fogyasztasService.updateEtel(etel, new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.progressBar.setVisibility(View.VISIBLE);


                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Sikeres update",
                                            Toast.LENGTH_SHORT).show();
                                    list.set(position,etel);
                                    holder.switchEditWhat.showNext();
                                    holder.switchlist_weight.showNext();
                                    holder.switchlist_mertekegyseg.showNext();
                                    holder.saveChange.setVisibility(View.GONE);
                                    holder.progressBar.setVisibility(View.GONE);
                                    notifyDataSetChanged();

                                } else {
                                    String localizedMessage = task.getException().getLocalizedMessage();
                                    Toast.makeText(context.getApplicationContext(), localizedMessage, Toast.LENGTH_SHORT).show();
                                    holder.progressBar.setVisibility(View.GONE);
                                }
                            }
                        }
                );

            }
        });
        holder.deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fogyasztasService.deleteEtel(list.get(position), new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Sikeres Törlés",
                                            Toast.LENGTH_SHORT).show();
                                    list.remove(list.get(position));
                                    notifyDataSetChanged();


                                } else {
                                    String localizedMessage = task.getException().getLocalizedMessage();
                                    Toast.makeText(context.getApplicationContext(), localizedMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }
        });

        if(holder.getAdapterPosition()>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition=holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static String convertMillisToDateTime(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date);
    }
}
