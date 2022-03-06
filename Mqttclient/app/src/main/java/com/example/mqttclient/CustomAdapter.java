package com.example.mqttclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<String> device_auto, device_name, device_id, device_status;
    Activity activity;

    CustomAdapter(Activity activity, Context context, ArrayList device_auto, ArrayList device_name, ArrayList device_id, ArrayList device_status)  {
        this.activity = activity;
        this.device_auto = device_auto;
        this.device_name = device_name;
        this.device_id = device_id;
        this.device_status = device_status;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.d_auto.setText(String.valueOf(device_auto.get(position)));
        holder.d_name.setText(String.valueOf(device_name.get(position)));
        holder.d_id.setText(String.valueOf(device_id.get(position)));
        holder.d_status.setText(String.valueOf(device_status.get(position)));
        int pos = position;

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(device_id.get(pos)).equals("Camera")){
                    Intent intent = new Intent(context, Cameralog.class);
                    intent.putExtra("device_auto", String.valueOf(device_auto.get(pos)));
                    intent.putExtra("device_name", String.valueOf(device_name.get(pos)));
                    intent.putExtra("device_id",String.valueOf(device_id.get(pos)));
                    intent.putExtra("device_status",String.valueOf(device_status.get(pos)));
                    activity.startActivityForResult(intent, 1);
                }
                else{

                    Intent intent = new Intent(context, DeviceManager.class);
                    intent.putExtra("device_auto", String.valueOf(device_auto.get(pos)));
                    intent.putExtra("device_name", String.valueOf(device_name.get(pos)));
                    intent.putExtra("device_id",String.valueOf(device_id.get(pos)));
                    intent.putExtra("device_status",String.valueOf(device_status.get(pos)));
                    activity.startActivityForResult(intent, 1);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return device_auto.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView d_auto, d_name, d_id, d_status;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            d_auto = itemView.findViewById(R.id.tv_device_auto);
            d_name = itemView.findViewById(R.id.tv_device_name);
            d_id = itemView.findViewById(R.id.tv_device_id);
            d_status = itemView.findViewById(R.id.tv_device_status);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
