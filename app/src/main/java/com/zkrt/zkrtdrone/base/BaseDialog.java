package com.zkrt.zkrtdrone.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by jack_xie on 16-12-26.
 */

public abstract class BaseDialog extends Dialog {
    private Context mContext;
    private int layoutRes;

    public BaseDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public BaseDialog(Context context,int theme, int resLayout){
        super(context,theme);
        this.mContext = context;
        this.layoutRes=resLayout;
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutId(getDialogFindById());
        setContentView(view);
        initView(view);
    }

    protected abstract void initView(View view);

    public abstract int getDialogFindById();

    public View getLayoutId(int layoutRes){
        return LayoutInflater.from(getContext()).inflate(layoutRes,null);
    }
}
