package com.zkrt.zkrtdrone.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.MoudleBean;

import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseAdapter;

/**
 * Created by jack_xie on 2016/10/18.
 */

public class MoudleAdapter extends BaseAdapter<MoudleBean> {
    private Context mContext;
    private int selectPic = -1;
    public MoudleAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getContentView() {
        return R.layout.moudle_item;
    }

    public void setNotifyDataChange(int position){
        selectPic = position;
        super.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onInitView(View view, int position) {
        MoudleBean moudleBean = getItem(position);
        LinearLayout linearLayout = get(view, R.id.linear_moudle);
        ImageView img_moudle = get(view, R.id.img_moudle);
        TextView txt_moudle = get(view, R.id.txt_moudle);
        if(selectPic == position){
            linearLayout.setBackgroundResource(R.drawable.moudl_bg);
        }else{
            linearLayout.setBackgroundResource(android.R.color.transparent);
        }
        img_moudle.setBackground(moudleBean.getBitmap());
        txt_moudle.setText(moudleBean.getName());
    }
}
