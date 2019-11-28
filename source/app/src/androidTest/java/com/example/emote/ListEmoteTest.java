package com.example.emote;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Test class for the list emotion fragment.
 *
 * Test assumes that there are two users testuser4 and testuser5
 * Testuser4 has two mood events Happy and Sad
 * Testuser5 has two mood events Excited and Depressed
 */
public class ListEmoteTest {

    private String validUserName = "testuser4";
    CountingIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        EmoteApplication.setUsername(validUserName);
        onView(withId(R.id.navigation_list_emote)).perform(click());

        idlingResource = EmoteApplication.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void testShowFriendsButton(){
        onData(anything())
                .inAdapterView(withId(R.id.emote_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.emote_text))
                .check(matches(withText("Happy")));

        onData(anything())
                .inAdapterView(withId(R.id.emote_list_view))
                .atPosition(1)
                .onChildView(withId(R.id.emote_text))
                .check(matches(withText("Sad")));

        onView(withId(R.id.check_box_show_friends)).perform(click());
    }


    @Test
    public void testFilterSelector(){
        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Happy")).check(matches(isDisplayed()));
    }

    @Test
    public void testFilterHappy(){
        onData(anything())
                .inAdapterView(withId(R.id.emote_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.emote_text))
                .check(matches(withText("Happy")));

        onData(anything())
                .inAdapterView(withId(R.id.emote_list_view))
                .atPosition(1)
                .onChildView(withId(R.id.emote_text))
                .check(matches(withText("Sad")));


        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Happy"))).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Happy"))));

        onData(anything())
                .inAdapterView(withId(R.id.emote_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.emote_text))
                .check(matches(not(withText("Sad"))));

    }

}
