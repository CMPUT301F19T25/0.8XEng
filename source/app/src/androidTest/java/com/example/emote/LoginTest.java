package com.example.emote;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Test class for the login activities.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    private String invalidUserName = "invalidUserName";
    private String invalidPassword = "invalid123";
    private String validUserName = "testuser4";
    private String validPassword = "password123";
    private String invalidLengthPassword = "a";


    @Rule
    public IntentsTestRule<LoginActivity> activityTestRule =
            new IntentsTestRule<>(LoginActivity.class);


    @Test
    public void unregisiteredUserInvlaidLogin() {
        onView(withId(R.id.input_username))
                .perform(typeText(invalidUserName), closeSoftKeyboard());
        onView(withId(R.id.input_password))
                .perform(typeText(invalidPassword), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
    }

    @Test
    public void invlaidUserNameLengthLogin() {
        onView(withId(R.id.input_username))
                .perform(typeText(invalidUserName), closeSoftKeyboard());
        onView(withId(R.id.input_password))
                .perform(typeText(invalidLengthPassword), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
    }

    @Test
    public void validLogin() {
        onView(withId(R.id.input_username))
                .perform(typeText(validUserName), closeSoftKeyboard());
        onView(withId(R.id.input_password))
                .perform(typeText(validPassword), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
    }

}
