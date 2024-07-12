package com.skt.skillup.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.skt.skillup.BottomSheets.AttributionBottomSheetFragment;
import com.skt.skillup.BottomSheets.SupportUsBottomSheetFragment;
import com.skt.skillup.BottomSheets.WelcomeBottomSheetFragment;
import com.skt.skillup.R;
import com.skt.skillup.databinding.ActivityDashboardBinding;
import com.skt.skillup.networks.SendDataToServerTask;

import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity implements SendDataToServerTask.AsyncTaskCompleteListener {

    private AppBarConfiguration mAppBarConfiguration;
    @SuppressLint("StaticFieldLeak")
    public static NavController navController;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String FUNCTION_EXECUTED_KEY = "isFunctionExecuted";

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDashboardBinding binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashboard.toolbar);

        setDeviceIDOnce();

        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_account, R.id.nav_task).setOpenableLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home || itemId == R.id.nav_account || itemId == R.id.nav_task) {
                navController.navigate(itemId);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (itemId == R.id.nav_t_and_c) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bharatplaced.com/terms-and-conditions.html"));
//                startActivity(browserIntent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (itemId == R.id.nav_privacy) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bharatplaced.com/privacy-policy.html"));
//                startActivity(browserIntent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (itemId == R.id.nav_about) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bharatplaced.com/about-us.html"));
//                startActivity(browserIntent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (itemId == R.id.nav_contact) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bharatplaced.com/contact-us-BharatPlaced.html"));
//                startActivity(browserIntent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (itemId == R.id.nav_attributions) {
                showAttributes();
                drawer.closeDrawer(GravityCompat.START);
            }

            return false;
        });

        addUserDetails(navigationView);

        showBottomSheetOnce();

        setNavVersion(navigationView);

        checkBioLock();

        askNotification13Plus();

        createNotificationChannel();
    }

    private void setDeviceIDOnce() {

        if (!getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(FUNCTION_EXECUTED_KEY, false)) {
            @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            storeID(androidId);
        }
    }

    private void storeID(String androidId) {

        String userName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        boolean userAccess = false;

        new SendDataToServerTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, userID, userName, androidId, String.valueOf(userAccess));
    }

    @Override
    public void onTaskComplete(Integer responseCode) {
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Log.i("DashboardActivity", "Data sent successfully");
            stopExecutionNow();
        } else {
            Log.e("DashboardActivity", "Error: Server Response Code - " + responseCode);
        }
    }

    public void stopExecutionNow() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(FUNCTION_EXECUTED_KEY, true);
        editor.apply();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "Default Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void askNotification13Plus() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void checkBioLock() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean lockedSwitch = prefs.getBoolean("bioLocked", false);

        Executor executor = ContextCompat.getMainExecutor(getApplicationContext());
        final BiometricPrompt.PromptInfo promptInfoSuccess = new BiometricPrompt.PromptInfo.Builder().setTitle("Scan Fingerprint/Face").setDescription("Scan to login").setDeviceCredentialAllowed(true).build();

        final BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                restartActivity();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(DashboardActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                restartActivity();
            }

            private void restartActivity() {
                Toast.makeText(DashboardActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                DashboardActivity.this.finish();
            }
        });

        if (lockedSwitch) {
            biometricPrompt.authenticate(promptInfoSuccess);
        }
    }

    private void setNavVersion(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        MenuItem versionItem = menu.findItem(R.id.nav_version);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            versionItem.setTitle(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showAttributes() {
        AttributionBottomSheetFragment bottomSheetFragment = new AttributionBottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void openTwitter() {
        String twitter_user_name = "nagrikar_adarsh";

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter_user_name)));
        }
    }

    private void launchEmail() {
        String emailAddress = "adarshnagrikar1404@gmail.com";
        String subject = "Contact";

        Uri uri = Uri.parse("mailto:" + emailAddress + "?subject=" + Uri.encode(subject));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }

    }

    private void showBottomSheetOnce() {
        SharedPreferences prefs = getSharedPreferences("showSheetPreference", Context.MODE_PRIVATE);
        boolean shouldShowSheet = prefs.getBoolean("showSheet", true);

        if (shouldShowSheet) {
            WelcomeBottomSheetFragment bottomSheetFragment = new WelcomeBottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        }
    }

    @SuppressLint("SetTextI18n")
    private void addUserDetails(NavigationView navigationView) {

        View headerView = navigationView.getHeaderView(0);

        TextView textViewUsername = headerView.findViewById(R.id.userName);
        TextView textViewUserEmail = headerView.findViewById(R.id.userEmail);
        CircleImageView imageViewProfile = headerView.findViewById(R.id.userProfile);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        textViewUsername.setText("" + name);

        String mail = auth.getCurrentUser().getEmail();
        textViewUserEmail.setText("" + mail);

        Uri profileUrl = auth.getCurrentUser().getPhotoUrl();
        Glide.with(this).load(profileUrl).centerInside().into(imageViewProfile);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (itemId == R.id.action_share) {
            showShareAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSupportSheet() {
        SupportUsBottomSheetFragment bottomSheetFragment = new SupportUsBottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void showShareAction() {
        String appPackageName = "https://play.google.com/store/apps/details?id=com.app.allmynotes";
        String shareMessage = "Check out AllMyNotes app for all study related help:\n\n" + appPackageName;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share app using"));
        } else {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
        //startActivity(new Intent(this, ImageActivity.class));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}