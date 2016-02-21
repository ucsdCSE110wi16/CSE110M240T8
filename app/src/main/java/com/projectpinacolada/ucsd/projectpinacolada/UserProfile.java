package com.projectpinacolada.ucsd.projectpinacolada;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class UserProfile extends AppCompatActivity {

    TextView name;
    TextView location;
    TextView reviews;

    Button editProfile;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = (TextView) findViewById(R.id.user_name);
        location = (TextView) findViewById(R.id.user_location);
        reviews = (TextView) findViewById(R.id.user_num_reviews);
        editProfile = (Button) findViewById(R.id.update_profile_button);
        logout = (Button) findViewById(R.id.logout_button);

        setUserInfo();
    }

    private void setUserInfo() {
        ParseUser currUser = ParseUser.getCurrentUser();
        name.setText(currUser.getString("name"));
        location.setText(currUser.getString("location"));
        reviews.setText(Integer.toString(currUser.getInt("numReviews")));
    }
}
