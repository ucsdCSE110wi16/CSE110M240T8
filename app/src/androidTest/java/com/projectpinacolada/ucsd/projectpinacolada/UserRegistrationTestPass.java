package com.projectpinacolada.ucsd.projectpinacolada;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



/**
 * Created by Darren on 3/4/2016.
 */
public class UserRegistrationTestPass {
    // Maximum for random.
    private int LARGE_RANDOM = 10000;
    private int SMALL_RANDOM = 50;

    //vars to hold the user input
    String name;
    String username;
    String password;
    String location;

    //the rule to be used to start the app
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(
            LoginActivity.class);


    //set the strings
    @Before
    public void initValidString() {
        Random numGenerator = new Random();

        // Specify a valid string.
        name = "ReviewNinjaTest" + numGenerator.nextInt(SMALL_RANDOM);
        username = numGenerator.nextInt(LARGE_RANDOM) + "foo@bar.com";
        password = numGenerator.nextInt(LARGE_RANDOM) + "abcd";
        location = "La Jolla" + numGenerator.nextInt(SMALL_RANDOM);
    }


    //run the tests
    @Test
    public void testReigster() {
        // Enter new username / password on login page and click register
        onView(withId(R.id.email)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());

        // Check that Email / Password has been imported from login page correctly.
        onView(withId(R.id.emailField)).check(matches(withText(username)));
        onView(withId(R.id.passwordField)).check(matches(withText(password)));

        // Enter the rest of the fields on registration field
        onView(withId(R.id.nameField)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.verifyPasswordField)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.locationField)).perform(typeText(location), closeSoftKeyboard());

        // Press register
        onView(withId(R.id.registerButton)).perform(click());

        // Go to user profile
        onView(withId(R.id.userButton)).perform(click());

        // Check that the user name and location is correct
        onView(withId(R.id.user_name)).check(matches(withText(name)));
        onView(withId(R.id.user_location)).check(matches(withText(location)));
    }

    // logout after the test completes
    @After
    public void logout() {
        onView(withId(R.id.logout_button)).perform(click());
    }
}
