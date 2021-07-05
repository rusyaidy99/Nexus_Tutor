package com.example.nexustutor.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nexustutor.Adapter.ActiveStudentRecyclerAdapter;
import com.example.nexustutor.Adapter.HistoryStudentRecyclerAdapter;
import com.example.nexustutor.MainActivity;
import com.example.nexustutor.R;
import com.example.nexustutor.Session;
import com.example.nexustutor.activity_pay_tutor;
import com.example.nexustutor.activity_session_details_tutor;
import com.example.nexustutor.activity_view_tutor_profile;
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

import static android.Manifest.permission.CALL_PHONE;
import static com.example.nexustutor.R.layout.fragment_student_list;
import static com.example.nexustutor.R.layout.fragment_tutor_list;


public class TutorListFragment extends Fragment {
    private Button btnFindTutor;
    CardView cardViewNoActive, cardViewNoHistory;

    private FirebaseRecyclerOptions<Session> options;
    private FirebaseRecyclerAdapter<Session, ActiveStudentRecyclerAdapter.MyViewHolder> adapter;
    private FirebaseRecyclerOptions<Session>  options2;
    private FirebaseRecyclerAdapter<Session, HistoryStudentRecyclerAdapter.MyViewHolder> adapter2;
    private RecyclerView recyclerViewActive, recyclerViewHistory;

    private DatabaseReference refSession, refUser,refHistory;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tutorListView = inflater.inflate(fragment_tutor_list, null);

        recyclerViewHistory = tutorListView.findViewById(R.id.recyclerview_session_student_history);
        recyclerViewActive = tutorListView.findViewById(R.id.recycler_active_student_session);
        cardViewNoActive = tutorListView.findViewById(R.id.cardView_no_active_session);
        cardViewNoHistory = tutorListView.findViewById(R.id.cardView_no_history);

        btnFindTutor = tutorListView.findViewById(R.id.btn_find_tutor);

        refSession = FirebaseDatabase.getInstance().getReference().child("Sessions");
        refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sessions").child("Actives");
        refHistory = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sessions").child("History");


        btnFindTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList history = new ArrayList<>();
        refHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    cardViewNoHistory.setVisibility(View.GONE);
                    recyclerViewHistory.setVisibility(View.VISIBLE);
                }
                for (DataSnapshot ds:snapshot.getChildren()){
                    String data = ds.getKey();
                    history.add(data);
                }
                Log.d("history", history.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //recyclerview History
        recyclerViewHistory.setHasFixedSize(true);
        LinearLayoutManager linearLayout2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerViewHistory.setLayoutManager(linearLayout2);

        Query myquery2  = refSession.orderByChild("status2").equalTo("H" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        myquery2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countreq = (int) snapshot.getChildrenCount();
                Log.d("count history", String.valueOf(countreq));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        options2 = new FirebaseRecyclerOptions.Builder<Session>().setQuery(myquery2, Session.class).build();
        adapter2 = new FirebaseRecyclerAdapter<Session, HistoryStudentRecyclerAdapter.MyViewHolder>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull final HistoryStudentRecyclerAdapter.MyViewHolder myViewHolder, int i, @NonNull final Session session) {
                final String studentID = session.getUid();
                final String tutorID = session.getTid();
                String status = session.getStatus();

                final String[] name = new String[1];
                final String[] gender = new String[1];


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
                            myViewHolder.mActiveSubject.setText("No Subject Added");
                        else
                            myViewHolder.mActiveSubject.setText(subjectlist);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                myViewHolder.mActiveDate.setText("" + session.getDate());

                DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Tutors").child(tutorID);
                studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name[0] = snapshot.child("fullname").getValue().toString();
                        String cardName = Arrays.toString(name).replace("[", "").replace("]", "").trim();
                        Log.d("name", Arrays.toString(name));
                        /*String studentName =  snapshot.child("fullname").getValue().toString();*/
                        myViewHolder.mActiveName.setText("" + cardName );
                        final String phone = snapshot.child("phone").getValue().toString();
                        /*Picasso.get().load(snapshot.child("image").getValue().toString()).into(myViewHolder.mActiveImg);*/

                        myViewHolder.mActiveContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+phone));
                                if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivity(intent);
                                } else {
                                    requestPermissions(new String[]{CALL_PHONE}, 1);
                                }
                            }
                        });
                        if (snapshot.hasChild("image")){
                            String image = snapshot.child("image").getValue().toString();
                            Picasso.get().load(image).into(myViewHolder.mActiveImg);
                        }
                        myViewHolder.cardviewActive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentSession = new Intent(getActivity(), activity_pay_tutor.class);
                                intentSession.putExtra("UID", session.getUid());
                                intentSession.putExtra("TID", session.getTid());
                                intentSession.putExtra("Time", session.getTime());
                                intentSession.putExtra("Date", session.getDate());
                                intentSession.putExtra("SessionID", session.getSessionID());

                                /*intent.putExtra("Email", mData.get(position).getEmail());*/
                                startActivity(intentSession);
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
            public HistoryStudentRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_active_student,parent, false);
                return new HistoryStudentRecyclerAdapter.MyViewHolder(v);

            }
        };
        adapter2.startListening();
        recyclerViewHistory.setAdapter(adapter2);



        final ArrayList actives = new ArrayList<>();
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("count", String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.getChildrenCount()>0){

                    cardViewNoActive.setVisibility(View.GONE);
                    recyclerViewActive.setVisibility(View.VISIBLE);
                }
                for (DataSnapshot ds:snapshot.getChildren()){
                    String data = ds.getKey();
                    actives.add(data);
                }
                Log.d("actives", actives.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerViewActive = tutorListView.findViewById(R.id.recycler_active_student_session);
        recyclerViewActive.setHasFixedSize(true);
        /*adapter = new OfferRecyclerViewAdapter(getActivity(), offerList);*/
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewActive.setLayoutManager(linearLayout);


        Query myquery  = refSession.orderByChild("status2").equalTo("S" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        myquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countreq = (int) snapshot.getChildrenCount();
                Log.d("count actives", String.valueOf(countreq));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        options = new FirebaseRecyclerOptions.Builder<Session>().setQuery(myquery, Session.class).build();
        adapter = new FirebaseRecyclerAdapter<Session, ActiveStudentRecyclerAdapter.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ActiveStudentRecyclerAdapter.MyViewHolder myViewHolder, int i, @NonNull final Session session) {
                refSession.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(session.getSessionID()).hasChild("paymentUri")){
                            myViewHolder.mActivePay.setVisibility(View.VISIBLE);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
                            myViewHolder.mActiveSubject.setText("No Subject Added");
                        else
                            myViewHolder.mActiveSubject.setText(subjectlist);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final String tutorID = session.getTid();
                String status = session.getStatus();

                final String[] name = new String[1];
                final String[] gender = new String[1];


                myViewHolder.mActiveDate.setText("" + session.getDate());

                DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Tutors").child(tutorID);
                studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name[0] = snapshot.child("fullname").getValue().toString();
                        String cardName = Arrays.toString(name).replace("[", "").replace("]", "").trim();
                        Log.d("name", Arrays.toString(name));
                        /*String studentName =  snapshot.child("fullname").getValue().toString();*/
                        myViewHolder.mActiveName.setText("" + cardName );
                        if (snapshot.hasChild("phone")) {
                           final String phone = snapshot.child("phone").getValue().toString();
                            myViewHolder.mActiveContact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+phone));
                                    if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        startActivity(intent);
                                    } else {
                                        requestPermissions(new String[]{CALL_PHONE}, 1);
                                    }
                                }
                            });
                        }
                        /*Picasso.get().load(snapshot.child("image").getValue().toString()).into(myViewHolder.mActiveImg);*/
                        if (snapshot.hasChild("image")){
                            String image = snapshot.child("image").getValue().toString();
                            Picasso.get().load(image).into(myViewHolder.mActiveImg);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                myViewHolder.cardviewActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), activity_pay_tutor.class);
                        intent.putExtra("UID", session.getUid());
                        intent.putExtra("TID", session.getTid());
                        intent.putExtra("Time", session.getTime());
                        intent.putExtra("Date", session.getDate());
                        intent.putExtra("SessionID", session.getSessionID());

                        /*intent.putExtra("Email", mData.get(position).getEmail());*/
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public ActiveStudentRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_active_student,parent, false);
                return new ActiveStudentRecyclerAdapter.MyViewHolder(v);

            }
        };
        adapter.startListening();
        recyclerViewActive.setAdapter(adapter);


        return tutorListView;
    }
}