package com.skt.skillup.BottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.skt.skillup.R;

import java.util.Objects;

public class TaskBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;

    public interface BottomSheetListener {
        void onAddButtonClicked(String inputText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout_task, container, false);

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            if (mListener != null) {
                TextInputEditText editText = view.findViewById(R.id.editText);
                String inputText = Objects.requireNonNull(editText.getText()).toString();
                mListener.onAddButtonClicked(inputText);
            }
            dismiss();
        });

        return view;
    }

    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }
}
