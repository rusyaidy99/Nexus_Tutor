package com.example.nexustutor.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nexustutor.R;
import com.example.nexustutor.User;
import com.example.nexustutor.update_profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    TextView mUsername, mName, mEmail, mGender, mGender2, mPhone, mType, CountPost, CountSession;
    ImageButton btn_update_profile;
    CircleImageView img_profile;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase mDatabase;
    String id;

    //profile pic

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View profileView = inflater.inflate(R.layout.fragment_profile,null);

        mAuth = FirebaseAuth.getInstance();

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidref = ref.child("Users").child(id);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        img_profile = profileView.findViewById(R.id.img_profile);


     /*   mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(mFirebaseAuth.getUid());
        user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();*/


        mUsername = profileView.findViewById(R.id.tv_username);
        mName = profileView.findViewById(R.id.tv_name);
        mEmail = profileView.findViewById(R.id.tv_email);
        mGender = profileView.findViewById(R.id.tv_gender1);
        mGender2 = profileView.findViewById(R.id.tv_gender2);
        mPhone = profileView.findViewById(R.id.tv_phone_number);
        mType = profileView.findViewById(R.id.tv_acc_type);
        btn_update_profile = profileView.findViewById(R.id.btn_update_profile);
        CountPost = profileView.findViewById(R.id.tv_count_post);
        CountSession = profileView.findViewById(R.id.tv_count_session);

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editprofile = new Intent(getActivity(), update_profile.class);
                startActivity(editprofile);
            }
        });

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
                mGender2.setText(user_gender);

                //Profile Type
                String user_acctype = snapshot.child("acctype").getValue(String.class);
                mType.setText(user_acctype);

                if (snapshot.child("phone").exists()){
                    String user_phone = snapshot.child("phone").getValue(String.class);
                    mPhone.setText(user_phone);
                }else{
                    mPhone.setText("Please update your phone number");
                }



                int countAct = (int) snapshot.child("Sessions").child("Actives").getChildrenCount();
                int countHis = (int) snapshot.child("Sessions").child("History").getChildrenCount();

                int totalcount = countAct +countHis;

                CountSession.setText("" + totalcount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidref.addListenerForSingleValueEvent(valueEventListener);

        DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
        final Query myquery = myref.child("Offers").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Log.d("query", myquery.toString());
        myquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = (int) snapshot.getChildrenCount();

                CountPost.setText("" + value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       getUserInfo();


        return profileView;
    }

   private void getUserInfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                  /*  String name = snapshot.child("fullname").getValue().toString();*/

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


}