package com.projectpinacolada.ucsd.projectpinacolada;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

// this is designed to test the unsuccessful creation of a review
public class WriteReviewTestFail {

    //vars to hold the user input
    String title;
    String body;
    String productUPC;

    //the rule to be used to start the app
    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule(
            ProductInfo.class, false, false);


    //set the strings for the user input
    @Before
    public void initValidString() {
        // Specify strings, both the title and body will be too short
        title = "W";  //too short
        body = "I do"; // too short
        productUPC = "883904333791"; //james bond movie
    }

    //run the tests
    @Test
    public void changeText_sameActivity() {
        //start up the product info activity
        Intent myIntent = new Intent();
        myIntent.putExtra("barcode", productUPC);
        mActivityRule.launchActivity(myIntent);

        // click on the write review button
        onView(withId(R.id.writeReviewButton)).perform(click());

        // Type the text into the email and password fields
        onView(withId(R.id.reviewTitle)).perform(typeText(title), closeSoftKeyboard());
        onView(withId(R.id.review_text)).perform(typeText(body), closeSoftKeyboard());

        // Press the submit button
        onView(withId(R.id.write_review_submit)).perform(click());

        // check that we have not moved screens. If the submit button is still present,
        // then there has been an error and the case has been properly handled
        onView(withId(R.id.write_review_submit)).check(matches(withText("Submit Review")));

    }
}
