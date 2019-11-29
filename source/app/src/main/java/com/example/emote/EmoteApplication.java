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
<<<<<<< Updated upstream

    public static void setIdlingResource(CountingIdlingResource idlingResource) {
        EmoteApplication.idlingResource = idlingResource;
    }
=======
<<<<<<< Updated upstream
=======

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
>>>>>>> Stashed changes
    public static CountingIdlingResource getIdlingResource() {
        if(idlingResource == null){
            idlingResource = new CountingIdlingResource("EmoteAppCounter");
        }
        return idlingResource;
    }
<<<<<<< Updated upstream
=======
>>>>>>> Stashed changes
>>>>>>> Stashed changes
}
