package com.projectpinacolada.ucsd.projectpinacolada;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.projectpinacolada.ucsd.projectpinacolada.ReadReviews.ReadReviewScreen;
import com.projectpinacolada.ucsd.projectpinacolada.ReadReviews.Reviews;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ProductInfo extends AppCompatActivity {

    private final String walmartApiUrl = "http://api.walmartlabs.com/v1/items?apiKey=qxqmszsqwghafeaax6ug77k7&upc=";
    private TextView productNameTV;             // Text box displaying product name.
    private TextView productDescriptionTV;      // Text box displaying product description.
    private RatingBar averageRatingBar;         // Rating bar displaying average rating.
    private String upcCode;                     // UPC code as string.
    private String productName;                 // Product name as string.

    //Button load_img;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        // Initializing barcode information as TextView.
        productNameTV = (TextView) findViewById(R.id.productName);
        productDescriptionTV = (TextView) findViewById(R.id.productDescription);
        img = (ImageView) findViewById(R.id.img);
        averageRatingBar = (RatingBar) findViewById(R.id.averageRating);
        upcCode = getIntent().getStringExtra("barcode");

        // Creates new thread for network connection and updates product name and description TVs.
        new SetProductInfoFromUrl().execute(walmartApiUrl);

    }

    // Handles loading an image from a url
    public class LoadImage extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            // display a progress dialog window for loading image
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductInfo.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            // set image if img object not null
            if(image != null){
                img.setImageBitmap(image);
                pDialog.dismiss();

            } else {
                // error handling
                pDialog.dismiss();
                //Toast.makeText(ProductInfo.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    /**
     * Establishes connection with endpoint and updates product name and description TextViews.
     */
    private class SetProductInfoFromUrl extends AsyncTask<String, Void, JSONObject> {

        protected void onPostExecute(JSONObject productNameAndDescription) {

            if (productNameAndDescription != null)
                updateProductInfo(productNameAndDescription);
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            // IMPORTANT: Do not move. Must remain outside try block for proper scoping with finally block.
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            JSONObject productNameAndDescription = null;

            try {

                // Dynamically-generated URL for product description.
                URL url = new URL(args[0] + upcCode);

                Log.e("URL", url.toString());

                // Opening connection.
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Creating input streams.
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                // String buffer to parse from input stream.
                StringBuffer buffer = new StringBuffer();
                String line = "";

                // Adding entire input from input stream to string buffer.
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                productNameAndDescription = new JSONObject(buffer.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("URL", "Bad URL");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IO", "Bad IO");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", "Bad JSON");
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return productNameAndDescription;
        }
    }

    /**
     * Sets product name and description TextViews from input as JSON object.
     *
     * @param jsonObject - JSON object to parse to set product name TextView.
     */
    private void updateProductInfo(JSONObject jsonObject) {

        JSONArray jsonArray = null;

        try {
            jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject currentObject = jsonArray.getJSONObject(i);
                    productName = currentObject.getString("name");
                    productNameTV.setText(productName);
                    productDescriptionTV.setText(currentObject.getString("shortDescription"));
                    new LoadImage().execute(currentObject.getString("largeImage"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set average rating.
        averageRatingBar.setRating((float) getAverageRating(upcCode));
    }

    // Takes UPC code and computes average ratings for our product
    private double getAverageRating(String upcCode) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.whereEqualTo("upcCode", Long.valueOf(upcCode));

        List<ParseObject> ob = null;
        try {
            ob = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Add view map for each review in the parse object
        double cumSum = 0;
        int numRatings = 0;
        for (ParseObject Review : ob) {
            Reviews allRatings = new Reviews();
            cumSum += Review.getDouble("rating");
            ++numRatings;
        }
        return cumSum / numRatings;
    }

    // Moves user from ProductInfo activity to ReadReviewScreen activity.
    public void readReviewsButtonClicked(View view) {

        Intent intent = new Intent(this, ReadReviewScreen.class);
        // Send product UPC to the write activity
        intent.putExtra("upcCode",upcCode);
        intent.putExtra("productName", productName);

        startActivity(intent);
    }

    // Moves user from ProductInfo activity to WriteReviewScreen activity.
    public void writeReviewButtonClicked(View view) {

        Intent intent = new Intent(this, WriteReviewScreen.class);

        // Send product UPC to the write activity
        intent.putExtra("upcCode",upcCode);
        intent.putExtra("productName", productName);

        startActivity(intent);
    }

}
