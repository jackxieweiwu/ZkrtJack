package com.zkrt.zkrtdrone.comassis.serialport_help;

import com.zkrt.zkrtdrone.bean.ComBean;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import android_serialport_api.SerialPort;


/**
 * @author
 *串口辅助工具类
 */
public abstract class SerialHelper{
	private SerialPort mSerialPort;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	//private ReadThread mReadThread;
	private String sPort="/dev/s3c2410_serial0";
	private int iBaudRate=9600;
	private boolean _isOpen=false;
	private byte[] _bLoopData=new byte[]{0x30};
	private int iDelay=500;
	//----------------------------------------------------
	public SerialHelper(String sPort,int iBaudRate){
		this.sPort = sPort;
		this.iBaudRate=iBaudRate;
	}
	public SerialHelper(){
		this("/dev/ttyS1",9600);
	}
	public SerialHelper(String sPort){
		this(sPort,9600);
	}
	public SerialHelper(String sPort,String sBaudRate){
		this(sPort,Integer.parseInt(sBaudRate));
	}
	//----------------------------------------------------
	public void open() throws SecurityException, IOException,InvalidParameterException{
		mSerialPort =  new SerialPort(new File(sPort), iBaudRate, 0);
		mOutputStream = mSerialPort.getOutputStream();
		mInputStream = mSerialPort.getInputStream();
		/*mReadThread = new ReadThread();
		mReadThread.start();*/
		setStartThread();
		_isOpen=true;
	}
	//----------------------------------------------------
	public void close(){
		/*if (mReadThread != null)
			mReadThread.interrupt();*/
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
		_isOpen=false;
	}
	//----------------------------------------------------
	public void send(byte[] bOutArray){
		try{
			mOutputStream.write(bOutArray);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	//----------------------------------------------------
	public void sendHex(String sHex){
		byte[] bOutArray = MyFunc.HexToByteArr(sHex);
		send(bOutArray);
	}
	//----------------------------------------------------
	public void sendTxt(String sTxt){
		byte[] bOutArray =sTxt.getBytes();
		send(bOutArray);
	}
	//----------------------------------------------------
	private void setStartThread(){
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(DJIModuleVerificationUtil.isFlightControllerAvailable()) {
					try{
						if (mInputStream == null) return;
						byte[] buffer=new byte[512];
						int size = mInputStream.read(buffer);
						if (size > 0){
							ComBean ComRecData = new ComBean(sPort,buffer,size);
							onDataReceived(ComRecData);
						}
					} catch (Throwable e){
						e.printStackTrace();
						return;
					}
				}
			}
		}, 1, 100, TimeUnit.MILLISECONDS);  //SECONDS
	}

	//----------------------------------------------------
	public int getBaudRate()
	{
		return iBaudRate;
	}
	public boolean setBaudRate(int iBaud)
	{
		if (_isOpen)
		{
			return false;
		} else
		{
			iBaudRate = iBaud;
			return true;
		}
	}
	public boolean setBaudRate(String sBaud)
	{
		int iBaud = Integer.parseInt(sBaud);
		return setBaudRate(iBaud);
	}
	//----------------------------------------------------
	public String getPort()
	{
		return sPort;
	}
	public boolean setPort(String sPort)
	{
		if (_isOpen)
		{
			return false;
		} else
		{
			this.sPort = sPort;
			return true;
		}
	}
	//----------------------------------------------------
	public boolean isOpen()
	{
		return _isOpen;
	}
	//----------------------------------------------------
	public byte[] getbLoopData()
	{
		return _bLoopData;
	}
	//----------------------------------------------------
	public void setbLoopData(byte[] bLoopData)
	{
		this._bLoopData = bLoopData;
	}
	//----------------------------------------------------
	public void setTxtLoopData(String sTxt){
		this._bLoopData = sTxt.getBytes();
	}
	//----------------------------------------------------
	public void setHexLoopData(String sHex){
		this._bLoopData = MyFunc.HexToByteArr(sHex);
	}
	//----------------------------------------------------
	public int getiDelay()
	{
		return iDelay;
	}
	//----------------------------------------------------
	public void setiDelay(int iDelay)
	{
		this.iDelay = iDelay;
	}
	//----------------------------------------------------
	/*public void startSend()
	{
		if (mSendThread != null)
		{
			mSendThread.setResume();
		}
	}
	//----------------------------------------------------
	public void stopSend()
	{
		if (mSendThread != null)
		{
			mSendThread.setSuspendFlag();
		}
	}*/
	//----------------------------------------------------
	protected abstract void onDataReceived(ComBean ComRecData);
}