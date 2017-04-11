package com.zkrt.zkrtdrone.until;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jack_xie on 2016/10/10.
 * 时间换算工具类   又秒转  时分秒
 */

public class Time_Date {

    public static String cal(int second){
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second%3600;
        if(second>3600){
            h= second/3600;
            if(temp!=0){
                if(temp>60){
                    d = temp/60;
                    if(temp%60!=0){
                        s = temp%60;
                    }
                }else{
                    s = temp;
                }
            }
        }else{
            d = second/60;
            if(second%60!=0){
                s = second%60;
            }
        }

        return h+":"+d+":"+s;
    }

    public static String getTime(){
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        String nameTime = year+"-"+month+"-"+day;
        return nameTime;
    }

    public static String getDateTimeFile(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
