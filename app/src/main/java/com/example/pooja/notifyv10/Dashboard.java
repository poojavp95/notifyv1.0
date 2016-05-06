package com.example.pooja.notifyv10;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private static final String URL = "http://miniprojectnotify.pe.hu/function.php";
    private EditText editTextmsg;
    private Button buttonPush;
    private CheckBox checkbox_general;
    private CheckBox checkbox_ieee;
    private CheckBox checkbox_acm;
    private CheckBox checkbox_iet;
    private CheckBox checkbox_artcircle;
    private CheckBox checkbox_sports;
    private CheckBox checkbox_cultural;
    private  String TOPICS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        editTextmsg = (EditText) findViewById(R.id.editTextmsg);
        checkbox_general=(CheckBox)findViewById(R.id.checkbox_general);
        checkbox_ieee=(CheckBox)findViewById(R.id.checkbox_ieee);
        checkbox_acm=(CheckBox)findViewById(R.id.checkbox_acm);
        checkbox_iet=(CheckBox)findViewById(R.id.checkbox_iet);
        checkbox_artcircle=(CheckBox)findViewById(R.id.checkbox_artcircle);
        checkbox_sports=(CheckBox)findViewById(R.id.checkbox_sports);
        checkbox_cultural=(CheckBox)findViewById(R.id.checkbox_cultural);

        buttonPush = (Button) findViewById(R.id.buttonPush);

        buttonPush.setOnClickListener(this);


    }

    private void sendnotif(){
        String msg= editTextmsg.getText().toString().trim();
        TOPICS="";
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
        sendNotif(msg,TOPICS);
    }

    private void sendNotif(final String msg,final String topic){
        class sendNotifClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Dashboard.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("FALSE")){
                    Toast.makeText(Dashboard.this, "Notification sending failed :(", Toast.LENGTH_LONG).show();


                }else{
                    Toast.makeText(Dashboard.this, "Notification Sent!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("message", params[0]);
                data.put("topic",params[1]);


                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(URL,data);

                return result;
            }
        }
        sendNotifClass ulc = new sendNotifClass();
        ulc.execute(msg,topic);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonPush)
        {
            sendnotif();
        }

    }
}