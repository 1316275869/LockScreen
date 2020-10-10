package cn.flyaudio.screenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.flyaudio.screenlock.ui.ScreenLockManager;


public class ScreenStatusReceiver extends BroadcastReceiver {
    String SCREEN_ON = "autochips.intent.action.QB_POWERON";
    String SCREEN_OFF = "autochips.intent.action.QB_POWEROFF";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SCREEN_ON)){
            new ScreenLockManager(context.getApplicationContext());
            Log.d("BootBroadcastReceiver1", "SCREEN_ON");
        }
        Log.d("ScreenLockService", " onReceive: ");
    }
}
