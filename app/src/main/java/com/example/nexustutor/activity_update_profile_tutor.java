package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_update_profile_tutor extends AppCompatActivity {
    private TextView name, email, phone, city, state, description;
    private Button btnUpdate;
    private ImageButton imgBtnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_tutor);

        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        phone = findViewById(R.id.et_phone);
        city = findViewById(R.id.et_city_edit);
        state = findViewById(R.id.et_state_edit);
        description = findViewById(R.id.et_description);

        btnUpdate = findViewById(R.id.btn_update);
        imgBtnBack = findViewById(R.id.img_btn_back);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mName = snapshot.child("fullname").getValue().toString();
                String mEmail = snapshot.child("email").getValue().toString();

                if (snapshot.child("phone").exists()) {
                    String mPhone = snapshot.child("phone").getValue().toString();
                    phone.setText(mPhone);
                }
                if (snapshot.child("city").exists()) {
                    String mCity = snapshot.child("city").getValue().toString();
                    city.setText(mCity);
                }
                if (snapshot.child("state").exists()) {
                    String mState = snapshot.child("state").getValue().toString();
                    state.setText(mState);
                }
                if (snapshot.child("description").exists()) {
                    String mDescription = snapshot.child("description").getValue().toString();
                    description.setText(mDescription);
                }

                name.setText(mName);
                email.setText(mEmail);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nName = name.getText().toString().trim();
                String nEmail= email.getText().toString().trim();
                String nPhone= phone.getText().toString().trim();
                String nCity= city.getText().toString().trim();
                String nState= state.getText().toString().trim();
                String nDescription= description.getText().toString().trim();

                reference.child("fullname").setValue(nName);
                reference.child("email").setValue(nEmail);
                reference.child("phone").setValue(nPhone);
                reference.child("city").setValue(nCity);
                reference.child("state").setValue(nState);
                reference.child("description").setValue(nDescription);

                Toast.makeText(activity_update_profile_tutor.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}