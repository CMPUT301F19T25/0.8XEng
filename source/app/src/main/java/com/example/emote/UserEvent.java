package com.example.emote;

import java.io.Serializable;
import java.util.Date;

public class UserEvent implements Serializable {
    private String username;
    private Emotion current_mood;
    private Date date;

    public UserEvent(String username, Emotion current_mood, Date date) {
        this.username = username;
        this.current_mood = current_mood;
        this.date = date;
    }

    void setUsername(String username) {
        this.username = username;
    }

    void setCurrent_mood(Emotion current_mood) {
        this.current_mood = current_mood;
    }

    void setDate(Date date) {
        this.date = date;
    }

    String getUsername() {
        return this.username;
    }

    Emotion getCurrent_mood() {
        return this.current_mood;
    }

    Date getDate() {
        return this.date;
    }
}
