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
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;

public class Homescreen2 extends AppCompatActivity {
    private static final  String LOGOUT_URL="http://miniprojectnotify.pe.hu/logout.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void gotodisp(View view)
    {
        Intent intent=new Intent(this,Display.class);
        startActivity(intent);
    }
    public  void logout(View view){
        Logout();


    }
    private void Logout() {
        class LogoutUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Homescreen2.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Homescreen2.this,MainScreen.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(String... params) {

                SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("username", "nouser");
                HashMap<String, String> data = new HashMap<>();
                data.put("user", user);
                String result=ruc.sendPostRequest(LOGOUT_URL, data);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                try {
                    InstanceID.getInstance(getApplicationContext()).deleteInstanceID();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return  result;
            }
        }

        LogoutUser ru = new LogoutUser();
        ru.execute();
    }
}
