package com.example.nexustutor;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class update_profile extends AppCompatActivity {
    private ImageButton backBtn;
    private Button btnUpdate;
    private EditText etName, etEmail, etContact, etCity, etState;
    private Spinner spinnerGender;

    DatabaseReference mRefUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        btnUpdate = findViewById(R.id.btn_update_user);
        backBtn = findViewById(R.id.img_btn_back);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etContact = findViewById(R.id.et_phone);
        etCity = findViewById(R.id.et_city);
        etState = findViewById(R.id.et_state_edit);


        String mAuth = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRefUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth);
        mRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etName.setText(snapshot.child("fullname").getValue().toString());
                etEmail.setText(snapshot.child("email").getValue().toString());

                if(snapshot.hasChild("phone")){
                    etContact.setText(snapshot.child("phone").getValue().toString());
                }
                if(snapshot.hasChild("city")){
                    etCity.setText(snapshot.child("city").getValue().toString());
                }
                if(snapshot.hasChild("state")){
                    etState.setText(snapshot.child("state").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = etName.getText().toString();
                String newEmail = etEmail.getText().toString();
                String newContact= etContact.getText().toString();
                String newCity= etCity.getText().toString();
                String newState= etState.getText().toString();

                mRefUsers.child("fullname").setValue(newName);
                mRefUsers.child("email").setValue(newEmail);
                mRefUsers.child("phone").setValue(newContact);
                mRefUsers.child("city").setValue(newCity);
                mRefUsers.child("state").setValue(newState);
                Toast.makeText(update_profile.this, "Updated Successfuly", Toast.LENGTH_SHORT).show();
                finish();
            }
        });




    backBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });

    }
}