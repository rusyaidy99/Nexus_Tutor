package com.example.nexustutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class register_as extends AppCompatActivity {

    ImageButton btnStudent, btnTutor, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as);

        btnStudent = findViewById(R.id.img_btn_studentType);
        btnTutor = findViewById(R.id.img_btn_tutorType);
        btnBack = findViewById(R.id.img_btn_back);

        //nav to register
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent studentreg = new Intent(register_as.this, activity_register.class);
                startActivity(studentreg);
            }
        });

        btnTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tutorreg = new Intent(register_as.this, activity_register_tutor.class);
                startActivity(tutorreg);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(register_as.this, login_main.class);
                startActivity(back);
            }
        });
    }
}