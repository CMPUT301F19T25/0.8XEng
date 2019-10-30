package com.example.emote;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginHelper {
    public static String DUMMY_DOMAIN = "@dummydomainthatdoesntexist.com";
    // TODO: Move all user and password validation / sanitization here

    public static Task<AuthResult> signupUser(String username, String password){
        String email = username + DUMMY_DOMAIN;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public static Task<AuthResult> loginUser(String username, String password){
        String email = username + DUMMY_DOMAIN;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.signInWithEmailAndPassword(email, password);
    }
}
