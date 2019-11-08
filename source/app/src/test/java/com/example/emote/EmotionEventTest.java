package com.example.emote;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void getDateIsCorrect() {
        assertEquals(new Date(2010, 5, 12), emotionEvent.getDate());
    }

    @Test
    public void getFireStoreDocumentIDIsCorrect() {
        assertEquals(36, emotionEvent.getFireStoreDocumentID().length());
    }

}
