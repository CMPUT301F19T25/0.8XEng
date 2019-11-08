package com.example.emote;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SituationTest {

    @Test
    public void situationGetIndexCorrect() {
        Situation s = Situation.ALONE;
        assertEquals(0, Situation.getIndex(s));

        Situation s2 = Situation.CROWD;
        assertEquals(3, Situation.getIndex(s2));
    }

}
