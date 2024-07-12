package com.skt.skillup.BottomSheets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skt.skillup.R;

public class AttributionBottomSheetFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attribution_bottom_sheet, container, false);

        Button dButton = view.findViewById(R.id.dismissButton);

        dButton.setOnClickListener(view1 -> {
            dismiss();

            SharedPreferences prefs = requireContext().getSharedPreferences("showSheetPreference", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("showSheet", false).apply();

        });

        return view;
    }
}
