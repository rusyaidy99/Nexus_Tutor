package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nexustutor.Adapter.OfferRequestRecyclerVIewAdapter;
import com.example.nexustutor.Adapter.RecyclerViewAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class activity_tutor_request extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textView;
    DatabaseReference ref, refOffer;

    private FirebaseRecyclerOptions<OfferRequest> options;
    private FirebaseRecyclerAdapter<OfferRequest, OfferRequestRecyclerVIewAdapter.MyViewHolder> adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Offer Request");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);

        Intent intent = getIntent();
        String OfferID = intent.getExtras().getString("offerid");


        ref = FirebaseDatabase.getInstance().getReference().child("Tutors");
        refOffer = FirebaseDatabase.getInstance().getReference().child("Offers").child(OfferID).child("Requests");

        textView = findViewById(R.id.textView24);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity_tutor_request.this, 1));


        refOffer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String count = String.valueOf(snapshot.getChildrenCount());
                if (snapshot.getChildrenCount()>0){
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<OfferRequest>().setQuery(refOffer, OfferRequest.class).build();
        adapter = new FirebaseRecyclerAdapter<OfferRequest, OfferRequestRecyclerVIewAdapter.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OfferRequestRecyclerVIewAdapter.MyViewHolder myViewHolder, int i, @NonNull final OfferRequest offerRequest) {
                final String tid = offerRequest.getTid();

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String name = snapshot.child(tid).child("fullname").getValue().toString();
                        final String gender = snapshot.child(tid).child("gender").getValue().toString();
                        if (snapshot.child(tid).child("image").exists()){
                            String image =snapshot.child(tid).child("image").getValue().toString();
                            Picasso.get().load(image).into(myViewHolder.img_tutor);
                        }

                        myViewHolder.tvname.setText(name);
                        myViewHolder.tvgender.setText(gender);

                        myViewHolder.imgBtnViewProfile.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(activity_tutor_request.this, activity_view_tutor_profile.class);
                                intent.putExtra("uid", tid);
                                intent.putExtra("Name", name);
                                intent.putExtra("Gender", gender);
                                if (snapshot.child(tid).child("image").exists()){
                                    String image =snapshot.child(tid).child("image").getValue().toString();
                                    intent.putExtra("image", image);
                                }else
                                    intent.putExtra("image", "");
                                /*intent.putExtra("Email", mData.get(position).getEmail());*/
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public OfferRequestRecyclerVIewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_offer_request,parent, false);
                return new OfferRequestRecyclerVIewAdapter.MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}