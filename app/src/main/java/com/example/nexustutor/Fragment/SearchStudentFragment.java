package com.example.nexustutor.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nexustutor.Adapter.OfferRecyclerViewAdapter;
import com.example.nexustutor.Adapter.RecyclerViewAdapter;
import com.example.nexustutor.Offer;
import com.example.nexustutor.OfferRequest;
import com.example.nexustutor.R;
import com.example.nexustutor.activity_comment_offer;
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


public class SearchStudentFragment extends Fragment {
    Toolbar toolbar;
    List<tutor> tutorList;


    DatabaseReference ref;
    DatabaseReference refUid, refTutor;

    private FirebaseRecyclerOptions<Offer> options;
    private FirebaseRecyclerAdapter<Offer, OfferRecyclerViewAdapter.MyViewHolder> adapter;
    private RecyclerView recyclerView;

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

        View searchOfferView = inflater.inflate(R.layout.fragment_search_student, container, false);


        toolbar = searchOfferView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        recyclerView = searchOfferView.findViewById(R.id.recyclerview_offer_search);


       /* EditText searchBar = (EditText) searchOfferView.findViewById(R.id.search_bar);
        final String searchText = searchBar.getText().toString().trim();*/

        ref = FirebaseDatabase.getInstance().getReference().child("Offers");
        refUid = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refTutor = FirebaseDatabase.getInstance().getReference().child("Tutors");
        /*Button btnSearch = searchOfferView.findViewById(R.id.btn_search);*/



        recyclerView.setHasFixedSize(true);
        /*adapter = new OfferRecyclerViewAdapter(getActivity(), offerList);*/
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayout);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = (int) snapshot.getChildrenCount();

                Log.d("countOffer", String.valueOf(value));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Offer>().setQuery(ref, Offer.class).build();
        adapter = new FirebaseRecyclerAdapter<Offer, OfferRecyclerViewAdapter.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OfferRecyclerViewAdapter.MyViewHolder myViewHolder, int i, @NonNull final Offer Offer) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(Offer.getOid()).child("Requests").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            myViewHolder.imgBtnRequestOff.setVisibility(View.GONE);
                            myViewHolder.imgBtnRequestOn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                myViewHolder.etTitle.setText("" + Offer.getTitle());
                myViewHolder.etDescription.setText("" + Offer.getDescription());
                myViewHolder.etLocation.setText("" + Offer.getLocationCity() + ", " + Offer.getLocationState());
                myViewHolder.etGender.setText("" + Offer.getGender());
                myViewHolder.etSubject.setText("" + Offer.getSubject());

                getComments(Offer.getOid(), myViewHolder.tvCommment);

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

                myViewHolder.imgBtnRequestOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OfferRequest offerRequest = new OfferRequest(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        ref.child(Offer.getOid()).child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(offerRequest);
                        refTutor.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Offer").child(Offer.getOid()).child("status").setValue(true);
                        myViewHolder.imgBtnRequestOff.setVisibility(View.GONE);
                        myViewHolder.imgBtnRequestOn.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "You requested this offer", Toast.LENGTH_SHORT).show();
                    }
                });

                myViewHolder.imgBtnRequestOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref.child(Offer.getOid()).child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                refTutor.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Offer").child(Offer.getOid()).removeValue();
                                myViewHolder.imgBtnRequestOn.setVisibility(View.GONE);
                                myViewHolder.imgBtnRequestOff.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

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

        /*btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Log.d("myText", searchText);
                searchStudent(searchText);*//*

            }
        });*/
        return searchOfferView;
    }

    private void searchStudent(String SearchText) {
        Log.d("search", SearchText);
        Query searchQuery = ref.orderByChild("title").startAt(SearchText).endAt(SearchText + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Offer>().setQuery(searchQuery, Offer.class).build();
        FirebaseRecyclerAdapter<Offer, OfferRecyclerViewAdapter.MyViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Offer, OfferRecyclerViewAdapter.MyViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final OfferRecyclerViewAdapter.MyViewHolder myViewHolder, int i, @NonNull final Offer Offer) {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child(Offer.getOid()).child("Requests").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    myViewHolder.imgBtnRequestOff.setVisibility(View.GONE);
                                    myViewHolder.imgBtnRequestOn.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        myViewHolder.etTitle.setText("" + Offer.getTitle());
                        myViewHolder.etDescription.setText("" + Offer.getDescription());
                        myViewHolder.etLocation.setText("" + Offer.getLocationCity() + ", " + Offer.getLocationState());
                        myViewHolder.etGender.setText("" + Offer.getGender());
                        myViewHolder.etSubject.setText("" + Offer.getSubject());

                        getComments(Offer.getOid(), myViewHolder.tvCommment);

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
        firebaseRecyclerAdapter.startListening();
        /*recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));*/
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchStudent(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchStudent(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

       /* if (id == R.id.action_settings){
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}