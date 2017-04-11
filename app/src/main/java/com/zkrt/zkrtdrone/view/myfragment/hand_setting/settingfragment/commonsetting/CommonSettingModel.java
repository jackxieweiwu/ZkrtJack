package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting;


import android.os.Environment;

import com.zkrt.zkrtdrone.bean.LogRz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.App;

/**
 * Created by root on 17-2-21.
 */

public class CommonSettingModel implements CommonSettingContract.Model {

    public static List<LogRz> getListItemsName() {  //存放名称
        return pathName(true);
    }

    public static List<LogRz> getListItemsName2() {  //存放名称
        return pathName2(true);
    }

    private static List<LogRz> pathName(boolean bool){
        String rootPath = Environment.getExternalStorageDirectory().toString()+"/com.zkrt.zkrtdrone/log/gases/";
        return getFileDir(rootPath,bool);//获取rootPath目录下的文件.
    }

    private static List<LogRz> pathName2(boolean bool){
        String rootPath = Environment.getExternalStorageDirectory().toString()+ App.app.getPackageName() + "/log/exls";
        return getFileDir(rootPath,bool);//获取rootPath目录下的文件.
    }

    public static List<LogRz> getFileDir(String filePath, boolean bool) {
         List<LogRz> items = new ArrayList<LogRz>();
        try{
            File f = new File(filePath);
            File[] files = f.listFiles();// 列出所有文件
            // 如果不是根目录,则列出返回根目录和上一目录选项
            if (!filePath.equals(filePath)) {
                LogRz log = new LogRz();
                log.setItemPath(filePath);
                log.setItemName(f.getParent());
                items.add(log);
            }
            // 将所有文件存入list中
            if(files != null){
                int count = files.length;// 文件个数
                items.clear();
                for (int i = 0; i < count; i++) {
                    File file = files[i];
                    if (bool) {
                        LogRz log = new LogRz();
                        log.setItemPath(file.getPath());
                        log.setItemName(file.getName());
                        items.add(log);
                    }else{
                        LogRz log = new LogRz();
                        log.setItemPath(file.getPath());
                        log.setItemName(file.getName());
                        items.add(log);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return items;
    }
}
