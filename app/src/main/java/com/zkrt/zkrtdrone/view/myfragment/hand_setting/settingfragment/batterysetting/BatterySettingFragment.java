package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.batterysetting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.AppUtil;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import java.math.BigDecimal;
import butterknife.BindView;
import dji.common.battery.DJIBatteryAggregationState;
import dji.common.battery.DJIBatteryLowCellVoltageOperation;
import dji.common.battery.DJIBatteryState;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.battery.DJIBattery;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;

/**
 * Created by jack_xie on 16-12-29.
 */

public class BatterySettingFragment extends BaseMvpFragment{
    @BindView(R.id.txt_setting_battery_v) TextView txt_setting_battery_v;
    @BindView(R.id.low_battery_spinner) Spinner low_battery_spinner;
    @BindView(R.id.kg_low_battery_spinner) Spinner kg_low_battery_spinner;
    @BindView(R.id.spinner_battery_xin) Spinner spinner_battery_xin;
    @BindView(R.id.txt_battert_low_one) TextView txt_battert_low_one;
    @BindView(R.id.battert_seekbar_one) SeekBar battert_seekbar_one;
    @BindView(R.id.txt_battert_low_two) TextView txt_battert_low_two;
    @BindView(R.id.battert_seekbar_two) SeekBar battert_seekbar_two;
    private double seekbarOne = 36d;
    private double seekbartwo = 35d;
    private int numberBattery = 0;

    @Override
    protected int getFragmentViewId() {
        return R.layout.setting_battery;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.battery_way,R.layout.spinner_list_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        low_battery_spinner.setAdapter(adapter);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.battery_way2,R.layout.spinner_list_item);
        adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        kg_low_battery_spinner.setAdapter(adapter2);

        ArrayAdapter adapters = ArrayAdapter.createFromResource(getContext(),R.array.battery_s,R.layout.spinner_list_item);
        adapters.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner_battery_xin.setAdapter(adapters);

