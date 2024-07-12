package com.skt.skillup.activities;


import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.skt.skillup.R;

import java.util.concurrent.Executor;

public class LockActivity extends AppCompatActivity {

    TextView avlText, enableText;
    SwitchCompat bmSwitch;

    @SuppressLint({"SetTextI18n", "SwitchIntDef"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        toolbar.setNavigationOnClickListener(v -> LockActivity.this.finish());

        avlText = findViewById(R.id.s5s);
        enableText = findViewById(R.id.s5t);
        bmSwitch = findViewById(R.id.sw1);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean lockedSwitch = prefs.getBoolean("bioLocked", false);

        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {

            case BiometricManager.BIOMETRIC_SUCCESS:
                bmSwitch.setEnabled(true);
                avlText.setText("Biometric sensor available");
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                bmSwitch.setEnabled(false);
                avlText.setText("This device does not have a biometric sensor");
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                bmSwitch.setEnabled(false);
                avlText.setText("The biometric sensor is currently unavailable");
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                bmSwitch.setEnabled(false);
                avlText.setText("Your device doesn't have biometric saved,please check your security settings");
                break;
        }

        bmSwitch.setChecked(lockedSwitch);
        if (lockedSwitch) {
            enableText.setText("Disable Biometric lock.");
        } else {
            enableText.setText("Enable Biometric lock.");
        }

        Executor executor = ContextCompat.getMainExecutor(getApplicationContext());
        final BiometricPrompt.PromptInfo promptInfoSuccess = new BiometricPrompt.PromptInfo.Builder().setTitle("Scan Fingerprint/Face")
                .setDescription("Scan to enable")
                .setDeviceCredentialAllowed(true).
                build();

        final BiometricPrompt.PromptInfo promptInfoFailed = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Scan Fingerprint/Face")
                .setDescription("Scan to disable")
                .setDeviceCredentialAllowed(true)
                .build();

        final BiometricPrompt biometricPrompt = new BiometricPrompt(
                this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                restartActivity();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (lockedSwitch) {
                    Toast.makeText(getApplicationContext(), "Biometric Disabled.", Toast.LENGTH_SHORT).show();
                    prefs.edit().putBoolean("bioLocked", false).apply();
                } else {
                    Toast.makeText(getApplicationContext(), "Biometric enabled.", Toast.LENGTH_SHORT).show();
                    prefs.edit().putBoolean("bioLocked", true).apply();
                }
                restartActivity();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                restartActivity();
            }
        });

        bmSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                biometricPrompt.authenticate(promptInfoSuccess);
            } else {
                biometricPrompt.authenticate(promptInfoFailed);
            }
        });

    }

    private void restartActivity() {
        startActivity(new Intent(getApplicationContext(), LockActivity.class));
        LockActivity.this.finish();
    }

}