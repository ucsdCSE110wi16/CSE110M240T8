package com.projectpinacolada.ucsd.projectpinacolada;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.vision.barcode.Barcode;

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

    private Intent data;        //the data that comes with the intent
    private Barcode barcode;    //barcode object
    private TextView upcCode;   //text shown to the user
    private String upcString;   //hold UPC value as a string
    private long upcInt = 0;    //hold UPC value as a long -- can not be an int, barcodes are too large

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //get the value of the text field
        upcCode = (TextView) findViewById(R.id.upcCode);
        upcString = getIntent().getStringExtra("barcode");

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("http://api.upcdatabase.org/json/53f300b1f4f72f42dc66bee14f15b7a6/" + upcString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            JSONObject jsonObject = new JSONObject(buffer.toString());
            updateUPCValue(jsonObject);

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
                if (reader != null) {
                    reader.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //change the text shown to the user
    private void updateUPCValue(JSONObject jsonObject) {

        if (jsonObject != null)
            try {
                upcCode.setText(jsonObject.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    //getter method if needed
    public long getUPC_int() {
        return upcInt;
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
