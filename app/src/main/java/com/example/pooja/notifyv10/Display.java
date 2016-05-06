package com.example.pooja.notifyv10;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Display extends AppCompatActivity {
    private ListView listView;
    private static  final  String NOTIF="http://miniprojectnotify.pe.hu/getnotifs.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        listView = (ListView)findViewById(R.id.listView);
        SharedPreferences sharedPreferences=getSharedPreferences("pref",Context.MODE_PRIVATE);
        String user=sharedPreferences.getString("username", "nouser");
        showNotif(user);

    }
    private void showNotif(final String user){
        class ShowNotifClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Display.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                String [] displist;
                displist=s.split(",");
                listView.setAdapter(new CustomAdapter(Display.this,displist,R.drawable.logo));

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> d = new HashMap<>();
                d.put("user",user);
                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequest(NOTIF,d);
                return result;
            }
        }
        ShowNotifClass snc = new ShowNotifClass();
        snc.execute(user);
    }


}
