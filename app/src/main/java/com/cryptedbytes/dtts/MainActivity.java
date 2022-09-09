package com.cryptedbytes.dtts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
static boolean resetTimerActive = false;
static byte clickCount = 0;
static boolean widgetRequiresTransparency = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameView, new SettingsFragment())
                .commit();




/*
        TextView txt = findViewById(R.id.appwidget_text);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickCount++;

                if(!resetTimerActive){
                    resetTimerActive = true;

                    Timer t = new Timer();
                    t.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            clickCount = 0;
                            resetTimerActive = false;
                            t.cancel();
                        }
                    }, 400, 1);

                }

                if(clickCount > 2){
                    Log.d("Widgetclick", "Possible double click. Click count: " + clickCount);
                }

            }
        });
        */


    }


    public void onButtonClick(View view) {


        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        ComponentName thisWidget = new ComponentName(context, Widget.class);
        remoteViews.setInt(R.id.imview, "setBackgroundColor", Color.TRANSPARENT);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

      //  RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.widget);
        // remoteViews.setInt(R.id.widgetLayout, "setBackgroundColor", Color.RED);
        // remoteViews.setString(R.id.hiworldbtn2, "setText()","test");

        // remoteViews.setInt(R.id.imview, "setColorFilter", Color.RED);

        // remoteViews.setInt(R.id.imview, "setBackgroundColor", Color.RED);


        //   remoteViews.setInt(R.id.widgetLayout, "setBackgroundResource", Color.BLUE);

      //  Widget.static_appWidgetManager.updateAppWidget(Widget.static_appWidgetId, remoteViews);


    }

    public void onButtonClick1(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppWidgetManager mAppWidgetManager = getSystemService(AppWidgetManager.class);

            ComponentName myProvider = new ComponentName(this, Widget.class);

            Bundle b = new Bundle();
            b.putString("ggg", "ggg");
            if (mAppWidgetManager.isRequestPinAppWidgetSupported()) {
                Intent pinnedWidgetCallbackIntent = new Intent(this, Widget.class);
                PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                        pinnedWidgetCallbackIntent, PendingIntent.FLAG_MUTABLE);

                mAppWidgetManager.requestPinAppWidget(myProvider, b, successCallback);
            }
        }
    }
}