package com.zkrt.zkrtdrone.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.LogAdapter;
import com.zkrt.zkrtdrone.base.BaseDialog;
import com.zkrt.zkrtdrone.bean.DeviceCallback;
import com.zkrt.zkrtdrone.bean.LogRz;
import com.zkrt.zkrtdrone.listviewitems.BarChartItem;
import com.zkrt.zkrtdrone.listviewitems.ChartItem;
import com.zkrt.zkrtdrone.listviewitems.LineChartItem;
import com.zkrt.zkrtdrone.listviewitems.PieChartItem;
import com.zkrt.zkrtdrone.until.HexToBinary;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack_xie on 17-3-14.
 */

public class ChartRzDialog extends BaseDialog{
    //private RecyclerView list_rv;
    private ListView rz_visbir;
    private ListView rz_list_day;
    private Activity activity;
    private LogAdapter logAdapter;//logAdapter2;
    private ArrayList<ChartItem> listChart = new ArrayList<ChartItem>();
    private ChartDataAdapter cda;
    private List<LogRz> listone;
    //private List<FileBean> mDatas;
    //private List<DeviceCallback> listDevice;

    public ChartRzDialog(Context context,int theme, int resLayout){
        super(context,theme,resLayout);
        this.activity =  (Activity) context;
        logAdapter = new LogAdapter(activity);
        /*logAdapter2 = new LogAdapter(activity);*/
        //listDevice = new ArrayList<>();
        cda = new ChartDataAdapter(activity,0);
        //mDatas = new ArrayList<FileBean>();

    }

    public void setData(List<LogRz> listones){
        //mDatas.clear();
        this.listone = listones;
        logAdapter.setList(listones);
        logAdapter.notifyDataSetChanged();
        /*for(int i=0;i<listones.size();i++){
            getListDroneData(i,listones.get(i));
        }
        setListAdapter();*/
    }

