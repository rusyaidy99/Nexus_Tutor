package com.example.nexustutor.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexustutor.Offer;
import com.example.nexustutor.R;

import com.example.nexustutor.Subjek;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SubjectRecyclerViewAdapter extends RecyclerView.Adapter<SubjectRecyclerViewAdapter.MyViewHolder>{

    private Context mContext;
    private List<Subjek> mData;

    public SubjectRecyclerViewAdapter(List<Subjek> mData) {
        this.mData = mData;
    }

    public SubjectRecyclerViewAdapter(Context mContext, List<Subjek> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_subject, parent, false);

        /*LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_subject,parent,false);*/
        return new MyViewHolder(view);
        /*return null;*/
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.etsubject.setText(mData.get(position).getMySubject());

        DatabaseReference Tref = FirebaseDatabase.getInstance().getReference().child("Tutors");
        Tref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    holder.removeSubject.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       /* Subjek subjek = mData.get(position);*/
        holder.removeSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Subjects");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(mData.get(position).getMySubject()))
                            reference.child(mData.get(position).getMySubject()).removeValue();
                        Toast.makeText(mContext, "Subject Removed", Toast.LENGTH_SHORT).show();
                        Log.d("subjek", "success");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView etsubject;
        public CardView cardViewSubject;
        public ImageButton removeSubject;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            etsubject = (TextView) itemView.findViewById(R.id.tv_subject);
            cardViewSubject = (CardView) itemView.findViewById(R.id.cardView_subject);
            removeSubject = (ImageButton) itemView.findViewById(R.id.img_btn_remove_subject);
        }
    }
}
