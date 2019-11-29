package com.example.emote;

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
import static org.hamcrest.Matchers.hasToString;

public class FriendsTest {

    private String senderUsername = "testsend";
    private String receiverUsername = "testreceive";

    CountingIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    // send request
    @Test
    public void testSendRequest() {
        EmoteApplication.setUsername(senderUsername);
        onView(withId(R.id.navigation_friends)).perform(click());
        idlingResource = EmoteApplication.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
        onView(withId(R.id.auto_complete_friends)).perform(typeText(receiverUsername), closeSoftKeyboard());
        onData(anything())
                .inAdapterView(withId(R.id.auto_complete_friends))
                .atPosition(0)
                .onChildView(withId(R.id.friend_text))
                .check(matches(withText(receiverUsername)));
        onData(anything())
                .inAdapterView(withId(R.id.auto_complete_friends))
                .atPosition(0)
                .onChildView(withId(R.id.button_send))
                .perform(click());
        onData(anything())
                .inAdapterView(withId(R.id.auto_complete_friends))
                .atPosition(0)
                .onChildView(withId(R.id.button_send))
                .check(matches(withText("REQUEST SENT")));
    }

    // receive request
    @Test
    public void testReceiveRequest() {
        EmoteApplication.setUsername(receiverUsername);
        idlingResource = EmoteApplication.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
        onView(withId(R.id.navigation_friends)).perform(click());
        onData(anything())
                .inAdapterView(withId(R.id.friends_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.decline_follow))
                .perform(click());
    }

}