    @Override
    protected void initView(View view) {
        rz_visbir = (ListView) view.findViewById(R.id.rz_visbir);
        rz_list_day = (ListView) view.findViewById(R.id.rz_list_day);
        rz_list_day.setAdapter(logAdapter);
        rz_visbir.setAdapter(cda);
        rz_list_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getListDroneData(listone.get(position));
            }
        });
    }

    private void getListDroneData(LogRz logRz){
        //List<LogRz> listtwo = CommonSettingModel.getFileDir(logRz.getItemPath(),false);
        /*for(int ii=0;ii<listtwo.size();ii++){
            mDatas.add(new FileBean(ii+1,i,logRz.getItemName()));
        }*/
        listChart.clear();
        DeviceCallback de = readFile(logRz.getItemPath());
        if(de != null){
            List<Integer> listDev = analysisData(de);
            listChart.add(new LineChartItem(generateDataLine(listDev), activity));
            listChart.add(new BarChartItem(generateDataBar(listDev), activity));
            listChart.add(new PieChartItem(generateDataPie(listDev), activity));
        }
        /*for (LogRz l: listtwo){
            DeviceCallback de = readFile(l.getItemPath());
            if(de != null){
                List<Integer> listDev = analysisData(de);
                listChart.add(new LineChartItem(generateDataLine(listDev), activity));
                listChart.add(new BarChartItem(generateDataBar(listDev), activity));
                listChart.add(new PieChartItem(generateDataPie(listDev), activity));
            }
        }*/
        cda.addAll(listChart);
    }

    private void setListAdapter(){
        /*try {
            SimpleTreeRecycleViewAdapter adapter = new SimpleTreeRecycleViewAdapter(activity, mDatas, 1);
            list_rv.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            list_rv.setAdapter(adapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
    }

    //解析数据
    private List<Integer> analysisData(DeviceCallback de){
        List<Integer> listS = new ArrayList<>();
        listS.add(HexToBinary.HexTo10Integer(de.getDeviceCallBackData().getTemperatureOne()) / 10);  //温度   温度1值
        listS.add(HexToBinary.HexTo10Integer(de.getDeviceCallBackData().getTemperatureTwo())/10);  //温度   温度2值
        listS.add(HexToBinary.HexTo10Integer(de.getDeviceCallBackData().getGasValueOne()));  //气体 CO
        listS.add(HexToBinary.HexTo10Integer(de.getDeviceCallBackData().getGasValueTwo()) / 100);  // H2S /100
        listS.add(HexToBinary.HexTo10Integer(de.getDeviceCallBackData().getGasValueThree()));  //NH3，
        listS.add(HexToBinary.HexTo10Integer(de.getDeviceCallBackData().getGasValueFour()));  //CO2
        return listS;
    }

    @Override
    public int getDialogFindById() {
        return R.layout.rzdialog;
    }

    public DeviceCallback readFile(String path){
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis,"GB2312");//文件编码Unicode,UTF-8,ASCII,GB2312,Big5
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char)ch);
            }
            in.close();
            return getGsonJson(buffer.toString());
        } catch (IOException e) {
            Toast.makeText(activity,"文件不存在!",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public DeviceCallback getGsonJson(String jsonData){
        Gson gson=new Gson();
        return gson.fromJson(jsonData,DeviceCallback.class);
    }

    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }
    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine(List<Integer> listDev) {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        for (int i = 0; i < listDev.size(); i++) {
            e1.add(new Entry(listDev.get(i), i));
            LineDataSet d1,d2,d3,d4,d5,d6;
            if(i == 0){
                d1 = new LineDataSet(e1, "温度1");
                d1.setValueTextColor(Color.WHITE);
                d1.setLineWidth(2.5f);
                d1.setCircleRadius(4.5f);
                d1.setHighLightColor(Color.rgb(244, 117, 117));
                d1.setDrawValues(false);
                sets.add(d1);
            }else if(i == 1){
                d2 = new LineDataSet(e1, "温度2");
                d2.setValueTextColor(Color.WHITE);
                d2.setLineWidth(2.5f);
                d2.setCircleRadius(4.5f);
                d2.setHighLightColor(Color.rgb(244, 117, 117));
                d2.setDrawValues(false);
                sets.add(d2);
            }else if(i == 2){
                d3 = new LineDataSet(e1, "CO");
                d3.setValueTextColor(Color.WHITE);
                d3.setLineWidth(2.5f);
                d3.setCircleRadius(4.5f);
                d3.setHighLightColor(Color.rgb(244, 117, 117));
                d3.setDrawValues(false);
                sets.add(d3);
            }else if(i == 3){
                d4 = new LineDataSet(e1, "H2S");
                d4.setValueTextColor(Color.WHITE);
                d4.setLineWidth(2.5f);
                d4.setCircleRadius(4.5f);
                d4.setHighLightColor(Color.rgb(244, 117, 117));
                d4.setDrawValues(false);
                sets.add(d4);
            }else if(i == 4){
                d5 = new LineDataSet(e1, "NH3");
                d5.setValueTextColor(Color.WHITE);
                d5.setLineWidth(2.5f);
                d5.setCircleRadius(4.5f);
                d5.setHighLightColor(Color.rgb(244, 117, 117));
                d5.setDrawValues(false);
                sets.add(d5);
            }else if(i == 5){
                d6 = new LineDataSet(e1, "CO2");
                d6.setValueTextColor(Color.WHITE);
                d6.setLineWidth(2.5f);
                d6.setCircleRadius(4.5f);
                d6.setHighLightColor(Color.rgb(244, 117, 117));
                d6.setDrawValues(false);
                sets.add(d6);
            }
        }


        /*for (int i = 0; i < listDev.size(); i++) {
            e1.add(new Entry(Float.parseFloat(listDev.get(i)), i));
        }

        LineDataSet d2 = new LineDataSet(e1, "温度2");
        d2.setValueTextColor(Color.WHITE);
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setDrawValues(false);*/

        //sets.add(d2);

        LineData cd = new LineData(getQuarters(), sets);
        cd.setValueTextColor(Color.WHITE);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(List<Integer> listDev) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < listDev.size(); i++) {
            entries.add(new BarEntry(listDev.get(i), i));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + 1);
        d.setValueTextColor(Color.WHITE);
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(getQuarters(), d);
        cd.setValueTextColor(Color.WHITE);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(List<Integer> listDev) {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < listDev.size(); i++) {
            entries.add(new Entry(listDev.get(i), i));
        }

        PieDataSet d = new PieDataSet(entries, "");
        d.setValueTextColor(Color.WHITE);
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(getQuarters(), d);
        cd.setValueTextColor(Color.WHITE);
        return cd;
    }

    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("温度1");
        q.add("温度2");
        q.add("CO");
        q.add("H2S");
        q.add("NH3");
        q.add("CO2");
        return q;
    }
}
