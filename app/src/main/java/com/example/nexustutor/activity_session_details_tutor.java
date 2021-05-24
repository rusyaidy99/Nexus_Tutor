package com.example.nexustutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class activity_session_details_tutor extends AppCompatActivity {
    private TextView tvName, tvSubject, tvDate, tvGender;
    private TextView tvFileName;
    private EditText etHours, etPrice;
    private Button btnContact, btnSave, btnReturn;
    private Button btnEnd, btnCancel;
    private ImageButton imgBtnReport, imgBtnBack, imgBtnDownload;
    private CircleImageView circleImageViewStudent;
    private CardView cardViewSet, cardviewFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details_tutor);

        tvName = findViewById(R.id.tv_detail_name);
        tvSubject = findViewById(R.id.tv_detail_subject);
        tvDate = findViewById(R.id.tv_detail_session_date);
        tvGender = findViewById(R.id.tv_detail_gender);

        tvFileName = findViewById(R.id.tv_download_filename);

        etHours = findViewById(R.id.et_hours_detail);
        etPrice = findViewById(R.id.et_fee_amount_detail);

        btnContact = findViewById(R.id.btn_detail_contact);

        btnSave = findViewById(R.id.btn_save_details);
        btnReturn = findViewById(R.id.btn_cancel_detail);

        btnEnd = findViewById(R.id.btn_end_session);
        btnCancel = findViewById(R.id.btn_cancel_event);

        imgBtnReport = findViewById(R.id.img_btn_detail_report);
        imgBtnBack = findViewById(R.id.img_btn_back);
        imgBtnDownload = findViewById(R.id.img_btn_download_file);

        circleImageViewStudent = findViewById(R.id.circleImg_profile_detail);

        cardViewSet = findViewById(R.id.cardview_set_fee);
        cardviewFinish = findViewById(R.id.cardview_download_file);


        Intent intent = getIntent();
        final String UserId = intent.getExtras().getString("UID");
        final String TutorId = intent.getExtras().getString("TID");
        String SessionTime = intent.getExtras().getString("Time");
        String SessionDate = intent.getExtras().getString("Date");
        final String SessionId = intent.getExtras().getString("SessionID");

        final DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
        final DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference().child("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final DatabaseReference sessionRef = FirebaseDatabase.getInstance().getReference().child("Sessions");

        tvDate.setText(SessionDate);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {



                if (snapshot.child(SessionId).hasChild("PaymentFile")){
                    final String pdfName = snapshot.child(SessionId).child("PaymentFile").getValue().toString();
                    final String pdfUrl = snapshot.child(SessionId).child("PaymentPic").getValue().toString();

                    tvFileName.setText(pdfName + ".pdf");
                    imgBtnDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            download(pdfName, pdfUrl);
                        }
                    });
                }

                
                if (snapshot.child(SessionId).hasChild("PaymentPic")){
                    cardViewSet.setVisibility(View.GONE);
                    cardviewFinish.setVisibility(View.VISIBLE);
                }


                if (snapshot.child(SessionId).hasChild("fee") || snapshot.child(SessionId).hasChild("duration")) {
                    String feeVal = snapshot.child(SessionId).child("fee").getValue().toString();
                    String DurVal = snapshot.child(SessionId).child("duration").getValue().toString();
                    etPrice.setText(feeVal);
                    etHours.setText(DurVal);
                }


                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String fee = etPrice.getText().toString().trim();
                            String duration = etHours.getText().toString().trim();
                            if (fee.isEmpty()){
                                etPrice.setError("Fee Amount is required");
                                etPrice.requestFocus();
                                return;
                            }
                            if (duration.isEmpty()){
                                etHours.setError("Duration Amount is required");
                                etHours.requestFocus();
                                return;
                            }
                            if (!fee.isEmpty() && !duration.isEmpty() ){
                                sessionRef.child(SessionId).child("fee").setValue(fee);
                                sessionRef.child(SessionId).child("duration").setValue(duration);
                                finish();
                            }

                            Toast.makeText(activity_session_details_tutor.this, "Fee Details Updated", Toast.LENGTH_SHORT).show();
                        }
                    });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                String Sname = snapshot.child("fullname").getValue().toString();
                String Sgender = snapshot.child("gender").getValue().toString();

                tvName.setText(Sname);
                tvGender.setText(Sgender);

                if (snapshot.hasChild("image")){
                    String image = snapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(circleImageViewStudent);
                }

                if (snapshot.hasChild("phone")){
                    btnContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String Tphone = snapshot.child("phone").getValue().toString();
                            Intent intent2 = new Intent(Intent.ACTION_DIAL);
                            intent2.setData(Uri.parse("tel:"+Tphone));
                            if (ContextCompat.checkSelfPermission(activity_session_details_tutor.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(intent2);
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{CALL_PHONE}, 1);
                                }
                            }
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(SessionId).child("status").getValue().equals("H" + TutorId))
                    btnEnd.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionRef.child(SessionId).child("status").setValue("H" + TutorId);
                sessionRef.child(SessionId).child("status2").setValue("H" + UserId);

                tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference removeReq =  tutorRef.child("Actives").child(UserId);
                        removeReq.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                tutorRef.child("History").child(UserId).setValue(true);

                studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference removeReq =  studentRef.child("Sessions").child("Actives").child(TutorId);
                        removeReq.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                studentRef.child("Sessions").child("History").child(TutorId).setValue(true);
                Toast.makeText(activity_session_details_tutor.this, " You have Ended Your Session with" + tvName.getText().toString(), Toast.LENGTH_SHORT).show();
                finish();
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

    private void download(String pdfName, String pdfUrl) {
        /*StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference storeRef = storageReference.child("Uploads").child(pdfName);
        */

        downloadFile(activity_session_details_tutor.this, pdfName, ".pdf", DIRECTORY_DOWNLOADS, pdfUrl);
        /*storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                
            }
        });*/
    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadManager.enqueue(request);
        Toast.makeText(context, "Downloading file", Toast.LENGTH_SHORT).show();
    }
}