package com.example.emote;

import android.content.ComponentName;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginSignupTest {

    private String invalidUserName = "invalidUserName";
    private String invalidPassword = "invalid123";
    private String validUserName = "testuser";
    private String validPassword = "password123";


    @Rule
    public IntentsTestRule<LoginActivity> mLoginActivityActivityTestRule =
            new IntentsTestRule<>(LoginActivity.class);



    @Test
    public void invlaidLogin() {
        onView(withId(R.id.input_username))
                .perform(typeText(invalidUserName), closeSoftKeyboard());
        onView(withId(R.id.input_password))
                .perform(typeText(invalidPassword), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

    }

    @Test
    public void validLogin() {

        onView(withId(R.id.input_username))
                .perform(typeText(validUserName), closeSoftKeyboard());
        onView(withId(R.id.input_password))
                .perform(typeText(validPassword), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

                // Check that the text was changed.
//        onView(withId(R.id.))
//                .check(matches(withText("")));

        intended(hasComponent(LoginActivity.class.getName()));



    }

}
