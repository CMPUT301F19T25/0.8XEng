package com.example.emote;

import androidx.test.espresso.idling.CountingIdlingResource;

/**
 * Static class to have the username globally for the application
 */
public class EmoteApplication {
    static String username;
    static CountingIdlingResource idlingResource;

    /**
     * Get the username
     * @return
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Set the username for the app
     * @param username
     */
    public static void setUsername(String username) {
        EmoteApplication.username = username;
    }

    public static void setIdlingResource(CountingIdlingResource idlingResource) {
        EmoteApplication.idlingResource = idlingResource;
    }
    public static CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }
}
