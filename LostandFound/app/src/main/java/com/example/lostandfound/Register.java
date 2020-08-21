package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    EditText et_name, et_reg, et_email, et_mobile, et_pass;
    FirebaseDatabase database;
    DatabaseReference reference;

    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userList = new ArrayList<>();
        et_name = findViewById(R.id.full_name_et);
        et_reg = findViewById(R.id.reg_num_et);
        et_email = findViewById(R.id.email_et);
        et_mobile = findViewById(R.id.mobile_num_et);
        et_pass = findViewById(R.id.pass_reg);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

    }

    public void register(View view) {
        final String username = et_name.getText().toString();
        final String reg = et_reg.getText().toString();
        final String email = et_email.getText().toString();
        final String mobile = et_mobile.getText().toString();
        final String pass = et_pass.getText().toString();
        if (username.isEmpty() || reg.isEmpty() || email.isEmpty() || mobile.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show();
        } else {
            reference.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User u = dataSnapshot.getValue(User.class);
                        userList.add(u);
                    }
                    Log.i("SIZE", "" + userList.size());
                    if (userList.size() != 0) {
                        int count = 0;
                        for (int i = 0; i < userList.size(); i++) {
                            if (reg.equals(userList.get(i).getReg())) {
                                Toast.makeText(Register.this,
                                        "The Username " + reg + " is already existed",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                count++;
                                if(count == userList.size()){
                                    User u = new User(username, reg, email, mobile, pass);
                                    reference.child("Users").push().setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            et_name.setText("");
                                            et_reg.setText("");
                                            et_email.setText("");
                                            et_mobile.setText("");
                                            et_pass.setText("");

                                        }
                                    });
                                }





                            }
                        }


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }

    public void openlogin(View view) {
        Intent i = new Intent(this, Login.class);
        startActivity(i);

    }
}