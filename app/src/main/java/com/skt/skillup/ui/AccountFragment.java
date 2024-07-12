package com.skt.skillup.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.skt.skillup.R;
import com.skt.skillup.activities.SplashActivity;

import java.util.Objects;

public class AccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        addUserDetails(view);

        view.findViewById(R.id.logoutButton).setOnClickListener(view1 -> logoutUser());

        return view;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        requireActivity().finish();
        startActivity(new Intent(requireActivity(), SplashActivity.class));
    }

    @SuppressLint("SetTextI18n")
    private void addUserDetails(View view) {

        TextView textViewUsername = view.findViewById(R.id.userName);
        TextView textViewUserEmail = view.findViewById(R.id.userMail);
        ImageView imageViewProfile = view.findViewById(R.id.userProfile);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        textViewUsername.setText("" + name);

        String mail = auth.getCurrentUser().getEmail();
        textViewUserEmail.setText("" + mail);

        Uri profileUrl = auth.getCurrentUser().getPhotoUrl();
        Glide.with(this).load(profileUrl).centerCrop().into(imageViewProfile);

    }
}