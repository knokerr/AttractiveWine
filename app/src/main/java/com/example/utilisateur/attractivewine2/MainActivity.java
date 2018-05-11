package com.example.utilisateur.attractivewine2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

//    final Spinner spinnerDeTest = (Spinner) findViewById(R.id.spinnerDeTest);

    private Button goToCave;
    public static String BIERS_UPDATE = "com.example.utilisateur.attractivewine2.update.Beers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToCave = findViewById(R.id.Cave);
        goToCave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }
        );
        Vin.startActionGetWine(this);
        Log.i(TAG, "Avec télémagouille on en a ...");
        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(),intentFilter);

    }

    public class BierUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            createNotification();
            //TODO : plus de parametre pour la notification
            Log.i(TAG,"J'ai été sélectionné !!!");
            displayCocktails("0cocktailArray.json");
            displayCocktails("1cocktailArray.json");
            displayCocktails("2cocktailArray.json");
        }
    }

    private void createNotification() {
        final NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final Intent launchNotifiactionIntent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, launchNotifiactionIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this)
                .setWhen(System.currentTimeMillis())
                .setTicker("Bonjour")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(getResources().getString(R.string.download_done))
                .setContentText(getResources().getString(R.string.notification_desc))
                .setContentIntent(pendingIntent);

        mNotification.notify(1, builder.build());

    }


    private HashMap<String, String> m_li;
    public void displayCocktails(String fileName){
        String jsonFile = loadJSON( fileName );
        try{
            JSONObject obj = new JSONObject(jsonFile);
            JSONArray m_jArry = obj.getJSONArray("drinks");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject JsStrinf = m_jArry.getJSONObject(i);
                Log.d("Details-->", JsStrinf.getString("strDrink"));

            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String loadJSON(String fileName) {
        String json = null;
        try {
            InputStream is = new FileInputStream(getCacheDir()+"/"+fileName);
            Log.i(TAG,getCacheDir()+fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
