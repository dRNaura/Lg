package com.lg_project.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.lg_project.R;

public class ShowImage extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        String image=getIntent().getStringExtra("Image");
        imageView=(ImageView)findViewById(R.id.imagee);
        Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.logo).into(imageView);



    }
}