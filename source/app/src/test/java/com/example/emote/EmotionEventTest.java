package com.example.emote;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the EmotionEvent class.
 */
public class EmotionEventTest {

    private EmotionEvent emotionEvent;

    @Before
    public void SetUp(){
        Date date = new Date(2010, 5, 12);
        emotionEvent = new EmotionEvent(Emotion.HAPPY, Situation.CROWD, "Festival", date);
    }

    @Test
    public void getSituationIsCorrect() {
        assertEquals(Emotion.HAPPY, emotionEvent.getEmote());
    }

    @Test
    public void getReasonIsCorrect() {
        assertEquals("Festival", emotionEvent.getReason());
    }

    @Test(expected = IllegalArgumentException.class)
    public void longReasonThrowsIllegalArgumentException() {
        String reason = "veryveryveryveryveryveryveryveryveryverylongreason";
        emotionEvent.setReason(reason);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fourWordsReasonThrowsIllegalArgumentException() {
        String reason = "four different words reason";
        emotionEvent.setReason(reason);
    }

    @Test
    public void getDateIsCorrect() {
        assertEquals(new Date(2010, 5, 12), emotionEvent.getDate());
    }

    @Test
    public void getFireStoreDocumentIDIsCorrect() {
        assertEquals(36, emotionEvent.getFireStoreDocumentID().length());
    }

}
