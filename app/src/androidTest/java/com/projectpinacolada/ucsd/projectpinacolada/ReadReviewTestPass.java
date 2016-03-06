package com.projectpinacolada.ucsd.projectpinacolada;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

// this is designed to test the reading of a review
public class ReadReviewTestPass {

    //vars to hold the expected input
    String title;
    String body;
    String productUPC;

    //the rule to be used to start the app
    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule(
            ProductInfo.class, false, false);


    //set the strings for the expected values
    @Before
    public void setValues() {
        // Specify expected values
        title = "I love horse camp!";
        body = "horse camp is my favorite!";
        productUPC = "796019829038"; //horse camp
    }

    //run the tests
    @Test
    public void checkReview() {
        //start up the product info activity
        Intent myIntent = new Intent();
        myIntent.putExtra("barcode", productUPC);
        mActivityRule.launchActivity(myIntent);

        // click on the read review button
        onView(withId(R.id.readReviewButton)).perform(click());

        // check the first review in the list to see if it matches
        onView(withId(R.id.reviewTitle)).check(matches(withText(title)));
        onView(withId(R.id.review)).check(matches(withText(body)));

        //enter into the single review screen
        onView(withId(R.id.reviewTitle)).perform(click());

        //check that the review text is the same for this screen
        onView(withId(R.id.review)).check(matches(withText(body)));
    }
}
