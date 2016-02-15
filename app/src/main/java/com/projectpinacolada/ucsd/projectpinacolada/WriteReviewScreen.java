package com.projectpinacolada.ucsd.projectpinacolada;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RatingBar;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class WriteReviewScreen extends AppCompatActivity {

    private TextView productNameTV;             // Text box displaying product name

    // Class Variables
    Button submitButton;
    EditText reviewTextField;
    String reviewText;
    Double reviewRating;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review_screen);

        // Initializing Text Views and Fields
        productNameTV = (TextView) findViewById(R.id.productName);
        reviewTextField = (EditText) findViewById(R.id.review_text);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // Populate product name
        productNameTV.setText(getIntent().getStringExtra("productName"));


        // TODO get the real star ratings
        //reviewRating = 4.5;

        // create submit button
        submitButton = (Button) findViewById(R.id.write_review_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReviewDetails();
                boolean retVal = uploadToDB();

                if (!retVal) {
                    // TODO msg user with error
                }
                else {
                    // Return us back to product info page
                    finish();
                }

            }
        });
    }

    // Setter method for extracting the review details
    // and storing it in WriteReviewScreen.reviewText
    private void setReviewDetails(){
        reviewText = reviewTextField.getText().toString();
        reviewRating = (double) ratingBar.getRating();
    }

    // Method to publish WriteReviewScreen data fields to parse
    private boolean uploadToDB () {

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
        review.saveInBackground();

        return true;
    }
}
