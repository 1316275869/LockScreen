package cn.flyaudio.screenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import cn.flyaudio.screenlock.service.ScreenLockService;


public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION="android.intent.action.BOOT_COMPLETED";
    private String TAG="BootBroadcastReceiver1";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)){
            Intent service=new Intent(context, ScreenLockService.class);
            context.startForegroundService(service);
            Log.d(TAG, "onReceive:  ");
        }
        Log.d(TAG, "onReceive:  android");

    }
}
