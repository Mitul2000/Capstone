package com.example.mqttclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetectionAdapter extends RecyclerView.Adapter<DetectionAdapter.MyViewHolder> {

    List<Detected> animals;
    Context context;
    public DetectionAdapter(Context ct, List<Detected> animals) {
        this.animals = animals;
        this.context = ct;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.detected_row, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(animals.get(position).name);
        holder.myText2.setText(animals.get(position).timestamp);

        String imageString = animals.get(position).image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.myImage.setImageBitmap(decodedImage);
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;
        ImageView myImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.detectedName);
            myText2 = itemView.findViewById(R.id.detectedTime);
            myImage = itemView.findViewById(R.id.detectedimageView);
        }
    }
}
