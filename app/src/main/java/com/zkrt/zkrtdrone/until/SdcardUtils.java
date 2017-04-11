package com.zkrt.zkrtdrone.until;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by jack_xie on 17-2-9.
 */

public class SdcardUtils {
    /**
     * @author chenshiqiang
     * Description: 判断SD卡是否存在
     * @return
     */
    public static boolean isSdcardExist() {
        return Environment.getExternalStorageState().
                equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * @author chenshiqiang
     * Description: 获取SD卡路径
     * @return
     */
    public static String getSdcardpath() {
        String sdcardStr = "";
        if (isSdcardExist()) {
            sdcardStr = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return sdcardStr;
    }

    /**
     * @author chenshiqiang
     * Description: 获得sdcard剩余空间
     * @return
     */
    public static long getSdcardAvailableSize() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            @SuppressWarnings("deprecation")
            long blockSize = sf.getBlockSize();
            @SuppressWarnings("deprecation")
            long availableCount = sf.getAvailableBlocks();
            return availableCount * blockSize;
        }else {
            return 0;
        }
    }

    public static boolean isCanDown(long size){
        long ava = getSdcardAvailableSize();
        if(ava > size){
            return true;
        }
        return false;
    }
}
