package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nexustutor.Adapter.EducationRecyclerViewAdapter;
import com.example.nexustutor.Adapter.SubjectRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class activity_view_tutor_profile extends AppCompatActivity {
    private TextView tvName, tvLocation, tvGender, tvAboutMe, tvCountSubject, tvCountStudent;
    private ImageView img, imgBtnBack;
    private Button btnHire,btnHireStatus, btnHired;
    private CardView noSub, noEdu;
    private List<Subjek> subjects;
    private List<Education> educations;
    RecyclerView recyclerViewSubject, recyclerViewEducation ;
    SubjectRecyclerViewAdapter adapter;
    EducationRecyclerViewAdapter adapter2;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tutor_profile);

        tvName = (TextView) findViewById(R.id.tv_tutor_name);
        tvLocation = (TextView) findViewById(R.id.tv_tutor_location);
        tvGender=  (TextView) findViewById(R.id.tv_tutor_gender);
        tvAboutMe = findViewById(R.id.tv_tutor_about_me);
        tvCountStudent = findViewById(R.id.tv_count_students);
        tvCountSubject = findViewById(R.id.tv_count_subject);

        img = (ImageView) findViewById(R.id.img_tutor_profile);
        imgBtnBack = (ImageView) findViewById(R.id.img_btn_back_viewprofile);

        btnHire = (Button) findViewById(R.id.btn_hire);
        btnHired = (Button) findViewById(R.id.btn_hired);
        btnHireStatus = (Button) findViewById(R.id.btn_hire_status);

        noEdu = findViewById(R.id.cardview_edu);
        noSub = findViewById(R.id.cardView_education);



        Intent intent = getIntent();
        String TutorName = intent.getExtras().getString("Name");
        String TutorGender = intent.getExtras().getString("Gender");
        String Category = intent.getExtras().getString("AccType");
        final String image = intent.getExtras().getString("image");

        final String myId = intent.getExtras().getString("uid");

       /* int image = intent.getExtras().getInt("Thumbnail");*/

        tvName.setText(TutorName);

        DatabaseReference refTutor = FirebaseDatabase.getInstance().getReference().child("Tutors").child(myId);
        refTutor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String numb = String.valueOf(snapshot.child("Actives").getChildrenCount());
                tvCountStudent.setText(numb);

                if (snapshot.child("description").exists()){
                    tvAboutMe.setText(snapshot.child("description").getValue().toString());
                }else{
                    tvAboutMe.setText("Owner doesn't update any description yet");
                }

                if (snapshot.child("image").exists()){
                    Picasso.get().load(image).into(img);
                }else {
                    img.setImageResource(R.drawable.nopicture);
                }

                if (snapshot.child("city").exists() && snapshot.child("city").exists()){
                    String lokasi = snapshot.child("city").getValue() + ", " + snapshot.child("state").getValue();
                    tvLocation.setText(lokasi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









       /* tvLocation.setText(myId);*/
        tvGender.setText(TutorGender);
        /*img.setImageResource(image);*/


        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViewSubject = findViewById(R.id.recyclerview_subject_view);
        recyclerViewSubject.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSubject.setLayoutManager(layoutManager);
        /*recyclerViewSubject.setItemAnimator(new DefaultItemAnimator());*/

        //recyclerview education
        recyclerViewEducation = findViewById(R.id.recyclerrview_education_view);
        recyclerViewEducation.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEducation.setLayoutManager(layoutManager2);


        DatabaseReference myrefSubject = FirebaseDatabase.getInstance().getReference();
        DatabaseReference subjectRef = myrefSubject.child("Tutors").child(myId).child("Subjects");



        final DatabaseReference refUid = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        subjects = new ArrayList<>();

        subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0){
                    noSub.setVisibility(View.GONE);
                }
                for (DataSnapshot ds:snapshot.getChildren()){
                    Subjek data = ds.getValue(Subjek.class);
                    subjects.add(data);
                }
                String countt = String.valueOf(snapshot.getChildrenCount());
                tvCountSubject.setText(countt);
                Log.d("subject", countt );


                /*Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                Log.d("timestamp", ts);*/

                adapter = new SubjectRecyclerViewAdapter(subjects);
                recyclerViewSubject.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       /* tvCountSubject.setText(subjects.size());*/



        DatabaseReference educationRef = myrefSubject.child("Tutors").child(myId).child("Educations");

        educations = new ArrayList<>();
        educationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0){
                    noEdu.setVisibility(View.GONE);
                }
                for (DataSnapshot ds:snapshot.getChildren()){
                    Education data2 = ds.getValue(Education.class);
                    educations.add(data2);
                }

                adapter2 = new EducationRecyclerViewAdapter(educations);
                recyclerViewEducation.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Sessions");
        final DatabaseReference mTutorRef = FirebaseDatabase.getInstance().getReference("Tutors").child(myId);



        btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String status = "R" + myId;
                final String sessionId = mDatabase.push().getKey();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                String format2 = simpleTimeFormat.format(new Date());
                Log.d("MainActivity", "Current Date: " + format );
                Log.d("MainActivity", "Current Time: " +  format2);

                final Session session = new Session(sessionId, FirebaseAuth.getInstance().getCurrentUser().getUid(), myId, status, format, format2);

                DatabaseReference refPhone = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                refPhone.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("phone").exists()){
                            FirebaseDatabase.getInstance().getReference().child("Sessions").child(sessionId).setValue(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(activity_view_tutor_profile.this, "Requesting", Toast.LENGTH_LONG).show();
                                        /*if (FirebaseDatabase.getInstance().getReference().child("Sessions").)*/
                                        FirebaseDatabase.getInstance().getReference("Tutors").child(myId).child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                                        ValueEventListener valueEventListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.child("Requests").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                    btnHire.setVisibility(View.GONE);
                                                    btnHireStatus.setVisibility(View.VISIBLE);
                                                }
                                                /*FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Offers").*/

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        };
                                        mTutorRef.addListenerForSingleValueEvent(valueEventListener);


                                    }
                                    else {
                                        Toast.makeText(activity_view_tutor_profile.this, "Can't hire right now", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(activity_view_tutor_profile.this, "Please Update Your Phone Number", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });
        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Requests").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    btnHire.setVisibility(View.GONE);
                    btnHireStatus.setVisibility(View.VISIBLE);
                }
                if (snapshot.child("Actives").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    btnHire.setVisibility(View.GONE);
                    btnHired.setVisibility(View.VISIBLE);
                }
                /*FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Offers").*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mTutorRef.addListenerForSingleValueEvent(valueEventListener2);
        btnHireStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup2 = new PopupMenu(getApplicationContext(), view);
                popup2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.cancel_request:

                                final DatabaseReference refRequest = FirebaseDatabase.getInstance().getReference().child("Tutors").child(myId).child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                refRequest.removeValue();
                                btnHireStatus.setVisibility(View.GONE);
                                btnHire.setVisibility(View.VISIBLE);

                                final DatabaseReference refSession = FirebaseDatabase.getInstance().getReference().child("Sessions");
                                final Query qSession = refSession.orderByChild("status").equalTo("R" + myId);
                                qSession.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (final DataSnapshot SesSnapshot: snapshot.getChildren()) {

                                            refSession.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                    String ses = String.valueOf(SesSnapshot.getRef());
                                                    String sid =  ses.replace("https://nexustutorfyp-default-rtdb.firebaseio.com/Sessions/", "");
                                                    if (snapshot2.child(sid).child("tid").getValue().equals(myId) && snapshot2.child(sid)
                                                            .child("uid").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                        Log.d("SesID", sid);
                                                        SesSnapshot.getRef().removeValue();
                                                    }else{
                                                        Log.d("SesID", "gg");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                          /*  if (SesSnapshot.getRef().child("tid").equals(myId) && SesSnapshot.getRef().child("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ){
                                                SesSnapshot.getRef().removeValue();
                                            }*/

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("Remove Request error", "onCancelled", error.toException());
                                    }
                                });

                                return true;
                        }
                        return false;
                    }
                });
                popup2.inflate(R.menu.request_menu);
                popup2.show();


            }
        });
    }

    public void logout (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), login_main.class));
        finish();
    }



}
