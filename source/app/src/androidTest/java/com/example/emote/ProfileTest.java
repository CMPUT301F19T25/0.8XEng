package com.example.emote;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test class for the profile fragment.
 *
 * Test assumes test user 4's latest mood is HAPPY
 * and that the test user has 2 friends.
 * Verifies that the two buttons launch other
 * activities.
 */
public class ProfileTest {

    private String validUserName = "testuser4";
    private String currentMood = "SAD";
    private String friendsButton = "2 FRIENDS";

    CountingIdlingResource idlingResource;


    @Rule
    public IntentsTestRule<MainActivity> activityRule
            = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        EmoteApplication.setUsername(validUserName);
        onView(withId(R.id.navigation_profile)).perform(click());
        idlingResource = EmoteApplication.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void testUserNameDisplayed() {
        onView(withId(R.id.profile_username))
                .check(matches(withText(validUserName)));
    }

    @Test
    public void testCurrentMood() {
        onView(withId(R.id.profile_current_mood))
                .check(matches(withText(currentMood)));
    }

    @Test
    public void testNumberFriends() {
        onView(withId(R.id.profile_number_friends))
                .check(matches(withText(friendsButton)));
    }

    @Test
    public void testFriendsButton() {
        onView(withId(R.id.profile_number_friends))
                .perform(click());
        intended(hasComponent(FollowingListActivity.class.getName()));
    }

    @Test
    public void testSignOutButton() {
        onView(withId(R.id.signoutButton))
                .perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }


}
