package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    EditText et_reg,et_pass;
    DatabaseReference reference;
    List<User> list;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_reg = findViewById(R.id.reg_id);
        et_pass = findViewById(R.id.pass_et);
        tv = findViewById(R.id.textView2);
        reference = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User u = dataSnapshot.getValue(User.class);
                    list.add(u);
                    assert u != null;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void login(View view) {
        final String reg= et_reg.getText().toString();
        final String pass = et_pass.getText().toString();
        for(int i = 0;i<list.size();i++) {
            if (reg.equals(list.get(i).getReg()) && pass.equals(list.get(i).getPass())) {
                //Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                tv.setVisibility(View.GONE);
                et_reg.setText("");
                et_pass.setText("");
                break;

            } else {
               // Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                tv.setVisibility(View.VISIBLE);
            }
        }

    }

    public void openreg(View view) {
        Intent i = new Intent(this,Register.class);
        startActivity(i);
    }
}
