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
     * @return get the global username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Set the username for the app
     * @param username the username to set
     */
    public static void setUsername(String username) {
        EmoteApplication.username = username;
    }


    /**
     * Sets the idlingResource globally throughout the application
     * @param idlingResource: idling resource to set
     */
    public static void setIdlingResource(CountingIdlingResource idlingResource) {
        EmoteApplication.idlingResource = idlingResource;
    }

    /**
     * Get the global idling resource for the application
     * @return the idling resoure
     */
    public static CountingIdlingResource getIdlingResource() {
        if(idlingResource == null){
            idlingResource = new CountingIdlingResource("EmoteAppCounter");
        }
        return idlingResource;
    }
}
