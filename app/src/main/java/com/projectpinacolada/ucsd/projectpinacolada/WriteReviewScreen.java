package com.projectpinacolada.ucsd.projectpinacolada;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RatingBar;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class WriteReviewScreen extends AppCompatActivity {

    private TextView productNameTV;             // Text box displaying product name

    // Class Variables
    Button submitButton;
    EditText reviewTextField;
    EditText reviewTitleTextField;
    String reviewTitle;
    String reviewText;
    double reviewRating;
    RatingBar ratingBar;
    public boolean saveGood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Write a Review");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review_screen);

        // Initializing Text Views and Fields
        //productNameTV = (TextView) findViewById(R.id.productName);
        reviewTitleTextField = (EditText) findViewById(R.id.reviewTitle);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        reviewTextField = (EditText) findViewById(R.id.review_text);
        reviewTitleTextField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        reviewTextField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        //set the default value
        saveGood = true;

        // Populate product name
        //productNameTV.setText(getIntent().getStringExtra("productName"));

        // create submit button
        submitButton = (Button) findViewById(R.id.write_review_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call the submitClicked method
                submitClicked();
            }
        });
    }

    //check for any errors, then submit to the database and return
    private void submitClicked() {
        boolean retVal = setReviewDetails();
        //if there was not an error, continue
        if(!retVal){

            retVal = uploadToDB();

            if (!retVal) {
                // give an error message to the user
                //Display an alert dialog to see if user wants to try again or get a random item
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(R.string.Oops);
                builder1.setMessage(R.string.writeError);
                builder1.setCancelable(true);
                builder1.setIconAttribute(android.R.attr.alertDialogIcon);

                builder1.setPositiveButton(
                        R.string.OK,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {
                //increment the number of reviews from the user
                ParseUser.getCurrentUser().increment("numReviews");
                ParseUser.getCurrentUser().saveEventually();

                // Return us back to product info page

                Intent intent = new Intent(this, ProductInfo.class);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        }
    }

    // Setter method for extracting the review details
    // and storing it in WriteReviewScreen.reviewText
    // also will do some error checking to ensure values have been set
    private boolean setReviewDetails(){
        reviewText = reviewTextField.getText().toString();
        reviewTitle = reviewTitleTextField.getText().toString();
        reviewRating = (double) ratingBar.getRating();

        return checkErrors();
    }

    //checks all fields, if there is an error, return true
    private boolean checkErrors() {
        reviewTextField.setError(null);
        reviewTitleTextField.setError(null);

        View focusView = null;
        boolean error = false;

        // check for error with title
        if(TextUtils.isEmpty(reviewTitle)){
            reviewTitleTextField.setError(getString(R.string.error_field_required));
            focusView = reviewTitleTextField;
            error = true;
        }
        else if(!titleValid()){
            focusView = reviewTitleTextField;
            error = true;
        }

        //check for error with review
        if(TextUtils.isEmpty(reviewText)){
            reviewTextField.setError(getString(R.string.error_field_required));
            focusView = reviewTextField;
            error = true;
        }
        else if(!reviewValid()){
            focusView = reviewTextField;
            error = true;
        }

        //if there was an error, set the focus to the error field
        if(error) {
            focusView.requestFocus();
        }

        return error;
    }

    // Method to publish WriteReviewScreen data fields to parse
    private boolean uploadToDB () {

        //check if there is an internet connection
        //if not, then return an error now
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            saveGood = false;
            return saveGood;
        }

        ParseObject review = new ParseObject("Review");
        ParseUser currentUser = ParseUser.getCurrentUser();

        // For now, name is not db normalized
        // Later update to retrieve by userObjectId
        String name = currentUser.getString("name");
        review.put("reviewerObjectId", currentUser.getObjectId());
        review.put("reviewer", name);

        review.put("upcCode", Long.valueOf(getIntent().getStringExtra("upcCode")));
        review.put("reviewText", reviewText);
        review.put("rating", reviewRating);
        review.put("reviewTitle", reviewTitle);

        //save the review with a callback
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    //there was an exception, internet must have been lost
                    saveGood = false;
                }
                else {
                    //there was no error, continue as normal
                    saveGood = true;
                }
            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return saveGood;
    }

    private boolean titleValid() {
        if(reviewTitle.length() < 3) {
            reviewTitleTextField.setError(getString(R.string.review_title_length_min));
            return false;
        }
        else if(reviewTitle.length() > 50) {
            reviewTitleTextField.setError(getString(R.string.review_title_length_max));
            return false;
        }
        else {
            return true;
        }
    }

    private boolean reviewValid() {
        if(reviewText.length() < 14) {
            reviewTextField.setError(getString(R.string.review_text_length_min));
            return false;
        }
        else if(reviewText.length() > 1001) {
            reviewTextField.setError(getString(R.string.review_text_length_max));
            return false;
        }
        else {
            return true;
        }
    }
}
