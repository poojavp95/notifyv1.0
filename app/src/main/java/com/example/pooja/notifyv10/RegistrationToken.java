package com.example.pooja.notifyv10;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationToken extends IntentService {
    private static final String TAG = "RegIntentService";
    private String TOPICS;
    private static final String REGISTER_URL = "http://miniprojectnotify.pe.hu/regid.php";

    public RegistrationToken() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("CAlled Intent Service","Intent Service");

        TOPICS= (String )intent.getExtras().get("TOPICS");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {

            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //System.out.print(token);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.


            // Subscribe to topic channels
            subscribeTopics(token);
            sendRegistrationToServer(token);

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }

    }

    /**
     *
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        SharedPreferences sharedPreferences=getSharedPreferences("pref", Context.MODE_PRIVATE);
        String user=sharedPreferences.getString("username", "nouser");
        RegisterUserClass ruc = new RegisterUserClass();
        HashMap<String, String> data = new HashMap<String,String>();
        data.put("regid",token);
        data.put("user",user);

        String result = ruc.sendPostRequest(REGISTER_URL,data);
        stopSelf();


        // Add custom implementation, as needed.
    }


    // [START subscribe_topics]
  private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        if(TOPICS !=null) {
            String[] TOPIC = TOPICS.split(",");

            for (String topic : TOPIC) {
                pubSub.subscribe(token, topic, null);
            }
        }
    }
    // [END subscribe_topics]

}