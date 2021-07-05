package com.example.nexustutor.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexustutor.Offer;
import com.example.nexustutor.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SessionRecyclerViewAdapter extends RecyclerView.Adapter<SessionRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Offer> mData;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cardviewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
        public CardView cardviewRequest;
        public TextView mName, mSubject, mLocation, mGender, mDate;
        public Button btnAccept, btnCancel;
        public ImageView imgMale, imgFemale, imgProfile;

        public TextView mActiveName, mActiveSubject, mActiveDate;
        public Button mActiveContact;
        public CircleImageView mActiveImg;
        public CardView cardviewActive;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //RequestActivity Item
            cardviewRequest = (CardView) itemView.findViewById(R.id.cardview_confirm_request);
            mName = (TextView)itemView.findViewById(R.id.tv_request_name);
            mSubject =(TextView) itemView.findViewById(R.id.tv_request_subject);
            mLocation = (TextView)itemView.findViewById(R.id.tv_request_location);
            mGender = (TextView)itemView.findViewById(R.id.tv_student_gender);
            mDate =(TextView) itemView.findViewById(R.id.tv_request_date2);
            btnAccept = (Button) itemView.findViewById(R.id.btn_request_accept);
            btnCancel = (Button) itemView.findViewById(R.id.btn_request_cancel);
            imgMale = itemView.findViewById(R.id.img_male);
            imgFemale  = itemView.findViewById(R.id.img_female);
            imgProfile = itemView.findViewById(R.id.img_tutor);


            //ActiveStudent Item
            mActiveName = (TextView)itemView.findViewById(R.id.tv_active_name);
            mActiveSubject = (TextView)itemView.findViewById(R.id.tv_active_subject);
            mActiveDate = (TextView)itemView.findViewById(R.id.tv_active_subject);
            mActiveContact =(Button) itemView.findViewById(R.id.btn_contact);
            mActiveImg = (CircleImageView) itemView.findViewById(R.id.img_active_profile);
            cardviewActive = (CardView) itemView.findViewById(R.id.cardview_student_active);

        }
    }
}
