package com.projectpinacolada.ucsd.projectpinacolada;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

//unit test to test the login feature
@RunWith(AndroidJUnit4.class)
public class LoginTestPass {

    //vars to hold the user input
    String username;
    String password;

    //the rule to be used to start the app
    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule(
            SplashScreen.class);


    //set the strings
    @Before
    public void initValidString() {
        // Specify a valid string.
        username = "foo@bar.com";
        password = "foobar";
    }
    
    //run the tests
    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.email)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());

        // Press sign in
        onView(withId(R.id.email_sign_in_button)).perform(click());

        // Go to user profile
        onView(withId(R.id.userButton)).perform(click());

        // Check that the user name is correct
        onView(withId(R.id.user_name)).check(matches(withText("Daniel")));
    }

    //logout after the test completes
    @After
    public void logout() {
        onView(withId(R.id.logout_button)).perform(click());
    }
}






