package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;

public class activity_pay_tutor extends AppCompatActivity {
    private TextView tvName, tvSubject, tvDate, tvGender, lblPage;
    private TextView tvDateRemaining, tvPackage, tvPrice, tvFile;
    private Button btnContact, btnPayNow, btnCancelPay, btnUploadPay;
    private ImageButton imgBtnReport, imgBtnBack, imgBtnDownload;
    private CircleImageView circleImageViewTutor;


    private Uri fileUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storagePayPicsRef;
    ProgressDialog progressDialog;

    private DatabaseReference sessionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_tutor);

        lblPage = findViewById(R.id.lbl_pay_tutor);

        tvName = findViewById(R.id.tv_pay_name);
        tvSubject = findViewById(R.id.tv_pay_subject);
        tvDate = findViewById(R.id.tv_pay_session_date);
        tvGender = findViewById(R.id.tv_pay_gender);
        tvDateRemaining = findViewById(R.id.tv_pay_session_countdown);
        tvPackage = findViewById(R.id.tv_pay_session_package);
        tvPrice = findViewById(R.id.tv_pay_price);
        tvFile = findViewById(R.id.tv_imgFile);

        btnContact = findViewById(R.id.btn_pay_contact);
        btnPayNow = findViewById(R.id.btn_submit_pay);
        btnCancelPay = findViewById(R.id.btn_cancel_pay);
        btnUploadPay = findViewById(R.id.btn_upload_pay);

        imgBtnReport = findViewById(R.id.img_btn_pay_report);
        imgBtnBack = findViewById(R.id.img_btn_back);
        imgBtnDownload = findViewById(R.id.img_btn_download_file);

        circleImageViewTutor = findViewById(R.id.circleImg_profile_pay);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCancelPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        String UserId = intent.getExtras().getString("UID");
        final String TutorId = intent.getExtras().getString("TID");
        String SessionTime = intent.getExtras().getString("Time");
        String SessionDate = intent.getExtras().getString("Date");
        final String SessionId = intent.getExtras().getString("SessionID");

        Log.d("sid", SessionId);



        DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference().child("Tutors");
        sessionRef = FirebaseDatabase.getInstance().getReference().child("Sessions");




        tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if (snapshot.hasChild("image")){
                    String image = snapshot.child(TutorId).child("image").getValue().toString();
                    Picasso.get().load(image).into(circleImageViewTutor);
                }
                String Tname = snapshot.child(TutorId).child("fullname").getValue().toString();
                tvName.setText(Tname);
                String Tgender = snapshot.child(TutorId).child("gender").getValue().toString();
                tvGender.setText(Tgender);

                btnContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Tphone = snapshot.child(TutorId).child("phone").getValue().toString();
                        Intent intent2 = new Intent(Intent.ACTION_DIAL);
                        intent2.setData(Uri.parse("tel:"+Tphone));
                        if (ContextCompat.checkSelfPermission(activity_pay_tutor.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            startActivity(intent2);
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{CALL_PHONE}, 1);
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvDate.setText(SessionDate);

        btnUploadPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(activity_pay_tutor.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPDF();
                }
                else {
                    ActivityCompat.requestPermissions(activity_pay_tutor.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileUri!=null)
                uploadfile(fileUri, SessionId);
                else
                    Toast.makeText(activity_pay_tutor.this, "Select a file", Toast.LENGTH_SHORT).show();
            }
        });

        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(SessionId).hasChild("PaymentFile")){
                   String pdfName = snapshot.child(SessionId).child("PaymentFile").getValue().toString();
                   btnUploadPay.setText("REUPLOAD");
                   tvFile.setText(pdfName + ".pdf");
                }

                if (snapshot.child(SessionId).child("status2").getValue().equals("H" + FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    btnUploadPay.setVisibility(View.INVISIBLE);
                    imgBtnDownload.setVisibility(View.VISIBLE);
                    btnPayNow.setVisibility(View.INVISIBLE);
                    btnCancelPay.setText("Return");
                    lblPage.setText("Session History");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference myrefSubject = FirebaseDatabase.getInstance().getReference();
        DatabaseReference subjectRef = myrefSubject.child("Tutors").child(TutorId).child("Subjects");
        final ArrayList subjects = new ArrayList<>();

        subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    String data = ds.child("mySubject").getValue(String.class);
                    subjects.add("" + String.valueOf(data));

                }
                String countt = String.valueOf(snapshot.getChildrenCount());
                Log.d("subject", countt );
                String subjectlist = Arrays.toString(subjects.toArray()).replace("[", "").replace("]", "").trim();
                if (subjectlist.equals(""))
                    tvSubject.setText("No Subject Added");
                else
                    tvSubject.setText(subjectlist);

                /*Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                Log.d("timestamp", ts);*/


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadfile(Uri fileUri, final String SessionID) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Please wait, while we are setting your Data");
        progressDialog.setProgress(0);
        progressDialog.show();

        String fileName = System.currentTimeMillis() + "";

        storagePayPicsRef = FirebaseStorage.getInstance().getReference().child("Uploads");

        //method 2
        if (fileUri != null){
            final StorageReference fileRef = storagePayPicsRef.child(fileName);
            uploadTask = fileRef.putFile(fileUri);
            sessionRef.child(SessionID).child("PaymentFile").setValue(fileName);
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
                        userMap.put("PaymentPic", myUri);
                        sessionRef.child(SessionID).updateChildren(userMap);

                        progressDialog.dismiss();
                        Toast.makeText(activity_pay_tutor.this, "File uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(activity_pay_tutor.this, "File not selected", Toast.LENGTH_SHORT).show();
        }

        //method1
        /*storagePayPicsRef.child("Uploads").child(fileName).putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Sessions").child(SessionID);
                        reference.child("PaymentPic").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(activity_pay_tutor.this, "File Successfully uploaded", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(activity_pay_tutor.this, "Something went wrong, Please Try Again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity_pay_tutor.this, "Something went wrong, Please Try Again!", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int currentProgress = (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
                if(currentProgress == 100){
                    progressDialog.dismiss();
                }
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectPDF();


        }else {
            Toast.makeText(activity_pay_tutor.this, "Please Allow Permission to Upload", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPDF() {
        Intent intent3 = new Intent();
        intent3.setType("application/pdf");
        intent3.setAction(Intent.ACTION_GET_CONTENT);
        /*String filename = intent3.getData().getPath();*/
        /*tvFile.setText("" + filename);*/
        startActivityForResult(intent3, 86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            fileUri = data.getData();
            tvFile.setText("" + data.getData().getLastPathSegment());

        }else {
            Toast.makeText(activity_pay_tutor.this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }
}