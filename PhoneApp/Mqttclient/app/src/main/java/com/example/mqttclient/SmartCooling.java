package com.example.mqttclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class SmartCooling extends AppCompatActivity {

    String auto, name, id, status;
    TextView tv_tempupdate, tv_humupdate;
    MqttAndroidClient client;
    String clientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_cooling);
        tv_tempupdate = findViewById(R.id.tv_tempupdate);
        tv_humupdate = findViewById(R.id.tv_humupdate);
        getandsetIntentData();
        mqttconnect();
    }
    public void remove(View view){

    }
    void getandsetIntentData(){
        if(getIntent().hasExtra("device_auto") && getIntent().hasExtra("device_name")
                && getIntent().hasExtra("device_id") && getIntent().hasExtra("device_status")){
            auto = getIntent().getStringExtra("device_auto");
            name = getIntent().getStringExtra("device_name");
            id = getIntent().getStringExtra("device_id");
            status = getIntent().getStringExtra("device_status");
        }else{
            Toast.makeText(this, "No data.",Toast.LENGTH_SHORT);
        }
    }

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
                    Toast.makeText(SmartCooling.this, "connection successful", Toast.LENGTH_SHORT).show();
                    sub();
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
        //callback subscription:
//        String topic = "AW234lk23S/update";
//        int qos = 1;
//        try {
//            IMqttToken subToken = client.subscribe(topic, qos);
//            subToken.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    // The message was published
//                    Log.d("MQTT", "success to subscribe");
//                    Toast.makeText(SmartCooling.this, "connection successful", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken,
//                                      Throwable exception) {
//                    // The subscription could not be performed, maybe the user was not
//                    // authorized to subscribe on the specified topic e.g. using wildcards
//                    Log.d("MQTT", "onFailure to subscribe");
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
    }

    public void turnonmotor(View view){
        String topic = "EuANiaPJsf" + "/MobileApp";
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
    public void sub(){
        String topic = "AW234lk23S/update";
        try{
            client.subscribe(topic,0);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d("subscribeTopic", "messageArrived: " + topic);
                    Log.d("subscribeMessage", "messageArrived: " + new String(message.getPayload()));
                    Toast.makeText(SmartCooling.this, "mess: "+ new String(message.getPayload()), Toast.LENGTH_SHORT).show();
                    String capturemes = new String(message.getPayload());
                    String[] arrOfStr = capturemes.split(" ", 6);
                    if(arrOfStr.length>=4){
                        tv_tempupdate.setText(arrOfStr[1]);
                        tv_humupdate.setText(arrOfStr[3]);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

        }catch (MqttException e){

        }
    }

}