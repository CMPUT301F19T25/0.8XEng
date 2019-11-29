package com.example.emote;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

/**
 * Test class for the Edit Emote Activty.
 * Accessed from the emote list.
 * Tests verify various functonalities
 * and correctness fo displayed event.
 */
public class EditEmoteTest {

    private String validUserName = "testuser4";
    private String CURRENT_EMOTION = "Sad";
    private String TEST_EMOTION = "Happy";
    private String CURRENT_SITUATION = "Alone";
    private String TEST_SITUATION = "A Few People";
    private String DATE = "2019/10/28";
    private String TIME = "01:16";

    CountingIdlingResource idlingResource;

    @Rule
    public IntentsTestRule<MainActivity> activityRule
            = new IntentsTestRule<>(MainActivity.class);


    @Before
    public void setup() {
        EmoteApplication.setUsername(validUserName);
        onView(withId(R.id.navigation_list_emote)).perform(click());

        idlingResource = EmoteApplication.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);

        onData(anything())
                .inAdapterView(withId(R.id.emote_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.emote_text))
                .check(matches(withText(CURRENT_EMOTION)))
                .perform(click());
    }

    @Test
    public void testCurrentEmotion() {
        onView(withId(R.id.spinnner_emote))
                .check(matches(withSpinnerText(CURRENT_EMOTION)));
    }

    @Test
    public void testEmotionSelector() {
        onView(withId(R.id.spinnner_emote)).perform(click());
        onView(withText(TEST_EMOTION)).check(matches(isDisplayed()));
    }

    @Test
    public void testCurrentSituation() {
        onView(withId(R.id.spinner_situation))
                .check(matches(withSpinnerText(containsString(CURRENT_SITUATION))));
    }

    @Test
    public void testSituationSelector() {
        onView(withId(R.id.spinner_situation)).perform(click());
        onView(withText(TEST_SITUATION)).check(matches(isDisplayed()));
    }

    @Test
    public void testDateAndTime() {
        onView(withId(R.id.text_date)).check(matches(withText(DATE)));
        onView(withId(R.id.text_time)).check(matches(withText(TIME)));
    }

    @Test
    public void testMap() {
        onView(withId(R.id.addLocationButton)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
    }


}
