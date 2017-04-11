package com.zkrt.zkrtdrone.bean;

/**
 * Created by jack_xie on 17-3-12.
 */

public class LogRz {
    private String itemPath;  //存放路径
    private String itemName;  //存放名称

    public LogRz() {
    }

    public LogRz(String itemPath, String itemName) {
        this.itemPath = itemPath;
        this.itemName = itemName;
    }

    public String getItemPath() {
        return itemPath;
    }

    public void setItemPath(String itemPath) {
        this.itemPath = itemPath;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
