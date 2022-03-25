package com.example.mqttclient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper mydb;
    ArrayList<String> device_auto, device_name, device_id, device_status;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle("Dashboard");
        recyclerView = findViewById(R.id.recyclerView);
        mydb = new DatabaseHelper(Dashboard.this);
        device_name = new ArrayList<>();
        device_id = new ArrayList<>();
        device_status = new ArrayList<>();
        device_auto = new ArrayList<>();
        displayData();

        customAdapter = new CustomAdapter(Dashboard.this,this, device_auto, device_name, device_id, device_status);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            recreate();
        }
    }

    public void addDevice(View view){
        Intent intent = new Intent(Dashboard.this, AddDeviceA.class);
        startActivity(intent);
    }

    public void displayData(){
        Cursor cursor = mydb.readAllData();
        if(cursor.getCount() ==0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
//                device_auto.add(cursor.getString(0));
                device_name.add(cursor.getString(1));
                device_id.add(cursor.getString(2));
                device_status.add(cursor.getString(3));

            }
            for (int i = 0 ; i < device_name.size(); i++){
                device_auto.add(Integer.toString(i+1));
            }
        }
    }
}