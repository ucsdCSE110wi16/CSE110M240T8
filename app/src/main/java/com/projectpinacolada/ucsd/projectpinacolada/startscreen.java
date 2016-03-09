package com.projectpinacolada.ucsd.projectpinacolada;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;

public class startscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Click to Scan or View Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);

        Button scanButton = (Button) findViewById(R.id.camera_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        Button profileButton = (Button) findViewById(R.id.userButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserProfile();
            }
        });
    }

    public void openCamera() {
        Intent intent = new Intent(this,CameraActivity.class);

        startActivity(intent);
    }

    public void openUserProfile() {
        Intent intent = new Intent(this, UserProfile.class);

        startActivity(intent);
    }

}
