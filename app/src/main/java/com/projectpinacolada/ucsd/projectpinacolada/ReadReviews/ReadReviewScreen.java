package com.projectpinacolada.ucsd.projectpinacolada.ReadReviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.projectpinacolada.ucsd.projectpinacolada.R;

public class ReadReviewScreen extends AppCompatActivity {

    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<Reviews> reviewsList = null;
    // long upc = 37000424314;
    int reviewRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_review_screen);
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ReadReviewScreen.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Read Reviews");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            reviewsList = new ArrayList<Reviews>();
            try {
                // Locate the class table named "Review" in Parse.com
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");

                // Pulls reviews of the specific product
                Intent i = getIntent();

                // Query Parse
                // Locate the column named "createdAt" in Parse.com and order list by ascending
                query.whereEqualTo("upcCode", Long.valueOf(i.getStringExtra("upcCode")));
                query.orderByAscending("createdAt");
                ob = query.find();

                // Add view map for each review in the parse object
                for (ParseObject Review : ob) {
                    Reviews map = new Reviews();
                    map.setReviewers((String) Review.get("reviewer"));
                    map.setReviews((String) Review.get("reviewText"));
                    map.setReviewRating ((double) Review.getDouble("rating"));
                    reviewsList.add(map);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(ReadReviewScreen.this,
                    reviewsList);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
}
