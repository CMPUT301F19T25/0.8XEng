package com.example.emote;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test class for the list emotion fragment.
 */
public class ListEmoteTest {

    private String validUserName = "testuser4";

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){

        EmoteApplication.setUsername(validUserName);
        onView(withId(R.id.navigation_list_emote)).perform(click());
    }

    @Test
    public void testShowFriendsButton(){
        onView(withId(R.id.check_box_show_friends)).perform(click());
    }


    @Test
    public void testFilterSelector(){
        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Happy")).check(matches(isDisplayed()));
    }

    @Test
    public void testFilterCheckBox(){
        onView(withId(R.id.check_box_filter)).perform(click());
    }

}
