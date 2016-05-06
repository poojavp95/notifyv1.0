package com.example.pooja.notifyv10;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Topics extends AppCompatActivity implements View.OnClickListener {
    private CheckBox checkbox_general;
    private CheckBox checkbox_ieee;
    private CheckBox checkbox_acm;
    private CheckBox checkbox_iet;
    private CheckBox checkbox_artcircle;
    private CheckBox checkbox_sports;
    private CheckBox checkbox_cultural;

    private Button buttonReg;
    private static final String TOPIC_URL = "http://miniprojectnotify.pe.hu/topic.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        checkbox_general=(CheckBox)findViewById(R.id.checkbox_general);
        checkbox_ieee=(CheckBox)findViewById(R.id.checkbox_ieee);
        checkbox_acm=(CheckBox)findViewById(R.id.checkbox_acm);
        checkbox_iet=(CheckBox)findViewById(R.id.checkbox_iet);
        checkbox_artcircle=(CheckBox)findViewById(R.id.checkbox_artcircle);
        checkbox_sports=(CheckBox)findViewById(R.id.checkbox_sports);
        checkbox_cultural=(CheckBox)findViewById(R.id.checkbox_cultural);
        buttonReg = (Button) findViewById(R.id.buttonReg);

        buttonReg.setOnClickListener(this);
    }

    public void onClick(View v) {
        if(v == buttonReg){
            gettokens();
        }
    }
    public void gettokens()
    {
        String TOPICS="";
        if(checkbox_general.isChecked())
            TOPICS+="/topics/general,";
        if(checkbox_ieee.isChecked())
            TOPICS+="/topics/ieee,";
        if(checkbox_acm.isChecked())
            TOPICS+="/topics/acm,";
        if(checkbox_iet.isChecked())
            TOPICS+="/topics/iet,";
        if(checkbox_artcircle.isChecked())
            TOPICS+="/topics/artcircle,";
        if(checkbox_sports.isChecked())
            TOPICS+="/topics/sports,";
        if(checkbox_cultural.isChecked())
            TOPICS+="/topics/cultural,";
        topics(TOPICS);

    }
    private void topics(final String TOPICS) {
        class TopicSave extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Topics.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Topics.this,MainScreen.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(String... params) {
                Intent intent=getIntent();
                String user=(String)intent.getExtras().get("user");
                HashMap<String, String> data = new HashMap<>();
                data.put("user",user);
                data.put("topic",TOPICS);

                String result = ruc.sendPostRequest(TOPIC_URL,data);

                return  result;
            }
        }

        TopicSave ts=new TopicSave();
        ts.execute(TOPICS);
    }
}


