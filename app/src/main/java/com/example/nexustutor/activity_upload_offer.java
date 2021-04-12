package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_upload_offer extends AppCompatActivity {
    EditText etTitle, etDescription, etSubject, etLocationCity, etLocationState;
    Button btnUpload, btnCancel;
    RadioGroup radioGender;
    RadioButton radioBtn;
    ImageButton btnBack;


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference refUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_offer);

        refUid = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        etTitle = findViewById(R.id.et_title);
        etDescription= findViewById(R.id.et_description);
        etSubject= findViewById(R.id.et_subject);
        etLocationCity= findViewById(R.id.et_city);
        etLocationState = findViewById(R.id.et_state);

        //radio value
        radioGender = findViewById(R.id.radio_group_gender);



        btnUpload = findViewById(R.id.btn_upload);
        btnCancel =  findViewById(R.id.btn_cancel);

        btnBack = findViewById(R.id.img_btn_back);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Offers");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = mAuth.getCurrentUser().getUid();
                Toast.makeText(activity_upload_offer.this,uid, Toast.LENGTH_LONG).show();
               /* finish();*/
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGender.getCheckedRadioButtonId();
                radioBtn = findViewById(selectedId);
                final String postId = mDatabase.push().getKey();
                final String uid = mAuth.getCurrentUser().getUid();
                String title = etTitle.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String city = etLocationCity.getText().toString().trim();
                String state = etLocationState.getText().toString().trim();
                String subject = etSubject.getText().toString().trim();
                String gender = radioBtn.getText().toString();

                final UserOffer userOffer = new UserOffer(postId);
                Offer offer = new Offer(uid, title, description, city, state, subject, gender);
                FirebaseDatabase.getInstance().getReference("Offers").child(postId).setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int postvalue = (int) snapshot.child("Offers").getChildrenCount() + 1;
                                            /*FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Offers").*/
                                    FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Offers").child(String.valueOf(postvalue)).setValue(postId);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            };
                            refUid.addListenerForSingleValueEvent(valueEventListener);
                            Toast.makeText(activity_upload_offer.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(activity_upload_offer.this, "Upload Failed! Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                });



            }
        });


    }
}