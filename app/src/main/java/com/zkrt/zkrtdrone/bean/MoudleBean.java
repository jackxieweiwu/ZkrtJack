package com.zkrt.zkrtdrone.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by jack_xie on 2016/10/18.
 */

public class MoudleBean {
    private Drawable bitmap;  //图片
    private String name; //名称
    private boolean status;  //状态

    public MoudleBean() {
    }

    public MoudleBean(Drawable bitmap, String name, boolean status) {
        this.bitmap = bitmap;
        this.name = name;
        this.status = status;
    }

    public Drawable getBitmap() {
        return bitmap;
    }

    public void setBitmap(Drawable bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
