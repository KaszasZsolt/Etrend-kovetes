package com.example.etrend_kovetes;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class FogyasztasAdapter  extends RecyclerView.Adapter<FogyasztasAdapter.ViewHolder> implements Filterable {
    private  ArrayList<Etel> mEtelItemsData;
    private  ArrayList<Etel> mEtelItemsDataAll;
    private Context mContext;
    private int lastPosition=-1;
    FogyasztasAdapter(Context context, ArrayList<Etel> itemsData){
        this.mEtelItemsData=itemsData;
        this.mEtelItemsDataAll=itemsData;
        this.mContext=context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_list,parent,false));
    }

    @Override
    public void onBindViewHolder(FogyasztasAdapter.ViewHolder holder, int position) {
        Etel currentItem =mEtelItemsData.get(position);
        holder.bindTo(currentItem);
        if(holder.getAdapterPosition()>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition=holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mEtelItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return fogyasztasFilter;
    }
    private Filter fogyasztasFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Etel> filteredList=new ArrayList<>();
            FilterResults results=new FilterResults();
            if(charSequence==null || charSequence.length()==0){
                results.count=mEtelItemsDataAll.size();
                results.values=mEtelItemsDataAll;
            }
            else{
                String filterPatter=charSequence.toString().toLowerCase().trim();
                for (Etel item: mEtelItemsDataAll){
                    if(item.getName().toLowerCase().contains(filterPatter)){
                        filteredList.add(item);
                    }
                }
                results.count=filteredList.size();
                results.values=filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mEtelItemsData=(ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mMibolText;
        private TextView mMikorText;
        private TextView mMennyitText;
        private TextView mSzerkeztText;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMibolText=itemView.findViewById(R.id.list_what);
            mMikorText=itemView.findViewById(R.id.list_when);
            mMennyitText=itemView.findViewById(R.id.list_weight);
            mSzerkeztText=itemView.findViewById(R.id.bSearch);

            mSzerkeztText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("acticit","szerkeztclikce ");
                }
            });
        }

        public void bindTo(Etel currentItem) {
            mMibolText.setText(currentItem.getName());
            mMennyitText.setText(currentItem.getAmount());
            mMikorText.setText((int) currentItem.getCreateDate());

        }
    }
}

