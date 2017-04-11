package com.zkrt.zkrtdrone.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseAdapter;

/**
 * Created by jack_xie on 17-3-29.
 */

public class SimpleAdapter extends BaseAdapter<String> {

    public SimpleAdapter(Context context) {
        super(context);
    }

    @Override
    public int getContentView() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onInitView(View view, int position) {
        String name = getItem(position);
        TextView txt_moudle = get(view, android.R.id.text1);
        txt_moudle.setSingleLine(true);
        txt_moudle.setTextColor(Color.WHITE);
        txt_moudle.setText(name+"");
    }
}
