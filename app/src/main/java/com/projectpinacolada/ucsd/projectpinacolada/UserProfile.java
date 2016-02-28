package com.projectpinacolada.ucsd.projectpinacolada;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

public class UserProfile extends AppCompatActivity {

    TextView name;
    TextView location;
    TextView reviews;

    Button editProfile;
    Button logout;
    ImageButton profilePicture;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    private byte[] imageBytes;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = (TextView) findViewById(R.id.user_name);
        location = (TextView) findViewById(R.id.user_location);
        reviews = (TextView) findViewById(R.id.user_num_reviews);
        editProfile = (Button) findViewById(R.id.update_profile_button);
        logout = (Button) findViewById(R.id.logout_button);
        profilePicture = (ImageButton) findViewById(R.id.profileImageButton);

        user = ParseUser.getCurrentUser();

        setUserInfo();

        getPicture();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditProfile();
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }

    private void setUserInfo() {
        ParseUser currUser = ParseUser.getCurrentUser();
        name.setText(currUser.getString("name"));
        location.setText(currUser.getString("location"));
        reviews.setText(Integer.toString(currUser.getInt("numReviews")));
    }

    private void startEditProfile() {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    private void logout() {
        boolean good = userLogout();
        if(good){
            goToLogin();
        }
        else {
            setAlert();
        }
    }

    //helper method to log out a user
    private boolean userLogout(){
        //check if parse user is not null (this should always be true, but just a failsafe
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            return false;
        }

        // log the user out
        ParseUser.logOut();

        //check to make sure the user really was logged out
        user = ParseUser.getCurrentUser();
        if(user == null) {
            return true;
        }
        else {
            return false;
        }
    }

    //go back to the login screen after the user has signed out
    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //alert the user that they were not able to logout
    private void setAlert(){
        //Display an alert dialog to see if user wants to try again or get a random item
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(R.string.logout_error);
        builder1.setMessage(R.string.logout_message);
        builder1.setCancelable(true);
        builder1.setIconAttribute(android.R.attr.alertDialogIcon);

        builder1.setPositiveButton(
                R.string.logout_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                R.string.logout_try_again,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        logout();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void getPicture() {
        ParseFile picture = (ParseFile)user.get("userImage");
        if (picture != null) {
            picture.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        imageBytes = data;
                        setImage();
                    }
                }
            });
        }
    }

    private void setImage() {
        imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        if(imageBitmap != null){
            profilePicture.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap,250,250,false));
        }
    }

    public void sendNewPicture() {

        ParseFile file = new ParseFile("profileimage.png", imageBytes);
        try {
            file.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.put("userImage", file);
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setImage();

    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageBytes = stream.toByteArray();
            sendNewPicture();
        }
    }
}
