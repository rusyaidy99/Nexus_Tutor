package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageButton btnBack;
    private Spinner spinnerGender,spinnerType;
    private EditText etname, etEmail, etPassword, etConfirmPass;
    private ProgressBar progressBar;
    private Button btnRegister;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Field Form
        etname = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPass = findViewById(R.id.et_confirm_pass);

        btnBack = findViewById(R.id.img_btn_back);
        btnRegister = findViewById(R.id.btn_register);

        //ProgressBar
        progressBar = findViewById(R.id.progressBar);

        //Dropdown Gender & AccType form
        spinnerGender = findViewById(R.id.spinner_gender);
        /*spinnerType = findViewById(R.id.spinner_acc_type);*/

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Spinner Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders,R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(this);

        /*ArrayAdapter<CharSequence> adapterAcc = ArrayAdapter.createFromResource(this, R.array.acctype,android.R.layout.simple_spinner_item);
        adapterAcc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterAcc);
        spinnerType.setOnItemSelectedListener(this);*/

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent previous = new Intent(activity_register.this, login_main.class);
                startActivity(previous);
            }
        });

        /*if(mAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
*/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etname.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmpass = etConfirmPass.getText().toString().trim();
                final String gender = spinnerGender.getSelectedItem().toString();
                final String type = "Student/Parent";

                //form validation
                if(name.isEmpty()){
                    etname.setError("Full Name is required");
                    etname.requestFocus();
                    return;
                }

                if(email.isEmpty()){
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Please provide valid email");
                    etEmail.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    etPassword.setError("Password is required");
                    etPassword.requestFocus();
                    return;
                }

       /* if(password.length() < 8 ){
            etPassword.setError("Password should be less than 8 characters!");
            etPassword.requestFocus();
            return;
        }*/

                if(confirmpass.isEmpty()){
                    etConfirmPass.setError("Please confirm your password");
                    etConfirmPass.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //register user in Firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            /*Toast.makeText(activity_register.this, "User has been registered", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), activity_view_tutor_profile.class));
                            */

                            User user = new User(name, email, gender, type);
                            String nameLowercase = name.toLowerCase();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fullnameLowercase")
                                    .setValue(nameLowercase);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(activity_register.this, "User has been registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), login_main.class));

                                        //redirect to login
                                    }else {
                                        Toast.makeText(activity_register.this, "Registration Failed! Try Again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(activity_register.this, "Registration Failed! Try Again!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String text = parent.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
