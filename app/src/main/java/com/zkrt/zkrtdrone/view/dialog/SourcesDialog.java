package com.zkrt.zkrtdrone.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.GridVIewAdapter;
import com.zkrt.zkrtdrone.adapter.LogAdapter;
import com.zkrt.zkrtdrone.bean.LogRz;
import com.zkrt.zkrtdrone.bean.exelBean;
import com.zkrt.zkrtdrone.until.XlsxUtil;

import org.alex.dialog.base.BaseDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.App;


/**
 * Created by root on 17-3-30.
 */

public class SourcesDialog extends BaseDialog {
    private ListView ef_form;
    //private ListView list_sources_dialog;
    private GridVIewAdapter logAdapter;
    private LineChart chart_sources_excel;
    private LinearLayout linear_excel;
    private LogAdapter logAdaptera;
    private List<LogRz> listItemsName2;
    private Context mContext;
    public SourcesDialog(Context context) {
        super(context);
        this.mContext=context;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.sources_dialog;
    }

    @Override
    public void onCreateData() {
        logAdapter = new GridVIewAdapter(context);
        logAdaptera = new LogAdapter(context);
        ef_form = (ListView) findView(R.id.sources_grid);
        ef_form.setAdapter(logAdapter);

        chart_sources_excel = (LineChart) findView(R.id.chart_sources_excel);
        linear_excel = (LinearLayout) findView(R.id.linear_excel);
        linear_excel.setVisibility(View.GONE);
        initChar();
    }
    public void setData(List<exelBean> exelBeen, String time2,boolean bool2){
        ef_form.setVisibility(bool2?View.VISIBLE:View.GONE);
        chart_sources_excel.setVisibility(bool2?View.GONE:View.VISIBLE);
        if(bool2){
            setSim(exelBeen,time2);
            logAdapter.setList(exelBeen);
        }else setData(exelBeen);
    }

    public void updateView(){
        logAdapter.notifyDataSetChanged();
    }

    private void setSim(List<exelBean> exelBeanList, String sim){
        XlsxUtil.writeExcel(sim,"记录",exelBeanList);
    }

    @Override
    public void onClick(View v, int id) {

    }

    private YAxis leftAxis,rightAxis;
    private void initChar(){
        chart_sources_excel.setDescription("");
        chart_sources_excel.setNoDataTextDescription("You need to provide data for the chart.");
        chart_sources_excel.setTouchEnabled(true);
        chart_sources_excel.setDragDecelerationFrictionCoef(0.9f);
        chart_sources_excel.setDragEnabled(true);
        chart_sources_excel.setScaleEnabled(true);
        chart_sources_excel.setDrawGridBackground(false);
        chart_sources_excel.setHighlightPerDragEnabled(true);
        chart_sources_excel.setPinchZoom(true);
        chart_sources_excel.setBackgroundColor(Color.LTGRAY);
        //setData(20, 30);
        chart_sources_excel.animateX(2500);
        Typeface tf = Typeface.createFromAsset(App.getResources().getAssets(), "OpenSans-Regular.ttf");
        Legend l = chart_sources_excel.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextSize(13f);
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        XAxis xAxis = chart_sources_excel.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        leftAxis = chart_sources_excel.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(3000f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        rightAxis = chart_sources_excel.getAxisRight();
        rightAxis.setTypeface(tf);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaxValue(5000);
        rightAxis.setAxisMinValue(0);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
    }

    private void setData(List<exelBean> exelBeenss) {  //int count, float range
        List<Integer> list = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < exelBeenss.size(); i++) {
            xVals.add(exelBeenss.get(i).getTime());
            list.add(exelBeenss.get(i).getGasValueFour());
        }
        leftAxis.setAxisMaxValue(Collections.max(list));
        rightAxis.setAxisMaxValue(Collections.max(list));

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        ArrayList<Entry> yVals4 = new ArrayList<Entry>();
        for(int i=0;i<exelBeenss.size();i++){
            exelBean exelBeen1 = exelBeenss.get(i);
            yVals1.add(new Entry(exelBeen1.getGasValueOne(), i));
            yVals2.add(new Entry(exelBeen1.getGasValueTwo(), i));
            yVals3.add(new Entry(exelBeen1.getGasValueThree(), i));
            yVals4.add(new Entry(exelBeen1.getGasValueFour(), i));
        }

        LineDataSet set1, set2,set3,set4;

        if (chart_sources_excel.getData() != null &&
                chart_sources_excel.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)chart_sources_excel.getData().getDataSetByIndex(0);
            set2 = (LineDataSet)chart_sources_excel.getData().getDataSetByIndex(1);
            set3 = (LineDataSet)chart_sources_excel.getData().getDataSetByIndex(2);
            set4 = (LineDataSet)chart_sources_excel.getData().getDataSetByIndex(3);
            set1.setYVals(yVals1);
            set2.setYVals(yVals2);
            set3.setYVals(yVals3);
            set4.setYVals(yVals4);
            chart_sources_excel.getData().setXVals(xVals);
            chart_sources_excel.getData().notifyDataChanged();
            chart_sources_excel.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "CO");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);

            // create a dataset and give it a type
            set2 = new LineDataSet(yVals2, "H2S");
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.WHITE);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            //set2.setFillFormatter(new MyFillFormatter(900f));

            set3 = new LineDataSet(yVals3, "NH3");
            set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set3.setColor(Color.YELLOW);
            set3.setCircleColor(Color.WHITE);
            set3.setLineWidth(2f);
            set3.setCircleRadius(3f);
            set3.setFillAlpha(65);
            set3.setFillColor(Color.RED);
            set3.setDrawCircleHole(false);
            set3.setHighLightColor(Color.rgb(244, 117, 117));

            set4 = new LineDataSet(yVals4, "CO2");
            set4.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set4.setColor(Color.GREEN);
            set4.setCircleColor(Color.WHITE);
            set4.setLineWidth(2f);
            set4.setCircleRadius(3f);
            set4.setFillAlpha(65);
            set4.setFillColor(Color.RED);
            set4.setDrawCircleHole(false);
            set4.setHighLightColor(Color.rgb(244, 117, 117));

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);
            dataSets.add(set3);
            dataSets.add(set4);

            // create a data object with the datasets
            LineData data = new LineData(xVals, dataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            chart_sources_excel.setData(data);
        }
    }
}
