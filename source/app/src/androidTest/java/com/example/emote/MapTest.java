package com.example.emote;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Test class for the personal and friends map activities.
 */
public class MapTest {

    private String validUserName = "testuser4";
    CountingIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        EmoteApplication.setUsername(validUserName);
        onView(withId(R.id.navigation_map)).perform(click());

        idlingResource = EmoteApplication.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);

        Intents.init();
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
        Intents.release();
    }

    @Test
    public void testPersonalMap(){
        onView(withId(R.id.personal_location_history_button)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
    }


    @Test
    public void testFriendsMap(){
        onView(withId(R.id.friends_location_history_button)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
    }

}
