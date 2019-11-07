package com.example.emote;

import android.content.Context;

public enum Situation {
    ALONE,
    ONE_PERSON,
    FEW_PEOPLE,
    CROWD;

    public static String[] getStrings(Context context) {
        String situations[] = new String[Situation.values().length];
        for (int i = 0; i < Situation.values().length; i++) {
            int identifier = context.getResources().getIdentifier(Situation.values()[i].toString(), "string", context.getPackageName());
            situations[i] = context.getResources().getString(identifier);
        }
        return  situations;
    }
}
