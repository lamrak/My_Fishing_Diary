package net.validcat.fishing.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import net.validcat.fishing.ListActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.data.FishingContract;

/**
 * Created by Oleksii on 3/29/16.
 */
public class FishingCountWidgetIntentService extends IntentService {
    private static final String[] COLUMNS = {
            FishingContract.FishingEntry._ID
    };

    public FishingCountWidgetIntentService() {
        super("FishingCountWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FishingCountWidgetProvider.class));

        // Get today's data from the ContentProvider
        int counter = 0;

        Cursor data = getContentResolver().query(
                FishingContract.FishingEntry.CONTENT_URI, COLUMNS, null,
                null, null);
        if (data != null) {
            if (data.moveToFirst()) {
                counter++;
                while (data.moveToNext()) {
                    counter++;
                }
            }
        }

        data.close();
        // Perform this loop procedure for each widget
        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget_layout;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            //views.setImageViewResource(R.id.ic_w_before, R.drawable.ic_w_before);
            // Content Descriptions for RemoteViews were only added in ICS MR1
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                setRemoteContentDescription(views, getString(R.string.widget_fishing_counter));
//            }
            views.setTextViewText(R.id.fishing_counter, String.valueOf(counter));

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, ListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        // Release the wake lock provided by the BroadcastReceiver.
//        completeWakefulIntent(intent);
    }


}
