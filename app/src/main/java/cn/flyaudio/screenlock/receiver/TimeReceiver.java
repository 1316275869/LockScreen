package cn.flyaudio.screenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import cn.flyaudio.screenlock.utils.SystemTimeUtils;


public class TimeReceiver extends BroadcastReceiver {

    private TextView timeView,dataText;

    public TimeReceiver(TextView timeView, TextView dataText) {
        this.timeView = timeView;
        this.dataText = dataText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)){
            timeView.setText(SystemTimeUtils.updateTime());
            dataText.setText(SystemTimeUtils.updataData());
        }
    }
}
