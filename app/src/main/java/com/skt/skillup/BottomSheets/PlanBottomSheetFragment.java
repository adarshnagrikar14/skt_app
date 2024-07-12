package com.skt.skillup.BottomSheets;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.skt.skillup.NotificationReceiver;
import com.skt.skillup.R;
import com.skt.skillup.SharedPreferencesHelper;
import com.skt.skillup.models.PlanModel;
import com.skt.skillup.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class PlanBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;

    public interface BottomSheetListener {
        void onAddButtonClicked(String inputText);
    }

    Button addButton;
    TextInputEditText editText, editDate, editTime;
    ImageView pickDate, pickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout_plan, container, false);

        addButton = view.findViewById(R.id.addButton);
        editText = view.findViewById(R.id.editText);

        editDate = view.findViewById(R.id.stDate);
        editTime = view.findViewById(R.id.stTime);

        pickDate = view.findViewById(R.id.calender);
        pickTime = view.findViewById(R.id.time);

        pickDate.setOnClickListener(this::setDate);
        pickTime.setOnClickListener(this::setTime);

        addButton.setOnClickListener(v -> {
            if (mListener != null) {

                String pDate, pTime, pPlan;

                pDate = Objects.requireNonNull(editDate.getText()).toString();
                pTime = Objects.requireNonNull(editTime.getText()).toString();
                pPlan = Objects.requireNonNull(editText.getText()).toString();

                if (!pDate.isEmpty() && !pTime.isEmpty() && !pPlan.isEmpty()) {
                    setSchedule(pPlan, pDate, pTime);
                } else {
                    Toast.makeText(v.getContext(), "Empty Field!", Toast.LENGTH_SHORT).show();
                }
            }
            dismiss();
        });

        view.findViewById(R.id.cancelTxt).setOnClickListener(view1 -> dismiss());


        return view;
    }

    private void setSchedule(String pPlan, String pDate, String pTime) {

        long timestamp = DateTimeUtils.calculateTimestamp(pDate, pTime);

        if (timestamp != -1) {
            System.out.println("Timestamp: " + timestamp);

            long currentTimestamp = System.currentTimeMillis();

            if (timestamp > currentTimestamp) {
                timerCode(pPlan, timestamp, pDate, pTime);
            } else {
                Toast.makeText(getActivity(), "Invalid Time selected.", Toast.LENGTH_SHORT).show();
            }

        } else {
            System.out.println("Timestamp calculation failed.");
            Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
            dismiss();
        }

        mListener.onAddButtonClicked(pPlan);
    }

    private void setTime(View view) {

        TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
            String selectedTime = hourOfDay + ":" + minute;
            editTime.setText(selectedTime);
        };

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), timeSetListener, hour, minute, true);
        timePickerDialog.show();
    }

    private void setDate(View view) {

        DatePickerDialog.OnDateSetListener dateSetListener = (view2, year, monthOfYear, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            editDate.setText(selectedDate);
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }

    @SuppressLint("ScheduleExactAlarm")
    private void timerCode(String message, long duration, String pDate, String pTime) {
        Context context = getContext();

        assert context != null;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra("contentNotification", "Reminder Notification");
        notificationIntent.putExtra("titleNotification", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, duration, pendingIntent);

        PlanModel newTask = new PlanModel(pDate, pTime, message);
        ArrayList<PlanModel> planModels;
        planModels = SharedPreferencesHelper.getPlans(context);
        planModels.add(newTask);

        SharedPreferencesHelper.savePlans(requireContext(), planModels);

        Toast.makeText(context, "Scheduled set!", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
