package com.example.nexustutor.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nexustutor.Adapter.OfferRecyclerViewAdapter;
import com.example.nexustutor.Adapter.RecyclerViewAdapter;
import com.example.nexustutor.Offer;
import com.example.nexustutor.R;
import com.example.nexustutor.activity_upload_offer;
import com.example.nexustutor.tutor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AddOfferFragment extends Fragment {
    ImageButton btn_upload;
    List<Offer> offerList;
    TextView postcount;

    DatabaseReference ref;
    DatabaseReference refUid;

    private FirebaseRecyclerOptions<Offer> options;
    private FirebaseRecyclerAdapter<Offer, OfferRecyclerViewAdapter.MyViewHolder> adapter;
    private RecyclerView recyclerView;
    /*private OfferRecyclerViewAdapter adapter;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View offerView = inflater.inflate(R.layout.fragment_add_offer,container, false);

        //declare variable
        postcount = offerView.findViewById(R.id.tv_count_post);
        btn_upload = offerView.findViewById(R.id.btn_create_offer);
        ref = FirebaseDatabase.getInstance().getReference().child("Offers");
        refUid = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int myOffer = (int) snapshot.child("Offers").getChildrenCount();
                postcount.setText("" + myOffer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refUid.addListenerForSingleValueEvent(valueEventListener);
        /*
        //Manual RecyclerView
        String title, description, locationCity, locationState, subject,  gender, location;
        title = "Title 1";
        description = "description";
        locationCity = "Kuala Terengganu";
        locationState = "Terengganu";
        subject = "Mathematics";
        gender = "Male Tutor only";

        offerList = new ArrayList<>();
        offerList.add(new Offer(title,description,locationCity,locationState,subject,gender, R.drawable.profile_image));
        offerList.add(new Offer(title,description,locationCity,locationState,subject,gender, R.drawable.profile_image));
        offerList.add(new Offer(title,description,locationCity,locationState,subject,gender, R.drawable.profile_image));
        offerList.add(new Offer(title,description,locationCity,locationState,subject,gender, R.drawable.profile_image));
        offerList.add(new Offer(title,description,locationCity,locationState,subject,gender, R.drawable.profile_image));
        offerList.add(new Offer(title,description,locationCity,locationState,subject,gender, R.drawable.profile_image));*/

        recyclerView = offerView.findViewById(R.id.recyclerview_offer);
        recyclerView.setHasFixedSize(true);
        /*adapter = new OfferRecyclerViewAdapter(getActivity(), offerList);*/
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayout);

        options = new FirebaseRecyclerOptions.Builder<Offer>().setQuery(ref, Offer.class).build();
        adapter = new FirebaseRecyclerAdapter<Offer, OfferRecyclerViewAdapter.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OfferRecyclerViewAdapter.MyViewHolder myViewHolder, int i, @NonNull Offer Offer) {
                myViewHolder.etTitle.setText("" + Offer.getTitle());
                myViewHolder.etDescription.setText("" + Offer.getDescription());
                myViewHolder.etLocation.setText("" + Offer.getLocationCity() + ", " + Offer.getLocationState());
                myViewHolder.etGender.setText("" + Offer.getGender());
                myViewHolder.etSubject.setText("" + Offer.getSubject());

            }

            @NonNull
            @Override
            public OfferRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_offer,parent, false);
                return new OfferRecyclerViewAdapter.MyViewHolder(v);

            }
        };
        adapter.startListening();
        /*recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));*/
        recyclerView.setAdapter(adapter);




        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_upload_offer.class);
                startActivity(intent);
            }
        });




        /*return inflater.inflate(R.layout.fragment_add_offer, container, false);*/
        return offerView;
    }


}