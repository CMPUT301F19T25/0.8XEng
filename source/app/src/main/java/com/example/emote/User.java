//package com.example.emote;
//
//import java.util.ArrayList;
//
//public class User {
//    private String email;
//    private String username;
//    private ArrayList<String> followers;
//    private ArrayList<String> following;
//    private ArrayList<String> followingRequests;
//    private ArrayList<String> followerRequests;
//
//    public User(String username) {
//        this.username = username;
//        FireStoreHandler.setupUser(username);
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public ArrayList<String> getFollowers() {
//        return followers;
//    }
//
//    public void setFollowers(ArrayList<String> followers) {
//        this.followers = followers;
//    }
//
//    public ArrayList<String> getFollowing() {
//        return following;
//    }
//
//    public void setFollowing(ArrayList<String> following) {
//        this.following = following;
//    }
//
//    public ArrayList<String> getFollowingRequests() {
//        return followingRequests;
//    }
//
//    public void setFollowingRequests(ArrayList<String> followingRequests) {
//        this.followingRequests = followingRequests;
//    }
//
//    public ArrayList<String> getFollowerRequests() {
//        return followerRequests;
//    }
//
//    public void setFollowerRequests(ArrayList<String> followerRequests) {
//        this.followerRequests = followerRequests;
//    }
//
//
//    public void followUser(String friendUsername) {
//        FireStoreHandler.sendFollowRequest(this.username, friendUsername);
//    }
//
//    public void acceptFollowRequest(String friendUsername) {
//        FireStoreHandler.removeIncomingFollowRequest(this.username, friendUsername);
//        FireStoreHandler.addToFollowers(this.username, friendUsername);
//    }
//
//    public void declineFollowRequest(String friendUsername) {
//        FireStoreHandler.removeIncomingFollowRequest(this.username, friendUsername);
//    }
//}
