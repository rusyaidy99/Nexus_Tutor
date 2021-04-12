package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.nexustutor.Fragment.ProfileTutorFragment;
import com.example.nexustutor.Fragment.SearchStudentFragment;
import com.example.nexustutor.Fragment.StudentListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityTutor extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tutor);

        bottomNavigationView = findViewById(R.id.bottom_navigation_tutor);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tutor, new SearchStudentFragment()).addToBackStack(null).commit();

    }

    //POPUP Menu profile
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu_tutor);
        popup.show();
    }


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
                startActivity(new Intent(getApplicationContext(), login_main.class));
                finish();
                return true;
            case R.id.item_update:
               /* Intent profile = new Intent(getApplicationContext(), UpdateProfilePic.class);
                startActivity(profile);*/
                return true;
        }
        return false;
    }
}