package com.projectpinacolada.ucsd.projectpinacolada;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.*;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.Objects;

public class Register extends AppCompatActivity {

    /*
        mNameField
        mEmailField
        mPasswordField
        mVerifyPasswrodField
        mLocationField
     */


    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mVerifyPasswrodField;
    private EditText mLocationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mNameField = (EditText) findViewById(R.id.nameField);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mVerifyPasswrodField = (EditText) findViewById(R.id.verifyPasswordField);
        mLocationField = (EditText) findViewById(R.id.locationField);

        Intent intent = getIntent();
        if(intent.hasExtra("email")){
            mEmailField.setText(intent.getStringExtra("email"));
        }
        if(intent.hasExtra("password")){
            mPasswordField.setText(intent.getStringExtra("password"));
        }

        mNameField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mLocationField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);


        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean good = checkData();
                if(good){
                    boolean attempt = attemptRegistration();
                    if(attempt){
                        openNextActivity();
                    }
                }
            }
        });
    }

    private void openNextActivity() {
        Intent intent = new Intent(this, startscreen.class);

        startActivity(intent);
    }

    private boolean checkData() {
        mNameField.setError(null);
        mEmailField.setError(null);
        mPasswordField.setError(null);
        mVerifyPasswrodField.setError(null);
        mLocationField.setError(null);

        boolean error = false;

        String name = mNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        String verifyPassword = mVerifyPasswrodField.getText().toString();
        String location = mLocationField.getText().toString();

        View focusView = null;

        if(TextUtils.isEmpty(name)) {
            mNameField.setError(getString(R.string.error_field_required));
            focusView = mNameField;
            error = true;
        }
        else if(!isNameVaild(name)){
            mNameField.setError(getString(R.string.error_invalid_name));
            focusView = mNameField;
            error = true;
        }

        if(TextUtils.isEmpty(email)){
            mEmailField.setError(getString(R.string.error_field_required));
            focusView = mEmailField;
            error = true;
        }
        else if(!isEmailValid(email)){
            mEmailField.setError(getString(R.string.error_invalid_email));
            focusView = mEmailField;
            error = true;
        }

        if(TextUtils.isEmpty(password)){
            mPasswordField.setError(getString(R.string.error_field_required));
            focusView = mPasswordField;
            if(TextUtils.isEmpty(verifyPassword)){
                mVerifyPasswrodField.setError(getString(R.string.error_field_required));
                focusView = mVerifyPasswrodField;
            }
            error = true;
        }
        else if(!isPasswordValid(password)){
            mPasswordField.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordField;
            if(!isPasswordValid(verifyPassword)){
                mVerifyPasswrodField.setError(getString(R.string.error_invalid_password));
                focusView = mVerifyPasswrodField;
            }
            error = true;
        }
        else if(!doPasswordMatch(password,verifyPassword)){
            mPasswordField.setError(getString(R.string.error_password_match));
            mVerifyPasswrodField.setError(getString(R.string.error_password_match));
            focusView = mPasswordField;
            error = true;
        }

        if(TextUtils.isEmpty(location)){
            mLocationField.setError(getString(R.string.error_field_required));
            focusView = mLocationField;
            error = true;
        }
        else if(!isLocationValid(location)){
            mLocationField.setError(getString(R.string.error_invalid_location));
            focusView = mLocationField;
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

    private boolean attemptRegistration(){
        String name = mNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        String location = mLocationField.getText().toString();

        ParseUser user = new ParseUser();

        user.setPassword(password);
        user.setEmail(email);
        user.setUsername(email);
        user.put("name", name);
        user.put("location",location);

        try {
            user.signUp();
        } catch (ParseException e) {
            e.printStackTrace();
            mNameField.setError(getString(R.string.error_invalid_name));
            mEmailField.setError(getString(R.string.error_invalid_email));
            mPasswordField.setError(getString(R.string.error_invalid_password));
            mVerifyPasswrodField.setError(getString(R.string.error_invalid_password));
            mLocationField.setError(getString(R.string.error_invalid_location));

            View focusView = null;
            focusView = mNameField;
            focusView.requestFocus();

            return false;
        }

        return true;

    }

    private boolean isNameVaild(String name) {
        return name.length() > 1;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password){
        return password.length() > 4;
    }

    private boolean doPasswordMatch(String password, String vPassword){
        return Objects.equals(password, vPassword);
    }

    private boolean isLocationValid(String location) {
        return location.length() > 1;
    }
}
