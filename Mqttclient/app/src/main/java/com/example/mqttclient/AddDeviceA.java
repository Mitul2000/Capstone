package com.example.mqttclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddDeviceA extends AppCompatActivity {
    EditText device_name, device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
    }
    public void findmydevice(View view){
        device_name = findViewById(R.id.pt_deviceName);
        device_id = findViewById(R.id.pt_deviceId);

        DatabaseHelper mydb = new DatabaseHelper(AddDeviceA.this);
        mydb.adddevice(device_name.getText().toString().trim(),
                device_id.getText().toString().trim());
    }
}