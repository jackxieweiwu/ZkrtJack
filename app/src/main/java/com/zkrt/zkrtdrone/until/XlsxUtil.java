package com.zkrt.zkrtdrone.until;
import android.os.Environment;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.exelBean;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.App;

/**
 * Created by jack_Xie on 17-3-26.
 */

public class XlsxUtil {
    private static String[] str = new String[]{"GPS_LAT","GPS_LNG","GPS_CO","GPS_H2S","GPS_NH3","GPS_CO2","GPS_TIME"};

    //exFileName name
    public static void writeExcel(String exFileName, String sheetName, List<exelBean> columnData) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(),App.app.getPackageName() + "/log");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dir2 = new File(Environment.getExternalStorageDirectory(),App.app.getPackageName() + "/log/exls");
            if (!dir2.exists()) {
                dir2.mkdirs();
            }
            String name = App.app.getPackageName() + "/log/exls"+"/"+exFileName+".xlsx";
            File file = new File(Environment.getExternalStorageDirectory(),name);
            if(!file.exists() && !file.createNewFile()) {
                return;
            }else{
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            setExcel(file,sheetName,columnData);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.setResultToToast(DJISampleApplication.activity,"Excel文件" + exFileName + "生成失败：" + e);
        }
    }

    private static void setExcel(File file ,String sheetName,List<exelBean> columnData) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        //新建excel
        HSSFWorkbook workBook = new HSSFWorkbook();
        //新建sheet
        HSSFSheet sheet = workBook.createSheet(sheetName);
        //创建单元格样式
        HSSFCellStyle style = getStyle(workBook);

        HSSFRow row2=sheet.createRow(0);
        for(int ji = 0; ji< str.length; ji++){
            row2.createCell(ji).setCellValue(str[ji]);
        }

        for (int i = 1; i <= columnData.size(); i++) {
            //创建行
            HSSFRow row = sheet.createRow(i);
            exelBean info = columnData.get(i-1);
            for (int j = 0; j < str.length; j++) {
                //创建列单元格
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(style);
                switch (j) {
                    case 0://时间
                        cell.setCellValue(info.getLat());
                        break;
                    case 1:
                        cell.setCellValue(info.getLog());
                        break;
                    case 2:
                        cell.setCellValue(info.getGasValueOne());
                        break;
                    case 3:
                        cell.setCellValue(info.getGasValueTwo());
                        break;
                    case 4:
                        cell.setCellValue(info.getGasValueThree());
                        break;
                    case 5:
                        cell.setCellValue(info.getGasValueFour());
                        break;
                    case 6:
                        cell.setCellValue(info.getTime());
                        break;
                }
            }
        }
        workBook.write(out);
        out.flush();
        out.close();
        Utils.setResultToToast(DJISampleApplication.activity,"Excel文件保存到 ：" + App.app.getPackageName() + "/log/exls");
    }

    private static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 设置单元格字体
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        style.setFont(font);

        return style;
    }
}
