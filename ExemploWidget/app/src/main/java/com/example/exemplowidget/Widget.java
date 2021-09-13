package com.example.exemplowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    private static final String VERIFICAR = "Verificar";

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        //Quando o botao receber um click
        views.setOnClickPendingIntent(R.id.btnVerificar, getPendingSelfIntent(context, VERIFICAR));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
            Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
        if (VERIFICAR.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager =
                    AppWidgetManager.getInstance(context);
            RemoteViews remoteViews;
            ComponentName watchWidget;
            remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            watchWidget = new ComponentName(context, Widget.class);
            final String[] value = new String[1];
            final int[] level = new int[1];
            final IntentFilter ifilter;
            final Intent[] batteryStatus = new Intent[1];
            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            batteryStatus[0] =
                    context.getApplicationContext().registerReceiver(null, ifilter);
            level[0] = batteryStatus[0].getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            value[0] = Integer.toString(level[0]);
            remoteViews.setTextViewText(R.id.appwidget_text, "Bateria: " + value[0] + "%");

            if (level[0] <= 49 && level[0] >= 15){
                remoteViews.setTextColor(R.id.appwidget_text, Color.parseColor("#ffa500"));
            } else if (level[0] < 15){
                remoteViews.setTextColor(R.id.appwidget_text, Color.parseColor("#e61919"));
            }else{
                remoteViews.setTextColor(R.id.appwidget_text, Color.parseColor("#ffffff"));
            }

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}