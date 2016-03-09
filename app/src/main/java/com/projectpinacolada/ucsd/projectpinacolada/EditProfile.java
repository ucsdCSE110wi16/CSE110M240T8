package com.projectpinacolada.ucsd.projectpinacolada;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Objects;

//used to edit the user profile
public class EditProfile extends AppCompatActivity {

    String name;
    String location;
    String email;

    EditText nameField;
    EditText emailField;
    EditText locationField;
    Button submit;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Edit Profile Information");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameField = (EditText) findViewById(R.id.nameField);
        emailField = (EditText) findViewById(R.id.emailField);
        locationField = (EditText) findViewById(R.id.locationField);

        user = ParseUser.getCurrentUser();

        setFields();

        submit = (Button) findViewById(R.id.updateButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitChanges();
            }
        });

    }

    private void setFields(){

        name = user.getString("name");
        location = user.getString("location");
        email = user.getEmail();

        nameField.setText(name);
        emailField.setText(email);
        locationField.setText(location);
    }

    private void submitChanges(){
        boolean good = checkData();
        if (!good)
            return;

        good = updateParse();
        if(good){
            Intent intent = new Intent(this, UserProfile.class);
            setResult(Activity.RESULT_OK, intent);
            //return to previous screen
            finish();
        }

    }

    private boolean updateParse() {
        boolean changes = false;
        //changes to name were made
        if (!Objects.equals(name, nameField.getText().toString())) {
            user.put("name", nameField.getText().toString());
            changes = true;
        }
        //changes to email were made
        if (!Objects.equals(email, emailField.getText().toString())) {
            user.setEmail(emailField.getText().toString());
            user.setUsername(emailField.getText().toString());
            changes = true;
        }
        //changes to location were made
        if (!Objects.equals(location, locationField.getText().toString())) {
            user.put("location", locationField.getText().toString());
            changes = true;
        }

        if (changes) {
            try {
                user.save();
            } catch (ParseException e) {
                e.printStackTrace();
                nameField.setError(getString(R.string.error_invalid_name));
                emailField.setError(getString(R.string.error_invalid_email));
                locationField.setError(getString(R.string.error_invalid_location));
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    private boolean checkData() {
        nameField.setError(null);
        emailField.setError(null);
        locationField.setError(null);

        boolean error = false;

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String location = locationField.getText().toString();

        View focusView = null;

        if(TextUtils.isEmpty(name)) {
            nameField.setError(getString(R.string.error_field_required));
            focusView = nameField;
            error = true;
        }
        else if(!isNameVaild(name)){
            nameField.setError(getString(R.string.error_invalid_name));
            focusView = nameField;
            error = true;
        }

        if(TextUtils.isEmpty(email)){
            emailField.setError(getString(R.string.error_field_required));
            focusView = emailField;
            error = true;
        }
        else if(!isEmailValid(email)){
            emailField.setError(getString(R.string.error_invalid_email));
            focusView = emailField;
            error = true;
        }

        if(TextUtils.isEmpty(location)){
            locationField.setError(getString(R.string.error_field_required));
            focusView = locationField;
            error = true;
        }
        else if(!isLocationValid(location)){
            locationField.setError(getString(R.string.error_invalid_location));
            focusView = locationField;
            error = true;
        }

        //check if error was set
        if(error){
            focusView.requestFocus();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean isNameVaild(String name) {
        return name.length() > 1;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isLocationValid(String location) {
        return location.length() > 1;
    }
}
