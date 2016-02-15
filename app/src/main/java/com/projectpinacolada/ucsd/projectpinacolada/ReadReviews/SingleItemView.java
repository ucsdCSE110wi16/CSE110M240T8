package com.projectpinacolada.ucsd.projectpinacolada.ReadReviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.projectpinacolada.ucsd.projectpinacolada.R;

/**
 * Created by Darren on 2/15/2016.
 */
public class SingleItemView extends AppCompatActivity{
    // Declare Variables
    String reviewer;
    String review;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.singleitemview);

        Intent i = getIntent();
        // Get the result of reviewer
        reviewer = i.getStringExtra("reviewers");
        // Get the result of review
        review = i.getStringExtra("reviews");

        // Locate the TextViews in singleitemview.xml
        TextView txtreviewer = (TextView) findViewById(R.id.reviewer);
        TextView txtreview = (TextView) findViewById(R.id.review);

        // Set results to the TextViews
        txtreviewer.setText(reviewer);
        txtreview.setText(review);
    }
}
