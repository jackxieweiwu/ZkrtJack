package com.zkrt.zkrtdrone.comassis.serialport_help;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zkrt.zkrtdrone.bean.ComBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by root on 17-4-20.
 */

public class ClientThread implements Runnable
{
    //For debug
    private final String TAG = "ClientThread";

    private Socket socket;
    private String ip;
    private int port;
    private Handler receiveHandler;
    public Handler sendHandler;
    BufferedReader bufferedReader;
    private InputStream inputStream;
    private OutputStream outputStream;
    public boolean isConnect = false;

    public ClientThread(Handler handler, String ip, String port) {
        // TODO Auto-generated constructor stub
        this.receiveHandler = handler;
        this.ip = ip;
        this.port = Integer.valueOf(port);
        Log.d(TAG, "ClientThread's construct is OK!!");
    }
    public ClientThread()
    {
        Log.d(TAG, "It is may be construct's problem...");
    }

    public void run()
    {
        try
        {
            Log.d(TAG, "Into the run()");
            socket = new Socket(ip, port);
            isConnect = socket.isConnected();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            //To monitor if receive Msg from Server
            new Thread()
            {
                @Override
                public void run()
                {
                    byte[] buffer = new byte[1024];

                    final StringBuilder stringBuilder = new StringBuilder();
                    try
                    {
                        while(socket.isConnected())
                        {
                            int readSize = inputStream.read(buffer);
                            Log.d(TAG, "readSize:" + readSize);

                            //If Server is stopping
                            if(readSize == -1)
                            {
                                inputStream.close();
                                outputStream.close();
                            }
                            if(readSize == 0)continue;
                            if (readSize > 0){
                                ComBean ComRecData = new ComBean("26",buffer,readSize);

                                Message msg = new Message();
                                msg.what = 0x123;
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("ComRecData", ComRecData);
                                msg.setData(bundle);
                                receiveHandler.sendMessage(msg);
                            }
                        }
                    }
                    catch(IOException e)
                    {
                        Log.d(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }

            }.start();

            //To Send Msg to Server
            Looper.prepare();
            sendHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    if (msg.what == 0x852)
                    {
                        try
                        {
                            outputStream.write((msg.obj.toString() + "\r\n").getBytes());
                            outputStream.flush();
                        }
                        catch (Exception e)
                        {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();

        } catch (SocketTimeoutException e)
        {
            // TODO Auto-generated catch block
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }catch (UnknownHostException e)
        {
            // TODO Auto-generated catch block
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
