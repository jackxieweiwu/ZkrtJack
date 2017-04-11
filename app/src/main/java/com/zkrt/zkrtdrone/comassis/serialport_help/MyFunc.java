package com.zkrt.zkrtdrone.comassis.serialport_help;


import com.zkrt.zkrtdrone.bean.ComAssis;

/**
 * @author
 *数据转换工具
 */
public class MyFunc {
	private static StringBuilder stringBuffer = new StringBuilder();
	//-------------------------------------------------------
	// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
	static public int isOdd(int num)
	{
		return num & 0x1;
	}
	//-------------------------------------------------------
	static public int HexToInt(String inHex)//Hex字符串转int
	{
		return Integer.parseInt(inHex, 16);
	}
	//-------------------------------------------------------
	static public byte HexToByte(String inHex)//Hex字符串转byte
	{
		return (byte)Integer.parseInt(inHex,16);
	}
	//-------------------------------------------------------
	static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
	{
		return String.format("%02x", inByte).toUpperCase();
	}
	//-------------------------------------------------------
	/*static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
	{
		StringBuilder strBuilder=new StringBuilder();
		int j=inBytArr.length;
		for (int i = 0; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString();
	}*/


	static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
	{
		String strBuilder = "";
		int j=inBytArr.length;
		for (int i = 0; i < j; i++){
			strBuilder = setByteString(Byte2Hex(inBytArr[i]));
			/*strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");*/
		}
		return strBuilder.toString();
	}

	private static String setByteString(String name){
		if(stringBuffer.length()>=24){
			stringBuffer.setLength(0);
		}
		if(stringBuffer.length()==0 && !name.equals("FF")){
			stringBuffer.append("FF");
			stringBuffer.append(" ");
		}
		stringBuffer.append(name);
		stringBuffer.append(" ");

		if(stringBuffer.length()==24){
			String[] rea = stringBuffer.toString().split(" ");
			if(rea[0].equals("FF")){
				String as = Integer.toHexString(HexToInt(rea[1]) + HexToInt(rea[2])
						+ HexToInt(rea[3]) + HexToInt(rea[4]) + HexToInt(rea[5]) + HexToInt(rea[6]));
				byte low = (byte)(HexToByte(as) & 0xff);
				if(Byte2Hex(low).equals(rea[7])){
					return stringBuffer.toString();
				}
			}else{
				stringBuffer.setLength(0);
			}
		}
		return "0";
	}


	//解析HEX字符取出来
	static public ComAssis HexToString(String name){
		if(!name.equals("0")) {
			String[] re = name.split(" ");
			//取遥感上下
			int upOrDownNum = Integer.parseInt(re[2].toString(), 16);  //值越大越上
			//取遥感左右
			int leftOrRightNum = Integer.parseInt(re[1].toString(), 16);  //值越大越左
			//取遥感变焦值
			int zoomNum = Integer.parseInt(re[3].toString(), 16);

			//取按钮的值
			String buttonnum = hexString2binaryString(re[5]);
			int nighteBtn = Integer.parseInt(buttonnum.substring(0, 1));
			int sevenBtn = Integer.parseInt(buttonnum.substring(1, 2));
			int sixBtn = Integer.parseInt(buttonnum.substring(2, 3));
			int fiveBtn = Integer.parseInt(buttonnum.substring(3, 4));
			int fourBtn = Integer.parseInt(buttonnum.substring(4, 5));
			int threeBtn = Integer.parseInt(buttonnum.substring(5, 6));
			int twoBtn = Integer.parseInt(buttonnum.substring(6, 7));
			int oneBtn = Integer.parseInt(buttonnum.substring(7, 8));
			return new ComAssis(upOrDownNum, leftOrRightNum, oneBtn, twoBtn, threeBtn
					, fourBtn, fiveBtn, sixBtn, sevenBtn, nighteBtn, zoomNum);
		}
		return null;
	}

	//十六进制转二进制
	static public String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++){
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(hexString
					.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	//-------------------------------------------------------
	static public String ByteArrToHex(byte[] inBytArr,int offset,int byteCount)//字节数组转转hex字符串，可选长度
	{
		StringBuilder strBuilder=new StringBuilder();
		int j=byteCount;
		for (int i = offset; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
		}
		return strBuilder.toString();
	}

	//-------------------------------------------------------
	//转hex字符串转字节数组
	static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
	{
		int hexlen = inHex.length();
		byte[] result;
		if (isOdd(hexlen)==1)
		{//奇数
			hexlen++;
			result = new byte[(hexlen/2)];
			inHex="0"+inHex;
		}else {//偶数
			result = new byte[(hexlen/2)];
		}
		int j=0;
		for (int i = 0; i < hexlen; i+=2)
		{
			result[j]=HexToByte(inHex.substring(i,i+2));
			j++;
		}
		return result;
	}
}