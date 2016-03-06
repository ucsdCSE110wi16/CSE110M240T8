package com.projectpinacolada.ucsd.projectpinacolada;

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

// unit test to test the login feature
// this test is designed to show invalid credentials will not be able to login
public class LoginTestFail {


    //vars to hold the invalid username/password
    String username;
    String password;

    //the rule to be used to start the app
    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule(
            SplashScreen.class);


    //set the strings for the user input
    @Before
    public void initStrings() {
        // some crazy strings which are not associated with a user
        username = "abcdefg1234567test@thisisonlyatest.com";
        password = "mypassowrddoesnotexist1234";
    }

    //run the tests
    @Test
    public void testUnsuccessfulLogin() {

        // Type the text into the email and password fields
        onView(withId(R.id.email)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());

        // Press sign in
        onView(withId(R.id.email_sign_in_button)).perform(click());

        // check to see if we are still in the login screen
        // if so, then we have passed this test
        onView(withId(R.id.email_sign_in_button)).check(matches(withText("Sign in")));
    }

}
