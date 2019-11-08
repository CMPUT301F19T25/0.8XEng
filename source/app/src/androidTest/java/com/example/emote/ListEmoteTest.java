package com.example.emote;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ListEmoteTest {

    private String validUserName = "testuser4";

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){

        EmoteApplication.setUsername(validUserName);
        onView(withId(R.id.navigation_profile)).perform(click());
    }

    @Test
    public void testUserNameDisplayed(){
        onView(withId(R.id.profile_username))
                .check(matches(withText(validUserName)));
    }

}
