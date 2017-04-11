package com.zkrt.zkrtdrone.until;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import zkrtdrone.zkrt.com.maplib.App;

/**
 * Created by jack_xie on 17-2-9.
 */

public class AssetUtil {
    public static void copyFile(String assetFile, String destFile){
        InputStream is = null;
        OutputStream out = null;
        try {
            is = App.app.getAssets().open(assetFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[8192];
            int count = 0;
            while((count = is.read(buffer)) > 0){
                out.write(buffer, 0, count);
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }
}
