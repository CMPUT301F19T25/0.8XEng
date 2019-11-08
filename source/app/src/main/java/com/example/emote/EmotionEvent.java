package com.example.emote;

//This is just an example of the emotional event class in order to get firestore setup.
//Additional data fields and handling should be added.

import com.example.emote.Situation;

import java.util.Date;
import java.util.UUID;

/**
 * This is a class that defines an EmotionEvent
 */
public class EmotionEvent {

    public static final String USERNAME_KEY = "username";

    private Emotion emotion;
    private Situation situation;
    private String reason;
    private String username;

    private Date date;
    private String fireStoreDocumentID;

    public EmotionEvent(){}


    /**
     * The constructor for the EmotionEvent
     * @param emote
     *   The emote for the event
     * @param situation
     *   The social situation
     * @param reason
     *   The reason for the event
     * @param date
     *   The Date of the event
     */
    public EmotionEvent(Emotion emote, Situation situation, String reason, Date date){
        setUsername(username);
        setEmote(emote);
        setSituation(situation);
        setReason(reason);
        setDate(date);
        fireStoreDocumentID = UUID.randomUUID().toString();
    }

    public Emotion getEmote() {
        return this.emotion;
    }

    public void setEmote(Emotion emote) {
        this.emotion = emote;
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

    /**
     * Gets the fireStoreDocumentID
     * @return
     *   A string containing fireStoreDocumentID
     */
    public String getFireStoreDocumentID() {
        return fireStoreDocumentID;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

}
