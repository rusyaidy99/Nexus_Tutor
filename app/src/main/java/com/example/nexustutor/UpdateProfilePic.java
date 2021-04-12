package com.example.nexustutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.nexustutor.Fragment.ProfileFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfilePic extends AppCompatActivity {

    private CircleImageView profileImg;
    private ImageButton backButton;
    private Button saveButton, closeButton, updatePicButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_pic);
        //init

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        profileImg = findViewById(R.id.circle_img_profile);

        closeButton = findViewById(R.id.btn_close);
        saveButton = findViewById(R.id.btn_save);

        updatePicButton = findViewById(R.id.btn_change_pic);
        backButton = findViewById(R.id.img_btn_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
            }
        });
            updatePicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CropImage.activity().setAspectRatio(1,1).start(UpdateProfilePic.this);
                }
            });

            getUserinfo();



    }

    private void getUserinfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0){
                    if (snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImg.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set Your Profile");
        progressDialog.setMessage("Please wait, while we are setting your Data");
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = storageProfilePicsRef.child(mAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = (Uri) task.getResult();
                        myUri = downloadUrl.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);

                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        progressDialog.dismiss();

                        finish();
                    }
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }
}