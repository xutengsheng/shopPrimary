package com.xts.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MeReceiver extends BroadcastReceiver {

    private static final String TAG = "MeReceiver";
    //接受广播
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String action = intent.getAction();

        Toast.makeText(context, "title="+title, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: "+action);
    }
}
