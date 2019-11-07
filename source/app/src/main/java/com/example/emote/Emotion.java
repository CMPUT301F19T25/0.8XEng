package com.example.emote;

import android.content.Context;

public enum Emotion {
    SAD,
    HAPPY,
    LONELY,
    DEPRESSED,
    EXCITED,
    NEUTRAL,
    ANNOYED,
    STRESSED;

    public static String[] getEmotionStrings(Context context) {
        String emotions[] = new String[Emotion.values().length];
        for (int i = 0; i < Emotion.values().length; i++) {
            int identifier = context.getResources().getIdentifier(Emotion.values()[i].toString(), "string", context.getPackageName());
            emotions[i] = context.getResources().getString(identifier);
        }
        return  emotions;
    }

}
