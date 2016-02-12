package com.projectpinacolada.ucsd.projectpinacolada;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProductInfo extends AppCompatActivity {

    private TextView productNameTV;             // Text box displaying product name.
    private TextView productDescriptionTV;      // Text box displaying product description.
    private String upcCode;                     // UPC code as string.

    //Button load_img;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        img = (ImageView)findViewById(R.id.img);

        // Sets thread policy to allow for network traffic to run in the main thread.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Initializing barcode information as TextView.
        productNameTV = (TextView) findViewById(R.id.productName);
        productDescriptionTV = (TextView) findViewById(R.id.productDescription);
        upcCode = getIntent().getStringExtra("barcode");
        // Establishing connection to Walmart API.
        establishConnection();

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

            }else{
                // error handling
                pDialog.dismiss();
                //Toast.makeText(ProductInfo.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }



    /**
     * Establishes connection with endpoint to acquire product description.
     */
    public void establishConnection() {

        // IMPORTANT: Do not move. Must remain outside try block for proper scoping with finally block.
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Dynamically-generated URL for product description.
            URL url = new URL("http://api.walmartlabs.com/v1/items?apiKey=qxqmszsqwghafeaax6ug77k7&upc=" + upcCode);

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

            // Adding input data as string to JSON object.
            JSONObject jsonObject = new JSONObject(buffer.toString());
            updateProductNameAndDescription(jsonObject);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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
    }

    /**
     * Sets product name and description TextViews from input as JSON object.
     *
     * @param jsonObject - JSON object to parse to set product name TextView.
     */
    private void updateProductNameAndDescription(JSONObject jsonObject) {

        JSONArray jsonArray = null;

        try {
            jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject currentObject = jsonArray.getJSONObject(i);
                    productNameTV.setText(currentObject.getString("name"));
                    productDescriptionTV.setText(currentObject.getString("shortDescription"));
                    new LoadImage().execute(currentObject.getString("largeImage"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Moves user from ProductInfo activity to ReadReviewScreen activity.
    public void readReviewsButtonClicked(View view) {

        Intent intent = new Intent(this, ReadReviewScreen.class);
        startActivity(intent);
    }

    // Moves user from ProductInfo activity to WriteReviewScreen activity.
    public void writeReviewButtonClicked(View view) {

        Intent intent = new Intent(this, WriteReviewScreen.class);
        startActivity(intent);
    }




}
