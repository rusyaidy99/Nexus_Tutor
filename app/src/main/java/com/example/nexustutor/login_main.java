package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_main extends AppCompatActivity implements View.OnClickListener{
    //Declare variables
    Button btnLogin;
    EditText etEmail, etPassword;
    TextView txtForget, btnRegister;
    ProgressBar progressBar;
    String id;

    FirebaseAuth mAuth;
    DatabaseReference mRefTutor,mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.lbl_btn_register);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        txtForget = findViewById(R.id.lbl_btn_forgot_pass);
        progressBar = findViewById(R.id.progressBar2);
        mRef = FirebaseDatabase.getInstance().getReference();
        mRefTutor = FirebaseDatabase.getInstance().getReference().child("Tutors");

      /*  id = FirebaseAuth.getInstance().getCurrentUser().getUid();*/



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent register = new Intent(login_main.this, register_as.class);
                startActivity(register);
            }
        });

        /*btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });*/

        txtForget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent forget = new Intent(login_main.this, activity_reset_password.class);
                startActivity(forget);
            }
        });

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(email.isEmpty()){
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    etPassword.setError("Password is required");
                    etPassword.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            id = FirebaseAuth.getInstance().getCurrentUser().getUid();


                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    /*String type = snapshot.child("Tutors").child(id).child("acctype").getValue().toString();
                                    *//*String type2 = snapshot.child("Users").child(id).child("acctype").getValue().toString();*//*
                                    Toast.makeText(login_main.this, type, Toast.LENGTH_LONG).show();*/

                                    if (snapshot.child("Tutors").child(id).exists()){
                                        String type = snapshot.child("Tutors").child(id).child("acctype").getValue().toString();
                                        Intent intent = new Intent(login_main.this,MainActivityTutor.class);
                                        startActivity(intent);
                                        finish();
                                        /*Toast.makeText(login_main.this, type, Toast.LENGTH_LONG).show();*/

                                    }

                                    if (snapshot.child("Users").child(id).exists()){
                                        String type = snapshot.child("Users").child(id).child("acctype").getValue().toString();
                                        Intent intent = new Intent(login_main.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                       /* Toast.makeText(login_main.this, type, Toast.LENGTH_LONG).show();*/
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            progressBar.setVisibility(View.GONE);


                            Toast.makeText(login_main.this, "Successfully Logged In", Toast.LENGTH_LONG).show();

                            /*Intent intent = new Intent(login_main.this,MainActivity.class);
                            startActivity(intent);
                            finish();*/
                        }else{
                            Toast.makeText(login_main.this, "Login failed", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

     }


    @Override
    public void onClick(View view) {


        }


    }


    /*private void userLogin(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etPassword.setError("Password is Required");
            etPassword.requestFocus();
            return;
        }
    }*/

