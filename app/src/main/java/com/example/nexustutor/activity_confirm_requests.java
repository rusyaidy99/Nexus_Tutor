package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.nexustutor.Adapter.OfferRecyclerViewAdapter;
import com.example.nexustutor.Adapter.SessionRecyclerViewAdapter;
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
import java.util.Arrays;

public class activity_confirm_requests extends AppCompatActivity {
    private ImageButton btnBack;
    private CardView cardviewNoHistory;

    private FirebaseRecyclerOptions<Session> options;
    private FirebaseRecyclerAdapter<Session, SessionRecyclerViewAdapter.MyViewHolder> adapter;
    private RecyclerView recyclerViewRequest;

    private DatabaseReference mRef, mSessionRef;
    private String mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_requests);

        //Define Id
        btnBack = findViewById(R.id.img_btn_back);
        recyclerViewRequest = findViewById(R.id.recyclerview_request);
        cardviewNoHistory = findViewById(R.id.cardView_no_history);



        //Firebase
        mAuth = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference("Tutors").child(mAuth).child("Requests");
        mSessionRef = FirebaseDatabase.getInstance().getReference("Sessions");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("count", String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.getChildrenCount()>0){

                    cardviewNoHistory.setVisibility(View.GONE);
                    recyclerViewRequest.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Recyclerview

        recyclerViewRequest.setHasFixedSize(true);
        /*adapter = new OfferRecyclerViewAdapter(getActivity(), offerList);*/
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewRequest.setLayoutManager(linearLayout);


        Query myquery = mSessionRef.orderByChild("status").equalTo("R" + mAuth);

        myquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countreq = (int) snapshot.getChildrenCount();
                Log.d("count request", String.valueOf(countreq));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        options = new FirebaseRecyclerOptions.Builder<Session>().setQuery(myquery, Session.class).build();
        adapter = new FirebaseRecyclerAdapter<Session, SessionRecyclerViewAdapter.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SessionRecyclerViewAdapter.MyViewHolder myViewHolder, int i, @NonNull final Session session) {
                final String studentID = session.getUid();
                String status = session.getStatus();

                final String[] name = new String[1];
                final String[] gender = new String[1];


                final DatabaseReference mStudentRef= FirebaseDatabase.getInstance().getReference("Users").child(studentID);
                mStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("image").exists()){
                            Picasso.get().load(snapshot.child("image").getValue().toString()).into(myViewHolder.imgProfile);
                        }
                        else{
                            Picasso.get().load(R.drawable.profile_icon).into(myViewHolder.imgProfile);
                        }


                        if (snapshot.child("city").exists() && snapshot.child("state").exists() & !snapshot.child("state").getValue().equals("") && !snapshot.child("city").getValue().equals("")){
                            String Location = snapshot.child("city").getValue().toString() + ", " + snapshot.child("state").getValue().toString();
                            myViewHolder.mLocation.setText("" + Location);
                        }
                        DatabaseReference myrefSubject = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference subjectRef = myrefSubject.child("Tutors").child(session.getTid()).child("Subjects");
                        final ArrayList subjects = new ArrayList<>();

                        subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds:snapshot.getChildren()){
                                    String data = ds.child("mySubject").getValue(String.class);
                                    subjects.add("" + String.valueOf(data));

                                }
                                String countt = String.valueOf(snapshot.getChildrenCount());
                                Log.d("subject", countt );
                                String subjectlist = Arrays.toString(subjects.toArray()).replace("[", "").replace("]", "").trim();
                                if (subjectlist.equals(""))
                                    myViewHolder.mSubject.setText("Subject Unavailable");
                                else
                                    myViewHolder.mSubject.setText(subjectlist);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myViewHolder.mDate.setText("" + session.getDate());

                        name[0] = snapshot.child("fullname").getValue().toString();
                        gender[0] = snapshot.child("gender").getValue().toString();
                        Log.d("name, gender", Arrays.toString(name) + ", " + Arrays.toString(gender));

                        String cardName = Arrays.toString(name).replace("[", "").replace("]", "").trim();
                        myViewHolder.mName.setText(""+ cardName);

                        String cardGender = Arrays.toString(gender).replace("[", "").replace("]", "").trim();
                        myViewHolder.mGender.setText("" + cardGender);

                        if (cardGender.equals("Female")){
                            myViewHolder.imgMale.setVisibility(View.GONE);
                            myViewHolder.imgFemale.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                myViewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSessionRef.child(session.getSessionID()).child("status").setValue("S" + mAuth);
                        mSessionRef.child(session.getSessionID()).child("status2").setValue("S" + studentID);
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                              DatabaseReference removeReq =  mRef.child(session.getUid());
                              removeReq.removeValue();
                                recyclerViewRequest.setVisibility(View.GONE);
                                recyclerViewRequest.setVisibility(View.VISIBLE);

                                String StudentID = session.getUid();
                                FirebaseDatabase.getInstance().getReference("Tutors").child(mAuth).child("Actives").child(StudentID).setValue(true);
                                FirebaseDatabase.getInstance().getReference("Users").child(StudentID).child("Sessions").child("Actives").child(mAuth).setValue(true);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                myViewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSessionRef.child(session.getSessionID()).child("status").setValue("D" +mAuth);
                        DatabaseReference refSession = FirebaseDatabase.getInstance().getReference().child("Sessions");
                        final Query qSession = refSession.orderByChild("status").equalTo("D" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                        qSession.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot SesSnapshot: snapshot.getChildren()) {
                                    SesSnapshot.getRef().removeValue();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Remove Request error", "onCancelled", error.toException());
                            }
                        });

                        mSessionRef.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                /*DatabaseReference removeSession =  mSessionRef.child(session.getSessionID());
                                removeSession.removeValue();*/
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DatabaseReference removeReq =  mRef.child(session.getUid());
                                removeReq.removeValue();
                                if (snapshot.getChildrenCount()== 0){
                                    cardviewNoHistory.setVisibility(View.VISIBLE);
                                    recyclerViewRequest.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });


            }
            @NonNull
            @Override
            public SessionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_confirm_request,parent, false);
                return new SessionRecyclerViewAdapter.MyViewHolder(v);

            }
        };
        adapter.startListening();
        /*recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));*/
        recyclerViewRequest.setAdapter(adapter);



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}