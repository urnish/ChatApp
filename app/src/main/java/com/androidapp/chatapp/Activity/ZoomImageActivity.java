package com.androidapp.chatapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.androidapp.chatapp.R;

public class ZoomImageActivity extends AppCompatActivity {

    ImageView imgCloseIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomimage);

        initComponets();

        imgCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponets() {
        imgCloseIcon = findViewById(R.id.imgCloseIcon);
    }
}
