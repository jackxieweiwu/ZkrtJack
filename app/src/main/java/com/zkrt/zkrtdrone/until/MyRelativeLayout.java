package com.zkrt.zkrtdrone.until;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by jack_xie on 17-3-7.
 */

public class MyRelativeLayout extends RelativeLayout {
    private ControlViewBringToFront mControlViewBringToFront;

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
        mControlViewBringToFront = new ControlViewBringToFront(this);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return  mControlViewBringToFront.getChildDrawingOrder(childCount, i);
    }

    @Override
    public void bringChildToFront(View child) {
        //super.bringChildToFront(child);
        mControlViewBringToFront.bringChildToFront(this, child);
    }
}
