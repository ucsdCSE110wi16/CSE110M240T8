package com.projectpinacolada.ucsd.projectpinacolada;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);

        Button scanButton = (Button) findViewById(R.id.camera_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }

    public void openCamera() {
        Intent intent = new Intent(this,CameraActivity.class);

        startActivity(intent);
    }
}
