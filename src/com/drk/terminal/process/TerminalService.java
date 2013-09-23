package com.drk.terminal.process;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/23/13
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalService extends Service {
    public static final String LOG_TAG = TerminalService.class.getSimpleName();

    /**
     * Class for clients to access. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        TerminalService getService() {
            return TerminalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This is the object that receives interactions from clients.
      */
    private final IBinder mBinder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
    }
}
