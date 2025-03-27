package com.bwi.foflow;

import android.app.Application;
import android.util.Log;

import com.bwi.onboard.remote.FORemoteConfig;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class App extends Application {

    private final String TAG = App.class.getSimpleName();
    public static FORemoteConfig foRemoteConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
       // FORemoteConfig.getInstance();
        // Initialize Firebase Remote Config
        foRemoteConfig = FORemoteConfig.getInstance();
        /*
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        // Set default values (from XML or programmatically)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        // Synchronous fetch-and-activate
        fetchRemoteConfigSync(remoteConfig);*/

    }

    private void fetchRemoteConfigSync(FirebaseRemoteConfig remoteConfig) {
        // Initialize CountDownLatch with a count of 1
        CountDownLatch latch = new CountDownLatch(1);

        // Start fetch-and-activate process
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("RemoteConfig", "Remote values fetched and activated.");
                    } else {
                        Log.e("RemoteConfig", "Failed to fetch remote values.");
                    }
                    latch.countDown(); // Release the latch when done
                });

        try {
            // Wait for the fetch to complete, with a timeout to prevent ANR
            if (!latch.await(3, TimeUnit.SECONDS)) {
                Log.e("RemoteConfig", "Fetch timeout. Proceeding with default/cached values.");
            }
        } catch (InterruptedException e) {
            Log.e("RemoteConfig", "Fetch was interrupted. Proceeding with default/cached values.", e);
        }
    }
}
