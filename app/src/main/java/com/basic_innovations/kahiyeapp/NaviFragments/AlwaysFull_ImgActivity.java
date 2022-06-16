package com.basic_innovations.kahiyeapp.NaviFragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.basic_innovations.kahiyeapp.R;
import com.squareup.picasso.Picasso;

public class AlwaysFull_ImgActivity extends AppCompatActivity {
    ImageView alwImgFull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_always_full__img);
        alwImgFull = findViewById(R.id.alw_full_img);
        String imgUrl = getIntent().getStringExtra("imgUrl");

        Picasso.with(this).load(imgUrl)
                .into(alwImgFull);
    }
}
