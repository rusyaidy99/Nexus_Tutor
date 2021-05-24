package com.example.nexustutor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexustutor.R;
import com.example.nexustutor.activity_view_tutor_profile;
import com.example.nexustutor.tutor;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    TextView textViewID, textViewName, textViewGender;


    private Context mContext;
    private List<tutor> mData;

    public RecyclerViewAdapter(Context mContext, List<tutor> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_search, parent,false);
        return new MyViewHolder(view);*/
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//        holder.tv_tutorname.setText(mData.get(position).getName());
//        holder.tv_gender.setText(mData.get(position).getLocation());
//        holder.img_tutor.setImageResource(mData.get(position).getThumbnail());

        //click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, activity_view_tutor_profile.class);
                intent.putExtra("Name", mData.get(position).getFullname());
                intent.putExtra("Gender", mData.get(position).getGender());
                intent.putExtra("AccType", mData.get(position).getAcctype());
                intent.putExtra("image", mData.get(position).getImage());
                /*intent.putExtra("Email", mData.get(position).getEmail());*/
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_tutorname, tvname, tv_gender, tvgender,tv_subject, tv_subject_history;
        public ImageView img_tutor;
        public CardView cardView;
        public Button imgBtnViewProfile;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tutorname = (TextView) itemView.findViewById(R.id.tv_tutorname);
            tvname = (TextView) itemView.findViewById(R.id.tv_name);
            tv_gender = (TextView) itemView.findViewById(R.id.tv_tutor_gender);
            tvgender = (TextView) itemView.findViewById(R.id.tv_gender);
            img_tutor = (ImageView) itemView.findViewById(R.id.img_tutor);
            cardView = (CardView) itemView.findViewById(R.id.cardview_tutor);
            tv_subject = itemView.findViewById(R.id.tv_tutor_subject);
            imgBtnViewProfile = (Button) itemView.findViewById(R.id.imgBtn_view_profile);

        }
    }
}
