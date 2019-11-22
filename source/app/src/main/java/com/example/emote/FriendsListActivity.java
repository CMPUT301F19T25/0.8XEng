package com.example.emote;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FriendsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_following);
        this.getSupportActionBar().hide();
    }
}
