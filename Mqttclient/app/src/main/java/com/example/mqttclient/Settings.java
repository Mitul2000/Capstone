package com.example.mqttclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    String auto, name, id, status;
    TextView update_devicename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        update_devicename = findViewById(R.id.device_rename);
        getandsetIntentData();
    }

    void getandsetIntentData(){
        if(getIntent().hasExtra("device_auto") && getIntent().hasExtra("device_name")
                && getIntent().hasExtra("device_id") && getIntent().hasExtra("device_status")){
            auto = getIntent().getStringExtra("device_auto");
            name = getIntent().getStringExtra("device_name");
            id = getIntent().getStringExtra("device_id");
            status = getIntent().getStringExtra("device_status");
            Toast.makeText(this, auto+ " " + name + " " + id + " " + status, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No data.",Toast.LENGTH_SHORT);
        }
    }


//    =============>>Update Name
    public void updatename(View view){

        String changename = update_devicename.getText().toString();
        DatabaseHelper mydb = new DatabaseHelper(Settings.this);
        mydb.updateData(id, changename);
        startActivity(new Intent(Settings.this, Dashboard.class));
    }

//    ======>>>> Delete Device
    public void deletedevice(View view){
        confimDialog();
    }
    public void confimDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete : "+ name);
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper mydb = new DatabaseHelper(Settings.this);
                mydb.deleteOneRow(id);
                startActivity(new Intent(Settings.this, Dashboard.class));
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

}