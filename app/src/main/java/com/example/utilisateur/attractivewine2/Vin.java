package com.example.utilisateur.attractivewine2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Debug;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class Vin extends IntentService {
    private static final String ACTION_get_All_wine = "com.example.utilisateur.attractivewine2.action.Beers";



    public Vin() {
        super("Vin");

    }

    public static void startActionGetWine(Context context) {
        Intent intent = new Intent(context, Vin.class);
        intent.setAction(ACTION_get_All_wine);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "C'est moiiiii !!");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_get_All_wine.equals(action)) {
                handleActionWine();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.BIERS_UPDATE));

            }
        }
    }

    private void handleActionWine() {
        Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
        URL url = null;

        try{
            url = new URL("http://binouze.fabrigli.fr/bieres.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(conn.getInputStream(),
                                        new File(getCacheDir(), "bieres.json"));
                Log.d(TAG, "Bieres json downloaded");
            }
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void copyInputStreamToFile(InputStream in, File file){
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int lenght;
            while ((lenght = in.read(buf)) > 0) {
                out.write(buf, 0, lenght);
            }
            out.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
