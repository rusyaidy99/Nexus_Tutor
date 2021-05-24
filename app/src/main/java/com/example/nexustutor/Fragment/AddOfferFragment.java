package com.example.nexustutor.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nexustutor.Adapter.OfferRecyclerViewAdapter;
import com.example.nexustutor.Adapter.RecyclerViewAdapter;
import com.example.nexustutor.Offer;
import com.example.nexustutor.R;
import com.example.nexustutor.activity_comment_offer;
import com.example.nexustutor.activity_tutor_request;
import com.example.nexustutor.activity_upload_offer;
import com.example.nexustutor.tutor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class AddOfferFragment extends Fragment {
    ImageButton btn_upload,btn_delete_offer;
    TextView tvName;
    ImageView imgProfile;
    List<Offer> offerList;
    TextView postcount, countSession;

    DatabaseReference ref;
    DatabaseReference refUid;

    private FirebaseRecyclerOptions<Offer> options;
    private FirebaseRecyclerAdapter<Offer, OfferRecyclerViewAdapter.MyViewHolder> adapter;
    private RecyclerView recyclerView;
    /*private OfferRecyclerViewAdapter adapter;*/
    private  void getComments(String offerid, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Offers").child(offerid).child("Comments");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>0)
                    comments.setText("View All " + snapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View offerView = inflater.inflate(R.layout.fragment_add_offer,container, false);

        //declare variable
        tvName = offerView.findViewById(R.id.tv_name);
        imgProfile = offerView.findViewById(R.id.circleImgView_profile);
        countSession = offerView.findViewById(R.id.tv_count_session);
        postcount = offerView.findViewById(R.id.tv_count_post);
        btn_upload = offerView.findViewById(R.id.btn_create_offer);
        ref = FirebaseDatabase.getInstance().getReference().child("Offers");
        refUid = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


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

        final DatabaseReference myref = FirebaseDatabase.getInstance().getReference();

        myref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int countAct = (int) snapshot.child("Sessions").child("Actives").getChildrenCount();
                int countHis = (int) snapshot.child("Sessions").child("History").getChildrenCount();

                int totalcount = countAct +countHis;

                countSession.setText("" + totalcount);


               tvName.setText("" + snapshot.child("fullname").getValue());

               if (snapshot.child("image").exists())
                   Picasso.get().load("" + snapshot.child("image").getValue()).into(imgProfile);
               else
                   imgProfile.setImageResource(R.drawable.profile_icon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final Query myquery = myref.child("Offers").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Log.d("query", myquery.toString());
        myquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int value = (int) snapshot.getChildrenCount();
                postcount.setText("" + value);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Log.d(TAG, myquery.toString());

        options = new FirebaseRecyclerOptions.Builder<Offer>().setQuery(myquery, Offer.class).build();
        adapter = new FirebaseRecyclerAdapter<Offer, OfferRecyclerViewAdapter.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OfferRecyclerViewAdapter.MyViewHolder myViewHolder, int i, @NonNull final Offer Offer) {

                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(Offer.getUid())){
                    myViewHolder.img_btn_delete.setVisibility(View.VISIBLE);


                    myViewHolder.img_btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setMessage("Are you sure you want to delete this?");
                            alertDialog.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference refRemove = FirebaseDatabase.getInstance().getReference().child("Offers").child(Offer.getOid());
                                    refRemove.removeValue();
                                    Toast.makeText(getContext(), "Offer removed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            alertDialog.setCancelable(true);
                            alertDialog.show();

                        }
                    });
                }
                myViewHolder.imgBtnRequestOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2 = new Intent(getActivity(), activity_tutor_request.class);
                        intent2.putExtra("offerid", Offer.getOid());
                        intent2.putExtra("userid", Offer.getUid());
                        startActivity(intent2);
                    }
                });
                myViewHolder.etTitle.setText("" + Offer.getTitle());
                myViewHolder.etDescription.setText("" + Offer.getDescription());
                myViewHolder.etLocation.setText("" + Offer.getLocationCity() + ", " + Offer.getLocationState());
                myViewHolder.etGender.setText("" + Offer.getGender());
                myViewHolder.etSubject.setText("" + Offer.getSubject());

                getComments(Offer.getOid(), myViewHolder.tvCommment);

                /*myViewHolder.imgBtnRequestOff.setVisibility(View.GONE);*/


                myViewHolder.imgBtnComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), activity_comment_offer.class);
                        intent.putExtra("offerid", Offer.getOid());
                        intent.putExtra("userid", Offer.getUid());
                        startActivity(intent);
                    }
                });
                myViewHolder.tvCommment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), activity_comment_offer.class);
                        intent.putExtra("offerid", Offer.getOid());
                        intent.putExtra("userid", Offer.getUid());
                        startActivity(intent);
                    }
                });
                DatabaseReference refRequest = FirebaseDatabase.getInstance().getReference().child("Offers").child(Offer.getOid()).child("Requests");
                refRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String count = String.valueOf(snapshot.getChildrenCount());
                        myViewHolder.tvCountReq.setText(count);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                myViewHolder.tvCountReq.setVisibility(View.VISIBLE);

                DatabaseReference refOffer = FirebaseDatabase.getInstance().getReference().child("Offers").child(Offer.getOid());
                refOffer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("date").exists())
                        myViewHolder.etDate.setText("Posted on " + snapshot.child("date").getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                DatabaseReference refOwner = FirebaseDatabase.getInstance().getReference().child("Users").child(Offer.getUid());
                refOwner.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String user_name = snapshot.child("fullname").getValue().toString();
                        myViewHolder.etName.setText(user_name);
                        if (snapshot.child("image").exists()){
                            String user_image = snapshot.child("image").getValue().toString();
                            Picasso.get().load(user_image).into(myViewHolder.img_view_profile);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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