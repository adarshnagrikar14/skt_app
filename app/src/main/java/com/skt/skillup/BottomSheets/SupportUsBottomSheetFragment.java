package com.skt.skillup.BottomSheets;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skt.skillup.R;

public class SupportUsBottomSheetFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.support_us_bottom_sheet, container, false);

        Button dButton = view.findViewById(R.id.dismissButton);
        TextView textView = view.findViewById(R.id.copyUPI);

        dButton.setOnClickListener(view1 -> {
            dismiss();

            SharedPreferences prefs = requireContext().getSharedPreferences("showSheetPreference", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("showSheet", false).apply();
        });

        textView.setOnClickListener(view1 -> {
            ClipboardManager clipboardManager = (ClipboardManager) view1.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("UPI-ID", "a.nagrikar@paytm");
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(view1.getContext(), "UPI ID Copied", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
