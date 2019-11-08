package com.example.emote;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Test class for the add emotion fragment.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddEmoteTest {

    private String validUserName = "testuser4";


    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
     EmoteApplication.setUsername(validUserName);
    }

    @Test
    public void testReasonField(){
        onView(withId(R.id.text_reason_field))
                .perform(typeText("I saw a puppy!"), closeSoftKeyboard())
        .check(matches(withText("I saw a puppy!")));
    }

    @Test
    public void testDateField(){
        onView(withId(R.id.text_date_field)).perform(click());
        onView(withText("Cancel")).check(matches(isDisplayed()));
    }

    @Test
    public void testTimeField(){
        onView(withId(R.id.text_time_field)).perform(click());
        onView(withText("Cancel")).check(matches(isDisplayed()));
    }

    @Test
    public void testEmotionField(){
        onView(withId(R.id.spinnner_emote)).perform(click());
        onView(withText("Happy")).check(matches(isDisplayed()));
    }

    @Test
    public void testSituationField(){
        onView(withId(R.id.spinner_situation)).perform(click());
        onView(withText("Alone")).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidFieldsAddEmotion(){
        onView(withId(R.id.add_emote_scroll_view))
                .perform(swipeUp());
        onView(withId(R.id.submitButton)).perform(click());
        onView(withText("Error: length=1; index=2"))
                .inRoot(withDecorView(not(is(activityRule.getActivity()
                        .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

}
