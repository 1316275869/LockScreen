package cn.flyaudio.screenlock.modle;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import cn.flyaudio.screenlock.BR;


public class PasswordNumber extends BaseObservable {
    private String number="";

    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {

        this.number += number;
        Log.d("TAG", "setNumber: "+this.number);
        notifyPropertyChanged(BR.number);
    }

    public void deleteNumber(){
        if (this.number.length()>0){
             this.number=this.number.substring(0,this.number.length()-1);
            Log.d("TAG", "deleteNumber: "+this.number);
            notifyPropertyChanged(BR.number);
        }
    }
    public void deleteAllNumber(){
       this.number="";
       notifyPropertyChanged(BR.number);
    }
}
