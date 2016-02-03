package com.projectpinacolada.ucsd.projectpinacolada;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.vision.barcode.Barcode;

public class ProductInfo extends AppCompatActivity {

    private Intent data;        //the data that comes with the intent
    private Barcode barcode;    //barcode object
    private TextView UPCCode;   //text shown to the user
    private String UPC_string;  //hold UPC value as a string
    private long UPC_int = 0;   //hold UPC value as a long -- can not be an int, barcodes are too large

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        //get the value of the text field
        UPCCode = (TextView) findViewById(R.id.upcCode);

        //get the intent
        data = getIntent();
        //get the value of the barcode
        parseData();
        //update the text field to the user
        updateUPCValue();
    }

    //will take the data from the barcode object and convert to a string and long
    private void parseData() {

        //get the barcode data from the intent
        barcode = data.getParcelableExtra(CameraActivity.BarcodeObject);
        //parse the string from the barcode
        UPC_string = barcode.displayValue;
        //parse a long from the barcode
        UPC_int = Long.parseLong(UPC_string);

    }

    //change the text shown to the user
    private void updateUPCValue() {

        UPCCode.setText(UPC_string);

    }

    //getter method if needed
    public long getUPC_int() {
        return UPC_int;
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
