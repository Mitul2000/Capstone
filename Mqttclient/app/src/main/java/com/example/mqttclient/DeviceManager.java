package com.example.mqttclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class DeviceManager extends AppCompatActivity {

    String auto, name, id, status;
    TextView device_name;
    MqttAndroidClient client;
    String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);
//        update_devicename = findViewById(R.id.pt_rename);
//        setfrequency = (EditText) findViewById(R.id.pt_frequencynum);
        device_name = findViewById(R.id.device_name);
        getandsetIntentData();

        mqttconnect();
    }

    void getandsetIntentData(){
        if(getIntent().hasExtra("device_auto") && getIntent().hasExtra("device_name")
                && getIntent().hasExtra("device_id") && getIntent().hasExtra("device_status")){
            auto = getIntent().getStringExtra("device_auto");
            name = getIntent().getStringExtra("device_name");
            id = getIntent().getStringExtra("device_id");
            status = getIntent().getStringExtra("device_status");
            device_name.setText(name);
        }else{
            Toast.makeText(this, "No data.",Toast.LENGTH_SHORT);
        }
    }

    //=============>>Update Name
//    public void updatename(View view){
//        String changename = update_devicename.getText().toString();
//        DatabaseHelper mydb = new DatabaseHelper(DeviceManager.this);
//        mydb.updateData(auto, changename);
//    }

    //======>>>> Delete Device
//    public void deletedevice(View view){
//        confimDialog();
//    }
//    public void confimDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Delete : "+ name);
//        builder.setMessage("Are you sure?");
//        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                DatabaseHelper mydb = new DatabaseHelper(DeviceManager.this);
//                mydb.deleteOneRow(auto);
//            }
//        });
//        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.create().show();
//    }


    public void mqttconnect(){
        Log.i("MQTT", "mqttconnect: start ");
        Toast.makeText(this, "start_connection", Toast.LENGTH_SHORT).show();


        clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("MQTT", "onSuccess");
                    Toast.makeText(DeviceManager.this, "connection successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("MQTT", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
/////===>>>> Light On
    public void mqttlighton(View view){
        String topic = id + "/MobileApp";
        String payload = "3";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    /////===>>>> Alarm On
    public void mqttalarm(View view){
        String topic = id + "/MobileApp";
        String payload = "2";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }


    /////===>>>> Sprinkler On
    public void mqttsprinkler(View view) {
        String topic = id + "/MobileApp";
        String payload = "1";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void settings(View view) {
        Intent i = new Intent(DeviceManager.this, Settings.class);
        i.putExtra("device_auto",auto );
        i.putExtra("device_name", name);
        i.putExtra("device_id", id);
        i.putExtra("device_status", status);
        startActivity(i);
    }


    ////==>>> SendFrequency
//    public void sendFrequency(View view) {
//        String topic = id + "/MobileApp";
//
//        String mess = setfrequency.getText().toString();
//        String payload = "f"+ mess;
//        byte[] encodedPayload = new byte[0];
//        try {
//            encodedPayload = payload.getBytes("UTF-8");
//            MqttMessage message = new MqttMessage(encodedPayload);
//            client.publish(topic, message);
//        } catch (UnsupportedEncodingException | MqttException e) {
//            e.printStackTrace();
//        }
//    }


    ///==>>>> Camm
//    public void opencam (View view) {
//        Intent i = new Intent(this, LiveFeed.class);
//        startActivity(i);
//    }
}