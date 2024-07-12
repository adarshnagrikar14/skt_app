package com.skt.skillup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TaskViewModel extends ViewModel {
    private final MutableLiveData<Boolean> taskStatusChangeLiveData = new MutableLiveData<>();

    public LiveData<Boolean> getTaskStatusChangeLiveData() {
        return taskStatusChangeLiveData;
    }

    public void notifyTaskStatusChanged() {
        taskStatusChangeLiveData.setValue(true);
    }
}
