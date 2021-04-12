package com.example.nexustutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class activity_view_tutor_profile extends AppCompatActivity {
    private TextView tvName, tvLocation, tvCategory;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tutor_profile);

        tvName = (TextView) findViewById(R.id.tv_tutor_name);
        tvCategory = (TextView) findViewById(R.id.tv_tutor_category);
        tvLocation=  (TextView) findViewById(R.id.tv_tutor_gender);
        img = (ImageView) findViewById(R.id.img_tutor_profile);

        Intent intent = getIntent();
        String TutorName = intent.getExtras().getString("Name");
        String TutorLocation = intent.getExtras().getString("Gender");
        String Category = intent.getExtras().getString("AccType");
        String image = intent.getExtras().getString("image");

       /* int image = intent.getExtras().getInt("Thumbnail");*/

        tvName.setText(TutorName);
        tvLocation.setText(TutorLocation);
        tvCategory.setText(Category);
        Picasso.get().load(image).into(img);
        /*img.setImageResource(image);*/
    }

    public void logout (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), login_main.class));
        finish();
    }
}
