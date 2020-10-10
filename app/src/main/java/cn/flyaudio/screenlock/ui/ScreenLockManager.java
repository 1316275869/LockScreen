package cn.flyaudio.screenlock.ui;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import cn.flyaudio.screenlock.R;
import cn.flyaudio.screenlock.databinding.PasswordLockBinding;
import cn.flyaudio.screenlock.modle.PasswordNumber;
import cn.flyaudio.screenlock.receiver.TimeReceiver;
import cn.flyaudio.screenlock.utils.SystemTimeUtils;


public class ScreenLockManager {
    private static final String TAG = "WarmingManager";
    private static final int MSG_PASSWORD_EEROR = 666;
    private Context mContext;
    private LayoutInflater mInflater;
    private View mView = null;
    private PasswordLockBinding binding;
    private View errorView=null;
    private View screenSaverView;
    private WindowManager.LayoutParams screenSavarParams;
    private WindowManager mWindowManager;
    private TimeReceiver mTimeReceiver = null;
    private IntentFilter mTimeFilter = null;
    private TextView timeText,dataText;


    public ScreenLockManager(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        //PasswordLockBinding da= DataBindingUtil.inflate(mInflater,R.layout.password_lock,null,false);
       // DataBindingUtil.bind(mView);
        mView = mInflater.inflate(R.layout.password_lock, null);
        errorView= mInflater.inflate(R.layout.error_tips, null);
        screenSaverView=mInflater.inflate(R.layout.screen_saver,null);
        binding= DataBindingUtil.bind(mView);

        timeText=screenSaverView.findViewById(R.id.digitalClock);
        dataText=screenSaverView.findViewById(R.id.system_data);

        timeText.setText(SystemTimeUtils.updateTime());
        dataText.setText(SystemTimeUtils.updataData());

        initBroadcast();//接收时间广播  改变时间

        PasswordNumber passwordNumber=new PasswordNumber();
        binding.setNumber(passwordNumber);
        eventHanded();
        mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        errorView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        params.type= LayoutParams.TYPE_APPLICATION_OVERLAY;
        //params.type = 2021;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        params.format = PixelFormat.TRANSLUCENT;
        initScreenSavarParams();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        mWindowManager.addView(mView, params);
        mWindowManager.addView(errorView,params);
        mWindowManager.addView(screenSaverView,screenSavarParams);
//        scaleWindow();
//        mWindowManager.updateViewLayout(screenSaverView,params);
        Log.d("yyqbc8", "WarmingManager: show");
    }

    private void initScreenSavarParams(){
        screenSavarParams=new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        screenSavarParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN| LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        screenSavarParams.type= LayoutParams.TYPE_APPLICATION_OVERLAY;
        screenSavarParams.x=0;
        screenSavarParams.y=0;
        screenSavarParams.format = PixelFormat.TRANSLUCENT;
        screenSavarParams.windowAnimations=android.R.style.Animation_InputMethod;
    }

    private float moveY;
    private float currentY=0.0f;
    private float postY=0.0f;
    private void eventHanded(){
        screenSaverView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TAG", "onTouch: "+event.getAction());
                Log.d("TAG", "onTouch: X:"+event.getX()+" Y:"+event.getY());
                //screenSaverView.setVisibility(View.GONE);
                if (event.getAction()== MotionEvent.ACTION_DOWN) {

                    moveY=event.getY();
                    postY=event.getY();
                    Log.d("TAG", "onTouch: dowm ");
                }
                currentY=event.getY();
                if (event.getAction()== MotionEvent.ACTION_UP) {
                    Log.d("TAG", "onTouch: up ");
                    screenSavarParams.y=0;
                    moveY=0;
                    mWindowManager.updateViewLayout(screenSaverView,screenSavarParams);
                }else {
                    int sum= (int) (event.getY()-moveY);
                    Log.d("TAG", "onTouch: sum"+(postY-currentY));
                    if (sum<0&&(postY-currentY)>1.0){//坐标移动超过1 更新
                        postY=event.getY();
                        screenSavarParams.y= (int) (event.getY()-moveY);
                        mWindowManager.updateViewLayout(screenSaverView,screenSavarParams);
                    }else if (sum<-170){
                        screenSaverView.setVisibility(View.GONE);
                    }
                }

                return false;
            }
        });


        binding.bDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (binding.txPassword.getText().toString().equals("")&& (binding.txPassword.getText() !=null)){
                    screenSaverView.setVisibility(View.VISIBLE);
                }
                binding.getNumber().deleteAllNumber();
                return false;
            }
        });

        binding.txPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("TAG", "afterTextChanged: "+s);
                if (s.toString().trim().equals("123456")){
                    errorView.setVisibility(View.GONE);
                    mView.setVisibility(View.GONE);
                    mContext.unregisterReceiver(mTimeReceiver);
                }else if (s.length()>=6&&!s.toString().trim().equals("123456")){
                    errorView.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(MSG_PASSWORD_EEROR,300);
                }
            }
        });
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==MSG_PASSWORD_EEROR){
                errorView.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void initBroadcast(){
        mTimeReceiver=new TimeReceiver(timeText,dataText);
        mTimeFilter=new IntentFilter();
        mTimeFilter.addAction(Intent.ACTION_TIME_TICK);
        mContext.registerReceiver(mTimeReceiver,mTimeFilter);
    }
}

