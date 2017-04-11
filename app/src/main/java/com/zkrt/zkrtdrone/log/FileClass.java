package com.zkrt.zkrtdrone.log;

import android.os.Environment;

import com.zkrt.zkrtdrone.bean.DeviceCallBackTwo;
import com.zkrt.zkrtdrone.bean.DeviceCallback;
import com.zkrt.zkrtdrone.until.TimeUtil;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import dji.thirdparty.gson.Gson;
import dji.thirdparty.gson.GsonBuilder;
import zkrtdrone.zkrt.com.maplib.App;

/**
 * Created by jack_xie on 16-12-19.
 */

public class FileClass{

    public static String gsonData(DeviceCallback deviceCallback){
        GsonBuilder builder=new GsonBuilder();
        Gson gson=builder.create();
        return gson.toJson(deviceCallback, DeviceCallback.class);
    }

    public static String gsonData1(DeviceCallBackTwo deviceCallback){
        GsonBuilder builder=new GsonBuilder();
        Gson gson=builder.create();
        return gson.toJson(deviceCallback, DeviceCallback.class);
    }

    public static void saveFileLog(String json){
        try {
            String time2 =TimeUtil.getTimeDate2();
            String time = TimeUtil.getTimeDate();
            String fileName = "gases_" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = App.app.getPackageName() + "/log/error/"+time2;
                File dir = new File(Environment.getExternalStorageDirectory(), path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
                fos.write(json.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    public static void saveFile(String json){
        try {
            String time2 =TimeUtil.getTimeDate2();
            String time = TimeUtil.getTimeDate();
            String fileName = "gases_" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = App.app.getPackageName() + "/log/gases/"+time2;
                File dir = new File(Environment.getExternalStorageDirectory(), path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
                fos.write(json.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    public static void saveFile2(String json){
        try {
            String time2 =TimeUtil.getTimeDate2();
            String fileName = "camera.log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = App.app.getPackageName() + "/log/camera/"+time2;
                File dir = new File(Environment.getExternalStorageDirectory(), path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
                fos.write(json.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
        }
    }


    /**
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
     *
     */
    public static void method1(String file, String conent) {
        File dir = new File(Environment.getExternalStorageDirectory(), file);
        File fileFIle = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        BufferedWriter out = null;
        try {
            File f=new File(file+"/test.txt");
            if(!f.exists()){
                fileFIle = new File(dir,"text.txt");
            }

            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileFIle, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void method2(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加文件：使用RandomAccessFile
     *
     * @param fileName
     *            文件名
     * @param content
     *            追加的内容
     */
    public static void method3(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
