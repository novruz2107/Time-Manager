package com.antech.timemanagement;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Novruz Engineer on 4/29/2018.
 */

public class AppService extends Service {

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        AppService getService() {
            return AppService.this;
        }
    }

    public void onTaskRemoved(Intent intent){
        SharedPreferences prefs = getSharedPreferences("Files", MODE_PRIVATE);
        MainActivity.cancellNotification();
        MainActivity.stopTick(prefs.getInt("number9", 0));
        stopSelf();
    }
}
