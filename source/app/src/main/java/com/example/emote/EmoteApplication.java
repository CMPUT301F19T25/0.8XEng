package com.example.emote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;

public class EmoteApplication {
    static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        EmoteApplication.username = username;
    }

    static public boolean isOnline() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
