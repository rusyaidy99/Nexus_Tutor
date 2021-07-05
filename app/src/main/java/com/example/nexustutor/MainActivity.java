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

import com.example.nexustutor.Fragment.AddOfferFragment;
import com.example.nexustutor.Fragment.ProfileFragment;
import com.example.nexustutor.Fragment.SearchTutorFragment;
import com.example.nexustutor.Fragment.TutorListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SearchTutorFragment()).addToBackStack(null).commit();

    }
    //POPUP Menu profile
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu);
        popup.show();
    }

    //Onclick Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_search:
                            selectedFragment = new SearchTutorFragment();

                            break;
                        case R.id.nav_add_offer:
                            selectedFragment = new AddOfferFragment();
                            break;
                        case R.id.nav_tutor:
                            selectedFragment = new TutorListFragment();
                            break;
                        case R.id.nav_profile:
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    if (selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }

                    return true;
                }
            };

    //Onclick Profile Menu
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.item1:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), login_main.class));
                finish();
                return true;
            case R.id.item_update:
                Intent profile = new Intent(getApplicationContext(), UpdateProfilePic.class);
                startActivity(profile);
                return true;

            default:
                return false;
        }
    }
}
