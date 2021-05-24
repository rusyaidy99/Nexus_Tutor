package com.example.nexustutor.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexustutor.Offer;
import com.example.nexustutor.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryStudentRecyclerAdapter extends RecyclerView.Adapter<HistoryStudentRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<Offer> mData;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cardviewActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mActiveName, mActiveSubject, mActiveDate;
        public Button mActiveContact, mActivePay;
        public CircleImageView mActiveImg;
        public CardView cardviewActive;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //ActiveStudent Item
            mActiveName = (TextView)itemView.findViewById(R.id.tv_active_name);
            mActiveSubject = (TextView)itemView.findViewById(R.id.tv_active_subject);
            mActiveDate = (TextView)itemView.findViewById(R.id.tv_active_date);
            mActiveContact =(Button) itemView.findViewById(R.id.btn_contact);
            mActiveImg = (CircleImageView) itemView.findViewById(R.id.img_active_profile);
            cardviewActive = (CardView) itemView.findViewById(R.id.cardview_student_active);
            mActivePay = (Button) itemView.findViewById(R.id.btn_pay);
        }
    }
}
