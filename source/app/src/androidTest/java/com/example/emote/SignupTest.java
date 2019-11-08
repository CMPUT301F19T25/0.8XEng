package com.example.emote;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Test class for the Signup activity.
 */
public class SignupTest {

    private String invalidUserName = "invalidUserName";
    private String invalidPassword = "invalid123";
    private String validUserName = "testuser4";
    private String validPassword = "password123";
    private String invalidLengthPassword = "a";


    @Rule
    public IntentsTestRule<SignupActivity> activityTestRule =
            new IntentsTestRule<>(SignupActivity.class);

    @Test
    public void invlaidUserNameLengthSignup() {
        onView(withId(R.id.signup_username))
                .perform(typeText(invalidUserName), closeSoftKeyboard());
        onView(withId(R.id.signup_password))
                .perform(typeText(invalidLengthPassword), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_password))
                .perform(typeText(invalidLengthPassword), closeSoftKeyboard());
        onView(withId(R.id.btn_create_account)).perform(click());
    }

    @Test
    public void passwordsNotMatchingSignup() {
        onView(withId(R.id.signup_username))
                .perform(typeText(validUserName), closeSoftKeyboard());
        onView(withId(R.id.signup_password))
                .perform(typeText(validPassword), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_password))
                .perform(typeText(invalidPassword), closeSoftKeyboard());
        onView(withId(R.id.btn_create_account)).perform(click());
    }
}
