package com.example.emote;

//This is just an example of the emotional event class in order to get firestore setup.
//Additional data fields and handling should be added.

import com.example.emote.Situation;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * This is a class that defines an EmotionEvent
 */
public class EmotionEvent implements Serializable {

    public static final String USERNAME_KEY = "username";

    private Emotion emotion;
    private Situation situation;
    private String reason;
    private String username;
    private String imageFile;

    private Date date;
    private String fireStoreDocumentID;

    // workaround to allow the class to be serializable
    // otherwise geopoint will trigger error when passed in bundle
    private double lat = -1;
    private double lng = -1;

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

    public EmotionEvent(Emotion emote, Situation situation, String reason, Date date, String imageFile){
        setUsername(username);
        setEmote(emote);
        setSituation(situation);
        setReason(reason);
        setImageFile(imageFile);
        setDate(date);
        fireStoreDocumentID = UUID.randomUUID().toString();
    }

    public EmotionEvent(Emotion emote, Situation situation, String reason, Date date, GeoPoint location){
        setUsername(username);
        setEmote(emote);
        setSituation(situation);
        setReason(reason);
        setLocation(location);
        setDate(date);
        fireStoreDocumentID = UUID.randomUUID().toString();
    }

    public EmotionEvent(Emotion emote, Situation situation, String reason, Date date, String imageFile, GeoPoint location){
        setUsername(username);
        setEmote(emote);
        setSituation(situation);
        setReason(reason);
        setImageFile(imageFile);
        setDate(date);
        setLocation(location);
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

    public void setImageFile(String imageFile) { this.imageFile = imageFile; }

    public String getImageFile() { return imageFile; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public GeoPoint getLocation() {
        if (lat == -1 || lng == -1) {
            return null;
        }
        else {
            return new GeoPoint(lat, lng);
        }
    }

    public void setLocation(GeoPoint location) {
        if (location == null) {
            lat = -1;
            lng = -1;
        }
        else {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }
}
