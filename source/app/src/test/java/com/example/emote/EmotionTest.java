package com.example.emote;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the emotion class.
 */
public class EmotionTest {

    @Test
    public void getIndexIsCorrect() {
        Emotion emote = Emotion.HAPPY;
        assertEquals(1, emote.getIndex(emote));
    }
}
