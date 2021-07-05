package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.nexustutor.Fragment.ProfileTutorFragment;
import com.example.nexustutor.Fragment.SearchStudentFragment;
import com.example.nexustutor.Fragment.StudentListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivityTutor extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    Dialog dialogSubject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tutor);


        dialogSubject = new Dialog(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation_tutor);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("userid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tutor, new ProfileTutorFragment()).addToBackStack(null).commit();
        }else
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tutor, new SearchStudentFragment()).addToBackStack(null).commit();

    }

    //POPUP Menu profile
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu_tutor);
        popup.show();
    }


    /*public void ShowPopupSubject(View v){
        ImageButton imgBtnClose;
        Button btnAdd, btnCancel;
        final EditText etAddSubject;

        dialogSubject.setContentView(R.layout.popup_subject);
        imgBtnClose = (ImageButton) dialogSubject.findViewById(R.id.img_btn_close_subject);
        btnAdd = (Button) dialogSubject.findViewById(R.id.btn_add_subject);
        btnCancel = (Button) dialogSubject.findViewById(R.id.btn_cancel_subject);
        etAddSubject = (EditText) dialogSubject.findViewById(R.id.et_add_subject);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            String mysubject = etAddSubject.getText().toString().trim();
            @Override
            public void onClick(View view) {
                Subjek subject = new Subjek(mysubject);

                if (mysubject.equals("")){
                    Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), mysubject, Toast.LENGTH_LONG).show();
                }
                dialogSubject.dismiss();
                *//*FirebaseDatabase.getInstance().getReference("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Subjects").child(mysubject).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                            dialogSubject.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Unable to add, Please Try Again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });*//*
            }
        });
        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSubject.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSubject.dismiss();
            }
        });

        dialogSubject.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSubject.show();
    }*/

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case  R.id.nav_search_student:
                    selectedFragment = new SearchStudentFragment();
                    break;
                case R.id.nav_my_student:
                    selectedFragment = new StudentListFragment();
                    break;
                case R.id.nav_profile_tutor:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    selectedFragment = new ProfileTutorFragment();
                    break;

            }
            if (selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tutor,selectedFragment).commit();
            }
            return true;
        }
    };


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), login_main.class));
                finish();
                return true;
            case R.id.item_update:
                Intent profile = new Intent(getApplicationContext(), activity_update_profile_pic_tutor.class);
                startActivity(profile);
                return true;
        }
        return false;
    }
}