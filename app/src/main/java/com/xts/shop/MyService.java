package com.xts.shop;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    int times=0;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private static final String TAG = "MyService";
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");

          new Thread() {

            public void run() {
                while (true){

                    times++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "time---="+times);
                }
            }
        }.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: ");

    }
}
