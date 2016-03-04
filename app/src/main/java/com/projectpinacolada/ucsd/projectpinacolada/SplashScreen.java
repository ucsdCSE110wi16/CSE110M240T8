package com.projectpinacolada.ucsd.projectpinacolada;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseUser;

//this is used for some parse set up as well as checking if the user is already signed in
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //if the user has signed in before, they will skip this screen completely
        ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser != null) {
            //the user is already signed in, skip login
            openNextActivity();
        }
        else {
            //go to login
            openLogin();
        }
    }

    public void openNextActivity() {
        Intent intent = new Intent(this, startscreen.class);

        startActivity(intent);
    }

    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }
}
