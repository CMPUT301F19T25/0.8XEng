package com.example.emote;

import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class FriendsTest {

    private String validUserName1 = "testuser44";
    private String validUserName2 = "testuser55";
    CountingIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        // send friend request to validUserName1
        FireStoreHandler fsh = new FireStoreHandler(validUserName2);
        fsh.sendFriendRequest(validUserName1);

        EmoteApplication.setUsername(validUserName1);
        onView(withId(R.id.navigation_friends)).perform(click());

        idlingResource = EmoteApplication.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }


    @Test
    public void testFriendRequestAccept() {
        // find request
        onData(anything())
                .inAdapterView(withId(R.id.friends_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.friend_text))
                .check(matches(withText(validUserName2)));

        // accept request
        onData(anything())
                .inAdapterView(withId(R.id.friends_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.accept_follow))
                .perform(click());
    }

    @Test
    public void testFriendRequestDecline() {
        // find request
        onData(anything())
                .inAdapterView(withId(R.id.friends_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.friend_text))
                .check(matches(withText(validUserName2)));

        // accept request
        onData(anything())
                .inAdapterView(withId(R.id.friends_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.decline_follow))
                .perform(click());
    }

}
