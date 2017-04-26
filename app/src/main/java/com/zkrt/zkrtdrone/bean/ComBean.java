package com.zkrt.zkrtdrone.bean;

import java.io.Serializable;

/**
 * @author
 *串口数据
 */
public class ComBean implements Serializable {
	public byte[] bRec=null;
	public String sComPort="";
	public ComBean(String sPort,byte[] buffer,int size){
		sComPort=sPort;
		bRec=new byte[size];
		for (int i = 0; i < size; i++)
		{
			bRec[i]=buffer[i];
		}
		/*SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
		sRecTime = sDateFormat.format(new java.util.Date());*/
	}

	public byte[] getbRec() {
		return bRec;
	}

	public void setbRec(byte[] bRec) {
		this.bRec = bRec;
	}

	public String getsComPort() {
		return sComPort;
	}

	public void setsComPort(String sComPort) {
		this.sComPort = sComPort;
	}
}