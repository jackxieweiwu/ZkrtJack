package com.zkrt.zkrtdrone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.bean.LogRz;
import com.zkrt.zkrtdrone.bean.MoudleBean;

import java.util.List;

import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseAdapter;

/**
 * Created by jack_xie on 2016/10/18.
 */

public class LogAdapter extends BaseAdapter<LogRz> {
    private Context mContext;
    private int selectPic = -1;
    public LogAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getContentView() {
        return android.R.layout.simple_list_item_1;
    }

    public void setNotifyDataChange(int position){
        selectPic = position;
        super.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onInitView(View view, int position) {
        LogRz log = getItem(position);
        TextView txt_moudle = get(view, android.R.id.text1);
        txt_moudle.setSingleLine(true);
        txt_moudle.setTextColor(Color.WHITE);
        txt_moudle.setText(log.getItemName());
    }
}
