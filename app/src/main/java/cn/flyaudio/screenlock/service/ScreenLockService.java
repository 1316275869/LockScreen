package cn.flyaudio.screenlock.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import cn.flyaudio.screenlock.receiver.ScreenStatusReceiver;
import cn.flyaudio.screenlock.ui.ScreenLockManager;


public class ScreenLockService extends Service {
    private String notificationId = "channelId";
    private String notificationName = "channelName";
    private NotificationManager notificationManager;
    private ScreenStatusReceiver screenStatusReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new ScreenLockManager(getApplicationContext());
        Log.d("ScreenLockService","ScreenLockManager");
         notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(1,getNotification());


    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("测试服务")
                .setContentText("我正在运行");
        //设置Notification的ChannelID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
        }
        Notification notification = builder.build();
        return notification;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        screenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction("autochips.intent.action.QB_POWERON");
        screenStatusIF.addAction("autochips.intent.action.QB_POWEROFF");
        // 注册
        registerReceiver(screenStatusReceiver, screenStatusIF);
        Log.d("ScreenLockService", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenStatusReceiver);
        Log.d("ScreenLockService", "onDestroy: ");
    }
}
