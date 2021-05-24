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

import com.example.nexustutor.Education;
import com.example.nexustutor.R;
import com.example.nexustutor.Subjek;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EducationRecyclerViewAdapter extends RecyclerView.Adapter<EducationRecyclerViewAdapter.MyViewHolder> {
    private List<Education> mData;
    private Context mContext;

    public EducationRecyclerViewAdapter(List<Education> mData) {
        this.mData = mData;
    }

    public EducationRecyclerViewAdapter(Context mContext, List<Education> mData ) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_education, parent, false);

        /*LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_subject,parent,false);*/
        return new MyViewHolder(view);
        /*return null;*/
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.etEducation.setText(mData.get(position).getEducation());

        DatabaseReference Tref = FirebaseDatabase.getInstance().getReference().child("Tutors");
        Tref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    holder.removeEducation.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /* Subjek subjek = mData.get(position);*/
        holder.cardViewEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.removeEducation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Educations");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(mData.get(position).getEducation()))
                                    reference.child(mData.get(position).getEducation()).removeValue();
                                Toast.makeText(mContext, "Education Removed", Toast.LENGTH_SHORT).show();
                                Log.d("education", "success");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView etEducation;
        public CardView cardViewEducation;
        public ImageButton removeEducation;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            etEducation = (TextView) itemView.findViewById(R.id.tv_education);
            cardViewEducation = (CardView) itemView.findViewById(R.id.cardView_education);
            removeEducation = (ImageButton) itemView.findViewById(R.id.img_btn_remove_education);
        }
    }
}
