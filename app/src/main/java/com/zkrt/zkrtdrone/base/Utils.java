package com.zkrt.zkrtdrone.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zkrt.zkrtdrone.R;

import java.util.Timer;
import java.util.TimerTask;

import dji.common.error.DJIError;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.UITask;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.rxutil.RxjavaUtil;

/**
 * Created by jack_xie on 15/12/18.
 */



public class Utils {
    public static final double ONE_METER_OFFSET = 0.00000899322;
    private static long lastClickTime;
    private static  Timer mOffTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void showDialogBasedOnError(Context ctx, DJIError djiError) {
        if (null == djiError)
            DJIDialog.showDialog(ctx, "Success");
        else
            DJIDialog.showDialog(ctx, djiError.getDescription());
    }


    public static void setResultToToast(final Context context, final String string) {
        RxjavaUtil.doInUIThread(new UITask<String>(string) {
            @Override
            public void doInUIThread() {
                Toast.makeText(context, string+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setResultToToastStringId(final Context context, final int stringid) {
        RxjavaUtil.doInUIThread(new UITask<Integer>(stringid) {
            @Override
            public void doInUIThread() {
                Toast.makeText(context, context.getResources().getString(stringid), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setResultToToast2(final Context context, final String string, final int color, final int imgBg) {
        RxjavaUtil.doInUIThread(new UITask<String>() {
            @Override
            public void doInUIThread() {
                Toast toast = new Toast(context);
                LayoutInflater inflater = LayoutInflater
                        .from(context);
                View view = inflater.inflate(R.layout.toast,null);
                ImageView toast_img = (ImageView) view.findViewById(R.id.img_toast);
                TextView toast_txt = (TextView) view.findViewById(R.id.txt_toast);
                if(imgBg != 0){
                    toast_img.setBackgroundResource(imgBg);
                }
                toast_txt.setText(string);
                toast_txt.setTextColor(color);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(view);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    public static void setResultToText(final Context context, final TextView tv, final String s) {
        RxjavaUtil.doInUIThread(new UITask<String>() {
            @Override
            public void doInUIThread() {
                if (tv == null) {
                    Toast.makeText(context, "tv is null", Toast.LENGTH_SHORT).show();
                } else {
                    tv.setText(s);
                }
            }
        });
    }

    public static boolean checkGpsCoordinate(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    public static double Radian(double x){
        return  x * Math.PI / 180.0;
    }

    public static double Degree(double x){
        return  x * 180 / Math.PI ;
    }

    public static double cosForDegree(double degree) {
        return Math.cos(degree * Math.PI / 180.0f);
    }

    public static double calcLongitudeOffset(double latitude) {
        return ONE_METER_OFFSET / cosForDegree(latitude);
    }

    public static void addLineToSB(StringBuffer sb, String name, Object value) {
        if (sb == null) return;
        sb.
                append(name == null ? "" : name + ": ").
                append(value == null ? "" : value + "").
                append("\n");
    }

    /*public static void setDialogToShow(final Context context, final String name){
        TextView mOffTextView = new TextView(context);
        mOffTextView.setText(name);
        Dialog mDialog = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setCancelable(false)
                .setView(mOffTextView)
                *//*.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mOffTime.cancel();
                        //off();////关闭后的一些操作
                    }
                })*//*
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        mOffTime.cancel();
                    }
                })
                .create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);

        mOffTime = new Timer(true);
        TimerTask tt = new TimerTask() {
            int countTime = 60;
            public void run() {
                if (countTime > 0) {
                    countTime--;
                }
                Message msg = new Message();
                msg.what = countTime;
                mUIHandler.sendMessage(msg);
            }
        };
        mOffTime.schedule(tt, 1000, 1000);
    }*/
}
