package com.example.emote;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the EmoteApplication class.
 */
public class EmoteApplicationTest {
    @Test
    public void setUsernameIsCorrect() {
        EmoteApplication emoteApplication = new EmoteApplication();
        emoteApplication.setUsername("testuser");
        assertEquals("testuser", emoteApplication.getUsername());
    }

}
