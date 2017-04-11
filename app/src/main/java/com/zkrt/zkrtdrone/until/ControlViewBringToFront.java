package com.zkrt.zkrtdrone.until;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jack_xie on 17-3-7.
 */

public class ControlViewBringToFront {
    private int mFocusChildIndex;


    public ControlViewBringToFront(ViewGroup vg){
        //设置这个方法后， 子view可以绘制在父控件的padding区域之上， 否则绘制在padding区域之下
        vg.setClipChildren(true);
        vg.setClipToPadding(true);
    }

    public void bringChildToFront(ViewGroup viewGroup, View childView){
        mFocusChildIndex = viewGroup.indexOfChild(childView);
        if(mFocusChildIndex != -1){
            viewGroup.postInvalidate();
        }
    }

    /**
     * 此函数 dispatchDraw 中调用. <br>
     * 原理就是和最后一个要绘制的view，交换了位置. <br>
     * 因为dispatchDraw最后一个绘制的view是在最上层的. <br>
     * 这样就避免了使用 bringToFront 导致焦点错乱问题. <br>
     */
    public int getChildDrawingOrder(int childCount, int i){
        if(mFocusChildIndex != -1){

            if(i == childCount-1){
                if(mFocusChildIndex > childCount-1){
                    mFocusChildIndex = childCount-1;
                }
                return mFocusChildIndex;
            }

            if(i == mFocusChildIndex){

                return childCount -1;
            }

        }
        if(childCount <= i){
            i = childCount-1;
        }
        return i;
    }
}
