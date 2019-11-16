package com.example.emote.ui.map;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.emote.MapsActivity;
import com.example.emote.R;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        Button Personal_Button = root.findViewById(R.id.personal_location_history_button);
        Button Friends_Button = root.findViewById(R.id.friends_location_history_button);

        Personal_Button.setOnClickListener((v) -> ViewPersonalLocation(v));
        Friends_Button.setOnClickListener((v) -> ViewFriendsLocation(v));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    public void ViewPersonalLocation(View view) {
        Intent intent = new Intent(this.getContext(), MapsActivity.class);
        startActivity(intent);
    }

    public void ViewFriendsLocation(View view) {
        Intent intent = new Intent(this.getContext(), MapsActivity.class);
        startActivity(intent);
    }
}
