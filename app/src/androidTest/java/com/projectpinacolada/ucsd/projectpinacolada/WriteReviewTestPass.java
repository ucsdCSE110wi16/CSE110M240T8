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


// this test is designed to test the successful creation of a review
public class WriteReviewTestPass {

    //vars to hold the user input
    String title;
    String body;
    String productUPC;
    String username;
    String password;

    //the rule to be used to start the app
    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule(
            ProductInfo.class, false, false);


    //set the strings for the user input
    @Before
    public void initValidString() {
        // Specify valid strings
        title = "This is the best movie out there!";
        body = "I do not know how I have gone so long without this movie. It has literally saved my life!";
        productUPC = "883904333791"; //james bond movie
        username = "x@y.com";
        password = "xyxyx";

        // Sign In
        ActivityTestRule<LoginActivity> mActivityRule2 = new ActivityTestRule(LoginActivity.class, false, false);
        Intent intent = new Intent();
        mActivityRule2.launchActivity(intent);
        onView(withId(R.id.email)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());

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

        // Type the text into the fields
        onView(withId(R.id.reviewTitle)).perform(typeText(title), closeSoftKeyboard());
        onView(withId(R.id.review_text)).perform(typeText(body), closeSoftKeyboard());

        // Press the submit button
        onView(withId(R.id.write_review_submit)).perform(click());

        // check that we have returned back to the product info page
        // this means that we have successfully submitted a review
        onView(withId(R.id.writeReviewButton)).check(matches(withText("Write Review")));

    }

}
