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
    private static String URLbase = "https://www.thecocktaildb.com/api/json/v1/1/";
    private static String []Refs = {      "search.php?s="   //0 //+cocktail name : return recipe
                                        , "search.php?i="   //1 //+ingredient : return ingredient description
                                        , "lookup.php?i="   //2 //+id : return a precise recipe
                                        , "filter.php?i="   //3 //+ingredient : return all cocktails containing those ingredients

                                        , "filter.php?a=Alcoholic"          //4 //+category : return all cocktails in this category
                                        , "filter.php?a=Non_Alcoholic"      //5 //+category : return all cocktails in this category
                                        , "filter.php?a=Optional_alcohol"   //6 //+category : return all cocktails in this category

                                        , "filter.php?c="   //7 //+Category : return all the cocktails in this category
                                        , "list.php?c="     //8 //get all categories
                                        , "list.php?i="     //9 //get all ingredients
                                        , "list.php?g="     //10//get all glass types
                                        , "random.php" };   //11//get a random cocktail



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
        try{
            for(int i=0; i<3; i++)
            {
                URL url = new URL( URLbase + Refs[4+i] );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                    File f = new File(getCacheDir(), i+"cocktailArray.json");
                    copyInputStreamToFile(conn.getInputStream(), f );
                    Log.d(TAG,  i + " cocktail list json downloaded");
                }
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
            OutputStream out = new FileOutputStream(file, true);
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
