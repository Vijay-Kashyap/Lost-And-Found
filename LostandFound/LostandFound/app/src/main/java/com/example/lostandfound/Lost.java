package com.example.lostandfound;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Lost extends AppCompatActivity implements View.OnClickListener {
    EditText et_lost,et_description, et_owner;
    Button btn_insert;
    Button btn_submit;
    ImageView img;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;
    public static final int REQUEST_CODE_GALLERY = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lost);
        try {
            et_lost = findViewById(R.id.lostitem_et);
            et_description = findViewById(R.id.des_et);
            et_owner = findViewById(R.id.owner_et);
            img = findViewById(R.id.image_view);
            btn_insert = findViewById(R.id.insert_btn);
            btn_submit = findViewById(R.id.submit_btn);
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            database = FirebaseDatabase.getInstance();
            reference = database.getReference();
            createNotificationChannel();
            btn_insert.setOnClickListener(this);
            btn_submit.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification";
            String description = "Someone Lost their Item";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("noti", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void imageUpload(Uri filepath){
        try {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading File");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMax(100);
            pd.setCancelable(false);
            pd.show();
            storageReference=storageReference.child("Images/"+ UUID.randomUUID().toString());
            storageReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    try {
                        Toast.makeText(Lost.this,
                                "Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    try {
                        Toast.makeText(Lost.this,
                                ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    try {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred())
                                / taskSnapshot.getTotalByteCount();
                        pd.setProgress((int) progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.insert_btn:
                insert();
                break;

            case R.id.submit_btn:
                submit();
                break;
        }
    }
        private void submit () {
            final String lostitem = et_lost.getText().toString();
            final String description = et_description.getText().toString();
            final String owner = et_owner.getText().toString();
            if (lostitem.isEmpty() || description.isEmpty() || owner.isEmpty()) {
                Toast.makeText(this, "Please fill the Details", Toast.LENGTH_SHORT).show();
        } else {
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    details d = null;
                    try {
                        String imagePath = uri.toString();
                        d = new details(lostitem, description, owner, imagePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    reference.child("Details").push().setValue(d).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    try {
                                        Toast.makeText(Lost.this, "Your Details are Saved Successfully....", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Lost.this,Found.class);
                                        startActivity(intent);
                                        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Lost.this);
                                        Intent inten = new Intent(Lost.this, Found.class);
                                        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(Lost.this, 0, intent, 0);
                                        final NotificationCompat.Builder builder = new NotificationCompat.Builder(Lost.this, "noti")
                                                .setSmallIcon(R.drawable.ic_notification)
                                                .setContentTitle("New Item Lost")
                                                .setContentText("Someone Lost their Item...")
                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                        .bigText("Help them if you find the Item..."))
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent).setAutoCancel(true);
                                        notificationManagerCompat.notify(100, builder.build());
                                        et_lost.setText("");
                                        et_description.setText("");
                                        et_owner.setText("");
                                        img.setImageURI(Uri.parse(""));
                                        et_lost.requestFocus();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            });
        }
    }
    private void insert() {
        try {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i,REQUEST_CODE_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY){
            if(resultCode == RESULT_OK){
                try {
                    assert data != null;
                    Uri u = data.getData();
                    img.setImageURI(u);
                    imageUpload(u);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
