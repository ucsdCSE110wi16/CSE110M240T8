package com.projectpinacolada.ucsd.projectpinacolada;

/**
 * Created by danielreznikov on 3/2/16.
 *
 * This test will load a product info page
 * and verify that the correct product details are present.
 */

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ProductInfoPageTestPass {

    //vars to hold the user input
    String productUPC;
    String productName;

    //the rule to be used to load product details
    @Rule
    public ActivityTestRule<ProductInfo> mActivityRule = new ActivityTestRule(
                                                             ProductInfo.class, false, false);

    //set the strings
    @Before
    public void setVars () {
        productUPC = "037000424314";
        productName = "Olay Ultra Moisture Body Wash with Shea Butter, 23.6 fl oz";

    }

    //run the tests
    @Test
    public void setupTest () {
        Intent myIntent = new Intent();
        myIntent.putExtra("barcode", productUPC);
        mActivityRule.launchActivity(myIntent);

        //Mockito

        onView(withId(R.id.productName)).check(matches(withText(productName)));
    }

    // @After
}