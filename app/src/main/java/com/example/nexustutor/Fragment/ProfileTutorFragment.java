package com.example.nexustutor.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nexustutor.Adapter.EducationRecyclerViewAdapter;
import com.example.nexustutor.Adapter.OfferRecyclerViewAdapter;
import com.example.nexustutor.Adapter.RecyclerViewAdapter;
import com.example.nexustutor.Adapter.SubjectRecyclerViewAdapter;
import com.example.nexustutor.Education;
import com.example.nexustutor.Model.MyCustomDialogEducation;
import com.example.nexustutor.Model.MyCustomDialogSubject;
import com.example.nexustutor.Offer;
import com.example.nexustutor.R;

import com.example.nexustutor.Subjek;
import com.example.nexustutor.activity_update_profile_tutor;
import com.example.nexustutor.update_profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileTutorFragment extends Fragment implements MyCustomDialogSubject.OnSubjectSelected, MyCustomDialogEducation.OnEducationSelected {
    private static final String TAG = "ProfileTutorFragment";
    public TextView mUsername, mName, mEmail, mGender, mPhone, mType, mLocation, mDescription, mCountStudent, mCountSubject;
    public CardView cardSub, cardEdu;
    ImageButton btn_update_profile, btn_update_description;
    CircleImageView img_profile;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase mDatabase;
    String id;
    Button btnOpenSubject, btnOpenEducation;



    //Recyclerview
    RecyclerView recyclerViewSubject, recyclerViewEducation ;
    SubjectRecyclerViewAdapter adapter;
    EducationRecyclerViewAdapter adapter2;
    private FirebaseRecyclerOptions<Subjek> optionsSubject;
    private FirebaseRecyclerAdapter<Subjek, SubjectRecyclerViewAdapter.MyViewHolder> adapterSubject;
    private List<Subjek> subjects;
    private List<Education> educations;
    //profile pic

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileTutorView = inflater.inflate(R.layout.fragment_profile_tutor,null);

        mAuth = FirebaseAuth.getInstance();

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidref = ref.child("Tutors").child(id);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tutors");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        img_profile = profileTutorView.findViewById(R.id.img_profile_tutor);



        mUsername = profileTutorView.findViewById(R.id.tv_username_tutor);
        mName = profileTutorView.findViewById(R.id.tv_name);
        mEmail = profileTutorView.findViewById(R.id.tv_email);
        mGender = profileTutorView.findViewById(R.id.tv_gender2);
        mLocation = profileTutorView.findViewById(R.id.tv_gender1);
        mDescription = profileTutorView.findViewById(R.id.tv_description);
        mPhone = profileTutorView.findViewById(R.id.tv_phone_number);
        mType = profileTutorView.findViewById(R.id.tv_acc_type);
        btnOpenSubject = profileTutorView.findViewById(R.id.btn_open_subject);
        btnOpenEducation= profileTutorView.findViewById(R.id.btn_open_education);
        btn_update_profile = profileTutorView.findViewById(R.id.btn_update_profile);
        btn_update_description = profileTutorView.findViewById(R.id.btn_update_description);


        cardEdu = profileTutorView.findViewById(R.id.cardView_edu);
        cardSub= profileTutorView.findViewById(R.id.cardView_sub);
        mCountStudent = profileTutorView.findViewById(R.id.tv_count_student);
        mCountSubject = profileTutorView.findViewById(R.id.tv_count_subject);

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editprofile = new Intent(getActivity(), activity_update_profile_tutor.class);
                startActivity(editprofile);
            }
        });

        btn_update_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editprofile = new Intent(getActivity(), activity_update_profile_tutor.class);
                startActivity(editprofile);
            }
        });

       /* subjects = new ArrayList<>();*/
        /*subjects.add(new Subject("Mathematics"));
        subjects.add(new Subject("Science"));
        subjects.add(new Subject("Bahasa"));
        subjects.add(new Subject("Mathematics"));
        subjects.add(new Subject("Science"));
        subjects.add(new Subject("Bahasa"));*/

        //set firebase recyclerview subject
        recyclerViewSubject = profileTutorView.findViewById(R.id.recylerview_subject);
        recyclerViewSubject.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSubject.setLayoutManager(layoutManager);
        /*recyclerViewSubject.setItemAnimator(new DefaultItemAnimator());*/

        //recyclerview education
        recyclerViewEducation = profileTutorView.findViewById(R.id.recyclerview_education);
        recyclerViewEducation.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEducation.setLayoutManager(layoutManager2);


        DatabaseReference myrefSubject = FirebaseDatabase.getInstance().getReference();
        final Query myquery = myrefSubject.child("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Subjects").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = (int) snapshot.getChildrenCount();

                Log.d(TAG, String.valueOf(value));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference studentRef = myrefSubject.child("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Actives");

        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCountStudent.setText(""+ snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference subjectRef = myrefSubject.child("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Subjects");
        subjects = new ArrayList<Subjek>();
        subjectRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    getAllSubject(snapshot);
                    if (snapshot.getChildrenCount()>0){
                        cardSub.setVisibility(View.GONE);
                    }
                }if (subjects.size()==0){
                    cardSub.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    getAllSubject(snapshot);

                    if (snapshot.getChildrenCount()>0){
                        cardSub.setVisibility(View.GONE);
                    }
                }
                if (subjects.size()==0){
                    cardSub.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    subjectDeletion(snapshot);
                    if (snapshot.getChildrenCount()>0){
                        cardSub.setVisibility(View.GONE);
                    }
                }
                if (subjects.size()==0){
                    cardSub.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjects = new ArrayList<>();
                mCountSubject.setText("" + snapshot.getChildrenCount());
                for (DataSnapshot ds:snapshot.getChildren()){
                    Subjek data = ds.getValue(Subjek.class);
                    subjects.add(data);
                }
                adapter = new SubjectRecyclerViewAdapter(getContext(),subjects);
                adapter.notifyDataSetChanged();
                recyclerViewSubject.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        DatabaseReference educationRef = myrefSubject.child("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Educations");

        educations = new ArrayList<>();
        educationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               if (snapshot.exists()){
                   getAllEducation(snapshot);
                   if (educations.size()>0){
                       cardEdu.setVisibility(View.GONE);
                   }
               }
                if (educations.size()==0){
                    cardEdu.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    getAllEducation(snapshot);
                    if (educations.size()>0){
                        cardEdu.setVisibility(View.GONE);
                    }
                }
                if (educations.size()==0){
                    cardEdu.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    EducationDeletion(snapshot);
                    if (educations.size()>0){
                        cardEdu.setVisibility(View.GONE);
                    }
                }
                if (educations.size()==0){
                    cardEdu.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*educationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Education data2 = ds.getValue(Education.class);
                    educations.add(data2);
                }
                adapter2 = new EducationRecyclerViewAdapter(getContext(), educations);
                recyclerViewEducation.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        Log.d(TAG, myquery.toString());


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Profile Name
                String user_name = snapshot.child("fullname").getValue(String.class);
                mUsername.setText(user_name);
                mName.setText(user_name);


                //Profile Email
                String user_email = snapshot.child("email").getValue(String.class);
                mEmail.setText(user_email);

                //Profile Gender
                String user_gender = snapshot.child("gender").getValue(String.class);
                mGender.setText(user_gender);

                //Profile Phone
                String user_phone = snapshot.child("phone").getValue(String.class);
                mPhone.setText(user_phone);

                //Profile Location
                String user_location = snapshot.child("city").getValue(String.class) +", " + snapshot.child("state").getValue(String.class);
                mLocation.setText(user_location);

                //Profile Description
                if ( snapshot.child("description").exists()){
                    String user_description = snapshot.child("description").getValue(String.class);
                    mDescription.setText(user_description);
                }else if(snapshot.child("description").equals("") || !snapshot.child("description").exists()){
                    mDescription.setText("Update Your description to gain your visitors' trust to hire you");
                }


                //Profile Type
                String user_acctype = snapshot.child("acctype").getValue(String.class);
                mType.setText(user_acctype);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidref.addListenerForSingleValueEvent(valueEventListener);

        btnOpenSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: opening dialog");
                MyCustomDialogSubject dialogSubject = new MyCustomDialogSubject();
                dialogSubject.setTargetFragment(ProfileTutorFragment.this,1);
                dialogSubject.show(getParentFragmentManager(),"MyCustomDialogSubject");
            }
        });

        btnOpenEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: opening dialog");
                MyCustomDialogEducation dialogEducation = new MyCustomDialogEducation();
                dialogEducation.setTargetFragment(ProfileTutorFragment.this,1);
                dialogEducation.show(getParentFragmentManager(),"MyCustomDialogEducation");
            }
        });

        //profilePic
        getUserInfo();



        // Inflate the layout for this fragment
        return profileTutorView;
    }

    private void ifContentEmpty(DataSnapshot snapshot) {
        if (!snapshot.exists()){
            cardEdu.setVisibility(View.VISIBLE);
        }else if (snapshot.getChildrenCount()>0){
            cardEdu.setVisibility(View.GONE);
        }
    }

    private void EducationDeletion(DataSnapshot snapshot) {
        for (DataSnapshot singlesnapshot : snapshot.getChildren()){
            String taskTitle = singlesnapshot.getValue(String.class);
            for (int i =0; i< educations.size(); i++){
                if (educations.get(i).getEducation().equals(taskTitle)){
                    educations.remove(i);
                }
            }
            Log.d(TAG, "Task Title" + taskTitle);
            adapter2.notifyDataSetChanged();
            adapter2 = new EducationRecyclerViewAdapter(getContext(), educations);
            recyclerViewEducation.setAdapter(adapter2);
        }


    }

    private void getAllEducation(DataSnapshot snapshot) {
        for (DataSnapshot singlesnapshot : snapshot.getChildren()){
            String TaskTitle = singlesnapshot.getValue(String.class);
            educations.add(new Education(TaskTitle));
            adapter2 = new EducationRecyclerViewAdapter(getContext(), educations);
            recyclerViewEducation.setAdapter(adapter2);
        }
    }

    private void subjectDeletion(DataSnapshot snapshot) {
        for (DataSnapshot singlesnapshot : snapshot.getChildren()){
            String taskTitle = singlesnapshot.getValue(String.class);
            for (int i =0; i< subjects.size(); i++){
                if (subjects.get(i).getMySubject().equals(taskTitle)){
                    subjects.remove(i);
                }
            }
            Log.d(TAG, "Task Title" + taskTitle);
            adapter.notifyDataSetChanged();
            adapter = new SubjectRecyclerViewAdapter(getContext(), subjects);
            recyclerViewSubject.setAdapter(adapter);
        }
    }

    private void getAllSubject(DataSnapshot snapshot) {
        for (DataSnapshot singlesnapshot : snapshot.getChildren()){
            String TaskTitle = singlesnapshot.getValue(String.class);
            subjects.add(new Subjek(TaskTitle));
            adapter = new SubjectRecyclerViewAdapter(getContext(), subjects);
            recyclerViewSubject.setAdapter(adapter);
        }
    }


    //profilepic
    private void getUserInfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                      String name = snapshot.child("fullname").getValue().toString();

                    if (snapshot.hasChild("image")) {
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(img_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void sendInput(String input) {
        /*mPhone.setText(input);*/
        Subjek subjek = new Subjek(input);
        FirebaseDatabase.getInstance().getReference("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Subjects").child(input).setValue(subjek).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getContext(), "Unable to add, Please Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void sendInputEdu(String input){
       /* mPhone.setText(input);*/
        Education education = new Education(input);
        FirebaseDatabase.getInstance().getReference("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Educations").child(input).setValue(education).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getContext(), "Unable to add, Please Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}