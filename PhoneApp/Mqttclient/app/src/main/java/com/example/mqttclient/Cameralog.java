package com.example.mqttclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Cameralog extends AppCompatActivity {

    String auto, name, id, status;
    TextView cameralog;
    RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceDetections;
    List<Detected> detectedList = new ArrayList<Detected>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameralog);
        cameralog = findViewById(R.id.cameralog);
        recyclerView = findViewById(R.id.detectionRecyclerView);
        getandsetIntentData();
        getimagedata();


        //passing data.
        cameralog.setText("Detection Log");

    }

    void getandsetIntentData() {
        if (getIntent().hasExtra("device_auto") && getIntent().hasExtra("device_name")
                && getIntent().hasExtra("device_id") && getIntent().hasExtra("device_status")) {
            auto = getIntent().getStringExtra("device_auto");
            name = getIntent().getStringExtra("device_name");
            id = getIntent().getStringExtra("device_id");
            status = getIntent().getStringExtra("device_status");

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT);
        }
    }

    private void getimagedata() {

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceDetections = mDatabase.getReference("Detected");
        List<String> keys = new ArrayList<>();

        mReferenceDetections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detectedList.clear();
                List<String> keys = new ArrayList<>();
                List<String> vals = new ArrayList<>();

                for(DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    //String obj = keyNode.getValue().toString();
                    Detected animal = keyNode.getValue(Detected.class);
                    Detected insert = new Detected(animal.name, animal.timestamp, animal.image);
                    detectedList.add(insert);


                }
                DetectionAdapter myDetectionAdapter = new DetectionAdapter(Cameralog.this, detectedList);
                recyclerView.setAdapter(myDetectionAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Cameralog.this));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}