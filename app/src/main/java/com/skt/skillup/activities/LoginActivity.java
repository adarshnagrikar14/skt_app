package com.skt.skillup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.skt.skillup.BuildConfig;
import com.skt.skillup.R;
import com.skt.skillup.networks.ReceiveDataFromServerAuthTask;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements ReceiveDataFromServerAuthTask.OnDataReceivedListener {

    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    final int RC_SIGN_IN = 123;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void openTandC(View view) {
    }

    public void loginUser(View view) {
        initiateRequired();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initiateRequired() {

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("9732883178-6h7eb10ks9g5qkbdrk7c1atv8jefq3jc.apps.googleusercontent.com").requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {

                showDialogAndCheck(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

            } else {
                Log.w("TAG", "signInWithCredential:failure", task.getException());
                Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogAndCheck(String uid) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Checking credentials");
        progressDialog.show();

        String serverUrl = BuildConfig.API_AUTH;
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        ReceiveDataFromServerAuthTask authTask = new ReceiveDataFromServerAuthTask(uid, androidId, this);
        authTask.execute(serverUrl);

    }

    @Override
    public void onDataReceived(String result) {
        progressDialog.dismiss();

        if (result != null) {

            Log.d("LOGIN-ACT", "onDataReceived: " + result);
            if ("44".equals(result)) {
                logOutComplete();
                Toast.makeText(this, "Multiple Login not allowed", Toast.LENGTH_SHORT).show();
            } else if ("22".equals(result)) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            } else {
                logOutComplete();
                Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        } else {
            logOutComplete();
            Toast.makeText(this, "Received null result", Toast.LENGTH_SHORT).show();
        }
    }

    private void logOutComplete() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Use Different Account or Retry", Toast.LENGTH_SHORT).show();
            } else {
                logOutComplete();
            }
        });
    }
}