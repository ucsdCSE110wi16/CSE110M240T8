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

//unit test to test the login feature
// this test is designed to pass
@RunWith(AndroidJUnit4.class)
public class LoginTestPass {

    //vars to hold the user input
    String username;
    String password;

    //the rule to be used to start the app
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(
            LoginActivity.class);


    //set the strings for the user input
    @Before
    public void initValidString() {
        // Specify valid strings
        username = "foo@bar.com";
        password = "foobar";
    }
    
    //run the tests
    @Test
    public void changeText_sameActivity() {
        // Type the text into the email and password fields
        onView(withId(R.id.email)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());

        // Press sign in
        onView(withId(R.id.email_sign_in_button)).perform(click());

        // Go to user profile
        onView(withId(R.id.userButton)).perform(click());

        // Check that the user name is correct, if so, we are done
        onView(withId(R.id.user_name)).check(matches(withText("Daniel")));
    }

    //logout after the test completes
    @After
    public void logout() {
        //we are already on the user profile screen, so the logout button is present
        onView(withId(R.id.logout_button)).perform(click());
    }
}






