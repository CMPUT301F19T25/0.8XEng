package com.example.emote;

//This is just an example of the emotional event class in order to get firestore setup.
//Additional data fields and handling should be added.

import com.example.emote.Situation;

import java.util.UUID;

public class EmotionEvent {

    public static final String USERNAME_KEY = "username";

    private String emote;
    private Situation situation;
    private String reason;
    private String username;
    private String fireStoreDocumentID;

    public EmotionEvent(){}

    public EmotionEvent(String emote, Situation situation, String reason){
        setUsername(username);
        setEmote(emote);
        setSituation(situation);
        setReason(reason);
        fireStoreDocumentID = UUID.randomUUID().toString();
    }

    public String getEmote() {
        return emote;
    }

    public void setEmote(String emote) {
        this.emote = emote;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFireStoreDocumentID() {
        return fireStoreDocumentID;
    }

}
