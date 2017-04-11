package com.zkrt.zkrtdrone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zkrt.zkrtdrone.view.layout.flash.FlashActivity;

/**
 * Created by jack_xie on 17-3-19.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    //重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        /*//后边的XXX.class就是要启动的服务
        Intent service = new Intent(context, FlashActivity.class);
        context.startService(service);
        Log.v("TAG", "开机自动服务自动启动.....");*/
        //启动应用，参数为需要自动启动的应用的包名
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {     // boot
            Intent intent2 = new Intent(context, FlashActivity.class);
//          intent2.setAction("android.intent.action.MAIN");
//          intent2.addCategory("android.intent.category.LAUNCHER");
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }

    }
}
