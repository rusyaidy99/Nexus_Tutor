package com.example.nexustutor.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexustutor.Offer;
import com.example.nexustutor.R;

import java.util.List;

public class OfferRequestRecyclerVIewAdapter extends RecyclerView.Adapter<OfferRequestRecyclerVIewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Offer> mData;
    @NonNull
    @Override
    public OfferRequestRecyclerVIewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferRequestRecyclerVIewAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

            return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvname,  tvgender;
        public ImageView img_tutor;
        public Button imgBtnViewProfile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname = (TextView) itemView.findViewById(R.id.tv_name);
            tvgender = (TextView) itemView.findViewById(R.id.tv_gender);
            imgBtnViewProfile = (Button) itemView.findViewById(R.id.imgBtn_view_profile);
            img_tutor = (ImageView) itemView.findViewById(R.id.img_tutor);
        }
    }
}
