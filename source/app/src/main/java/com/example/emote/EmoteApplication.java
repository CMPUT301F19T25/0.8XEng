package com.example.emote;

/**
 * Static class to have the username globally for the application
 */
public class EmoteApplication {
    static String username;

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
}
