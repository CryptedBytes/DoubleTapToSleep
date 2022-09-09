package com.cryptedbytes.dtts;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    private static final String onClickTag = "onClickEvent";

    private static final String ButtonClick1 = "ButtonClickTag1";
    private static final String ButtonClick2 = "ButtonClickTag2";
    private static final String LayoutClick0 = "LayoutClickTag0";
    private static final String TextClick = "TextClickTag1";
    static boolean resetTimerActive = false;
    static byte clickCount = 0;

    static int static_appWidgetId;
    public static AppWidgetManager static_appWidgetManager;

    public static String executeShellForResult(String command, Boolean useRootShell) {
        String line="",output="";
        try
        {
            Process p;
            if(useRootShell) p = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            else p = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});

            BufferedReader b=new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((line=b.readLine())!=null){output+=line+"\r\n";}
        }catch(Exception e){return "error";}
        return output;

    }



    static protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, Widget.class);
        intent.setAction(action);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            return PendingIntent.getBroadcast(context, 0, intent, 0);
        }
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d("Widget", "updateAppWidget");

        static_appWidgetId = appWidgetId;
        static_appWidgetManager = appWidgetManager;


        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
       // views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);



        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
       // remoteViews.setTextViewText(R.id.appwidget_text, widgetText);

        //setup actions for the two buttons and textview as too.
        remoteViews.setOnClickPendingIntent(R.id.hiworldbtn, getPendingSelfIntent(context, ButtonClick1));
        remoteViews.setOnClickPendingIntent(R.id.hiworldbtn2, getPendingSelfIntent(context, ButtonClick2));
        //remoteViews.setOnClickPendingIntent(R.id.appwidget_text, getPendingSelfIntent(context, TextClick));
        remoteViews.setOnClickPendingIntent(R.id.widgetLayout, getPendingSelfIntent(context, LayoutClick0));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

//remoteViews.setColor(R.id.widgetLayout,null, Color.argb(0,0,0,0));

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        Log.d("Widget", "onUpdate");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("Widget", "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction()!= null) Log.d("Widget", "onReceive: " + intent.getAction().toString());

        if (ButtonClick1.equals(intent.getAction())) {



            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
           // remoteViews.setInt(R.id.widgetLayout, "setBackgroundColor", Color.RED);
           // remoteViews.setString(R.id.hiworldbtn2, "setText()","test");

           // remoteViews.setInt(R.id.imview, "setColorFilter", Color.RED);

           // remoteViews.setInt(R.id.imview, "setBackgroundColor", Color.RED);
            remoteViews.setInt(R.id.imview, "setBackgroundColor", Color.TRANSPARENT);

         //   remoteViews.setInt(R.id.widgetLayout, "setBackgroundResource", Color.BLUE);

            static_appWidgetManager.updateAppWidget(static_appWidgetId, remoteViews);


            Toast.makeText(context, "Button1", Toast.LENGTH_SHORT).show();
            Log.w("Widget", "Clicked button1");

            executeShellForResult("input keyevent KEYCODE_POWER", true);
            //executeShellForResult("input keyevent 26", true);

        }
        else if (LayoutClick0.equals(intent.getAction())) {


            Log.w("Widget", "Clicked Layout");
            clickCount++;
            Log.d("Widgetclick dblclk:", "Click count: " + clickCount);
            if (!resetTimerActive) {
                resetTimerActive = true;

                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        Log.d("Widgetclick dblclk:", "Click count reset ");
                        clickCount = 0;
                        resetTimerActive = false;
                        t.cancel();
                    }
                }, 550, 1);

            }

            if (clickCount == 2) {

                Log.d("Widgetclick dblclk:", "Possible double click. Click count: " + clickCount);
                Toast.makeText(context, "Double click", Toast.LENGTH_SHORT).show();
                executeShellForResult("input keyevent KEYCODE_POWER", true);
                clickCount = 0;

            }

        } else if (ButtonClick2.equals(intent.getAction())) {



        } else if (TextClick.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            String widgetText = "Textivew"; //default
            if (extras != null) {
                //Log.w("Widget", "Bundle is not NULL");
                int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    //Log.w("Widget", "widget id is ok");
                    widgetText = "TEST";
                }
            }
            Toast.makeText(context, widgetText, Toast.LENGTH_SHORT).show();
            Log.w("Widget", "TV: " + widgetText);
        }


        if (onClickTag.equals(intent.getAction())){
            //your onClick action is here
            //display in short period of time
            Log.d("Widget", "onclick ok");
            Toast.makeText(context, "n", Toast.LENGTH_SHORT).show();

        }
    };


}