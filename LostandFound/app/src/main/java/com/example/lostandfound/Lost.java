package com.example.lostandfound;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

@SuppressWarnings("ALL")
public class Lost extends AppCompatActivity {
    EditText et_lost,et_description;
    Button btn_insert;
    Button btn_submit;
    ImageView img;
    FirebaseDatabase database;
    StorageReference mstorageref;
    private StorageTask storageTask;
    DatabaseReference reference;
    public Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        et_lost = findViewById(R.id.lostitem_et);
        et_description = findViewById(R.id.des_et);
        btn_insert = findViewById(R.id.insert_btn);
        btn_submit = findViewById(R.id.submit_btn);
        database = FirebaseDatabase.getInstance();
        img = findViewById(R.id.image_view);
        reference= database.getReference();
        mstorageref = FirebaseStorage.getInstance().getReference("Images");
        btn_insert.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               insert();
           }
       });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storageTask != null && storageTask.isInProgress()){
                    Toast.makeText(Lost.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                }
                else{
                    upload();
                }
            }
        });
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimetypemap = MimeTypeMap.getSingleton();
        return mimetypemap.getExtensionFromMimeType(cr.getType(uri));

    }

    private void upload() {
        try {
            StorageReference Ref = mstorageref.child(System.currentTimeMillis()+"."+getExtension(imgUri));

            storageTask = Ref.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(Lost.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void insert() {
        Intent intent = new Intent();
        intent.setType("Image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() != null){
            imgUri = data.getData();
            img.setImageURI(imgUri);
        }
    }

    public void submit(View view) {
        String lostitem = et_lost.getText().toString();
        String description = et_description.getText().toString();
        if(lostitem.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please fill the Details", Toast.LENGTH_SHORT).show();
        }
        else {
            details d = new details(lostitem, description);
            reference.child("Details").push().setValue(d).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Lost.this, "Details saved Successfully", Toast.LENGTH_SHORT).show();
                    et_lost.setText("");
                    et_description.setText("");

                }
            });
        }

    }
}


