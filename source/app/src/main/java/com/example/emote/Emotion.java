package com.example.emote;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * An Enum used to define emotions
 */
public enum Emotion {
    SAD,
    HAPPY,
    LONELY,
    DEPRESSED,
    EXCITED,
    NEUTRAL,
    ANNOYED,
    STRESSED;

    /**
     * Get all the associated emotion strings for the spinner
     * @param context
     * @return
     *  An array of strings containing all the emotion strings
     */
    public static String[] getStrings(Context context) {
        String emotions[] = new String[Emotion.values().length];
        for (int i = 0; i < Emotion.values().length; i++) {
            int identifier = context.getResources().getIdentifier(Emotion.values()[i].toString(), "string", context.getPackageName());
            emotions[i] = context.getResources().getString(identifier);
        }
        return  emotions;
    }

    /**
     * Get the colour of an emotion
     * @param context
     * @param emotion
     * @return
     *  An int indicating the colour of an emotion.
     */
    public static int getColor(Context context, Emotion emotion){
        int identifier = context.getResources().getIdentifier(emotion.toString(), "color", context.getPackageName());
        return context.getResources().getColor(identifier);
    }

    /**
     * Get the emoticon for that specific emotion
     * @param context Application's context
     * @param emotion The emoticon to get
     * @return the emoji path
     */
    public static int getEmoticonPath(Context context, Emotion emotion){
        int identifier = context.getResources().getIdentifier(emotion.toString(), "color", context.getPackageName());
        return context.getResources().getColor(identifier);
    }

    /**
     * get the index for that emotion
     * @param emotion the index to get for the emotion
     * @return the index for the emotion
     */
    public static int getIndex(Emotion emotion){
        return Arrays.asList(Emotion.values()).indexOf(emotion);
    }

}