        battert_seekbar_two.isPressed();
        battert_seekbar_one.isPressed();
        //spinner_battery_xin.isPressed();
        spinner_battery_xin.setSelection(1,false);
        spinner_battery_xin.invalidate();
    }

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        if(DJIModuleVerificationUtil.isBattery()) {

            //获取电量
            DJISampleApplication.getProductInstance().getBattery().setBatteryStateUpdateCallback(new DJIBattery.DJIBatteryStateUpdateCallback() {
                @Override
                public void onResult(DJIBatteryState djiBatteryState) {
                    showDJIBatteryState(djiBatteryState);
                    //将代理设置为接收电池聚合信息。
                    DJISampleApplication.getAircraftInstance().getBattery().setBatteryAggregationStateUpdatedCallback(new DJIBattery.DJIBatteryAggregationStateUpdatedCallback() {
                        @Override
                        public void onResult(DJIBatteryAggregationState djiBatteryAggregationState) {
                            showDJIBatteryAggregationState(djiBatteryAggregationState);
                        }
                    });
                }
            });

            //获取以mV为单位的1级单元电压阈值。
            DJISampleApplication.getAircraftInstance().getBattery().getLevel1CellVoltageThreshold(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    showLevel1CellVoltageThreshold(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取以mV为单位的1级单元电压阈值。
            DJISampleApplication.getAircraftInstance().getBattery().getLevel2CellVoltageThreshold(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    showLevel2CellVoltageThreshold(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //
            DJISampleApplication.getAircraftInstance().getBattery().getLevel1CellVoltageOperation(new DJICommonCallbacks.DJICompletionCallbackWith<DJIBatteryLowCellVoltageOperation>() {
                @Override
                public void onSuccess(DJIBatteryLowCellVoltageOperation djiBatteryLowCellVoltageOperation) {
                    showLevel1CellVoltageperation(djiBatteryLowCellVoltageOperation);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            DJISampleApplication.getAircraftInstance().getBattery().getLevel2CellVoltageOperation(new DJICommonCallbacks.DJICompletionCallbackWith<DJIBatteryLowCellVoltageOperation>() {
                @Override
                public void onSuccess(DJIBatteryLowCellVoltageOperation djiBatteryLowCellVoltageOperation) {
                    showLevel2CellVoltageperation(djiBatteryLowCellVoltageOperation);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }



        setSpinner(low_battery_spinner,true);
        setSpinner(kg_low_battery_spinner,false);
        //spinner_battery_xin.setSelection(1,true);
        /*setBatterySeekberTwo();
        setBatterySeekberOne();*/
        spinner_battery_xin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int aa = 9;
                if (position == 0){
                    numberBattery = 6;
                    aa = 3;
                }

                if(position == 1){
                    numberBattery = 12;
                    aa = 9;
                }

                if(DJIModuleVerificationUtil.isBattery()){
                    DJISampleApplication.getAircraftInstance().getBattery().setNumberOfCells(aa, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError != null){
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.setResultToToast(mActivity,djiError.getDescription()+"");
                                    }
                                });
                            }
                        }
                    });


                    //获取以mV为单位的1级单元电压阈值。
                    DJISampleApplication.getAircraftInstance().getBattery().getLevel1CellVoltageThreshold(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            showLevel1CellVoltageThreshold(integer);
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            showDjiError(djiError);
                        }
                    });

                    //获取以mV为单位的1级单元电压阈值。
                    DJISampleApplication.getAircraftInstance().getBattery().getLevel2CellVoltageThreshold(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            showLevel2CellVoltageThreshold(integer);
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            showDjiError(djiError);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });

        low_battery_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(DJIModuleVerificationUtil.isBattery()){
                    if(position == 0){
                        DJISampleApplication.getAircraftInstance().getBattery().setLevel1CellVoltageOperation(DJIBatteryLowCellVoltageOperation.GoHome, new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {

                            }
                        });
                    }
                    if(position == 1){
                        DJISampleApplication.getAircraftInstance().getBattery().setLevel1CellVoltageOperation(DJIBatteryLowCellVoltageOperation.LEDWarning, new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {

                            }
                        });
                    }

                    if(position == 2){
                        DJISampleApplication.getAircraftInstance().getBattery().setLevel1CellVoltageOperation(DJIBatteryLowCellVoltageOperation.Landing, new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kg_low_battery_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(DJIModuleVerificationUtil.isBattery()){
                    if(position == 0){
                        DJISampleApplication.getAircraftInstance().getBattery().setLevel2CellVoltageOperation(DJIBatteryLowCellVoltageOperation.LEDWarning, new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {

                            }
                        });
                    }

                    if(position == 1){
                        DJISampleApplication.getAircraftInstance().getBattery().setLevel2CellVoltageOperation(DJIBatteryLowCellVoltageOperation.Landing, new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        battert_seekbar_one.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarOne = 36/10d+progress/100d;

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_battert_low_one.setText(seekbarOne*numberBattery+"");
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setBatterySeekberOne();
            }
        });

        battert_seekbar_two.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbartwo = 35/10d+progress/100d;
                if(seekbartwo<seekbarOne) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_battert_low_two.setText(seekbartwo * numberBattery + "");
                        }
                    });
                }else{
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_battert_low_two.setText((seekbarOne * numberBattery)-1 + "");
                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekbartwo<seekbarOne)
                setBatterySeekberTwo();
            }
        });
    }

    private void setBatterySeekberTwo(){
        if(DJIModuleVerificationUtil.isBattery()){
            DJISampleApplication.getAircraftInstance().getBattery().setLevel2CellVoltageThreshold((int) (Float.parseFloat(txt_battert_low_two.getText().toString()) * 1000), new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if(djiError == null){
                        showLevel1CellVoltageThreshold(42);
                    }
                }
            });
        }
    }

    private void setBatterySeekberOne(){
        if(DJIModuleVerificationUtil.isBattery()){
            DJISampleApplication.getAircraftInstance().getBattery().setLevel1CellVoltageThreshold((int) (Float.parseFloat(txt_battert_low_one.getText().toString()) * 1000), new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if(djiError == null){
                        showLevel1CellVoltageThreshold(43);
                    }
                }
            });
        }
    }

    public void setSpinner(Spinner sp,boolean bool){
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (DJIModuleVerificationUtil.isAircraft()) {
                    if (position == 0)
                        if(!bool){
                            if (position == 0)
                                DJISampleApplication.getProductInstance().getBattery().setLevel1CellVoltageOperation(DJIBatteryLowCellVoltageOperation.LEDWarning,
                                        new DJICommonCallbacks.DJICompletionCallback() {
                                            @Override
                                            public void onResult(DJIError djiError) {
                                            }
                                        });
                            if (position == 1)
                                DJISampleApplication.getProductInstance().getBattery().setLevel1CellVoltageOperation(DJIBatteryLowCellVoltageOperation.Landing,
                                        new DJICommonCallbacks.DJICompletionCallback() {
                                            @Override
                                            public void onResult(DJIError djiError) {
                                            }
                                        });
                        }

                    if(bool){
                        DJISampleApplication.getProductInstance().getBattery().setLevel1CellVoltageOperation(DJIBatteryLowCellVoltageOperation.GoHome,
                                new DJICommonCallbacks.DJICompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {

                                    }
                                });
                        if (position == 1)
                            DJISampleApplication.getProductInstance().getBattery().setLevel1CellVoltageOperation(DJIBatteryLowCellVoltageOperation.LEDWarning,
                                    new DJICommonCallbacks.DJICompletionCallback() {
                                        @Override
                                        public void onResult(DJIError djiError) {
                                        }
                                    });
                        if (position == 2)
                            DJISampleApplication.getProductInstance().getBattery().setLevel1CellVoltageOperation(DJIBatteryLowCellVoltageOperation.Landing,
                                    new DJICommonCallbacks.DJICompletionCallback() {
                                        @Override
                                        public void onResult(DJIError djiError) {
                                        }
                                    });
                    }

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    //@Override
    public void showDJIBatteryState(DJIBatteryState djiBatteryState) {
        BigDecimal b = new BigDecimal(djiBatteryState.getCurrentVoltage()/1000f);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        if(txt_setting_battery_v == null) return;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_setting_battery_v.setText(f1+"");
            }
        });
    }

    //@Override
    public void showDjiError(DJIError djiError) {
        if(djiError != null)
        Utils.setResultToToast(mActivity,djiError.getDescription());
    }

    //@Override
    public void showDJIBatteryAggregationState(DJIBatteryAggregationState djiBatteryAggregationState) {
        /*numberBattery = DJISampleApplication.getAircraftInstance().getBattery().getNumberOfCells(); //当前连接的电池数量。
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(numberBattery == 6){
                    spinner_battery_xin.setSelection(0);
                }else{
                    spinner_battery_xin.setSelection(1);
                }
            }
        });*/
    }

    //@Override
    public void showLevel1CellVoltageThreshold(Integer integer) {
        double leve1 = (integer/1000d)*numberBattery;
        double umber = ((integer/1000-3.6)/(4-3.6))*(40-36)+36;
        DJISampleApplication.lowone = leve1;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                battert_seekbar_one.setProgress((int) umber);
                if(txt_battert_low_one != null)txt_battert_low_one.setText(AppUtil.doubleToString(leve1));
            }
        });
    }

    //@Override
    public void showLevel2CellVoltageThreshold(Integer integer) {
        double leve2 = (integer/1000d)*numberBattery;
        double umber = ((integer/1000-3.5)/(3.8-3.5))*(38-35)+35;
        DJISampleApplication.lowtwo = leve2;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                battert_seekbar_two.setProgress((int) umber);
                if(txt_battert_low_two != null)txt_battert_low_two.setText(AppUtil.doubleToString(leve2));
            }
        });
    }

    //@Override
    public void showLevel1CellVoltageperation(DJIBatteryLowCellVoltageOperation djiBatteryLowCellVoltageOperation) {
        int number = djiBatteryLowCellVoltageOperation.value();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(number == 0){
                    low_battery_spinner.setSelection(1);
                }
                if(number == 1){
                    low_battery_spinner.setSelection(0);
                }
                if(number == 2){
                    low_battery_spinner.setSelection(2);
                }
            }
        });
    }

    //@Override
    public void showLevel2CellVoltageperation(DJIBatteryLowCellVoltageOperation djiBatteryLowCellVoltageOperation) {
        int number = djiBatteryLowCellVoltageOperation.value();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(number == 0){
                    kg_low_battery_spinner.setSelection(0);
                }
                if(number == 2){
                    kg_low_battery_spinner.setSelection(1);
                }
            }
        });
    }
}
