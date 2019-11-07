package com.example.emote.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.FireStoreHandler;
import com.example.emote.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ProfileViewModel profileViewModel;

    private TextView usernameText;
    private TextView currentmoodText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_profile);
        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        usernameText = root.findViewById(R.id.profile_username);
        currentmoodText = root.findViewById(R.id.profile_current_mood);

        FireStoreHandler fsh = new FireStoreHandler("john123");
        usernameText.setText(fsh.getUsername());
        currentmoodText.setText(fsh.getRecentEmote().getEmote());

        return root;
    }
}