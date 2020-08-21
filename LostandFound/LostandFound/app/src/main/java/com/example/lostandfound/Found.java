package com.example.lostandfound;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Found extends AppCompatActivity {
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<details> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        try {
            recyclerView = findViewById(R.id.rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            reference = FirebaseDatabase.getInstance().getReference().child("Details");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        if(reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        list = new ArrayList<>();
                        for(DataSnapshot ds: snapshot.getChildren()) {
                            list.add(ds.getValue(details.class));
                        }
                        MyAdapter adapter = new MyAdapter(getApplicationContext(),list);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Found.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}