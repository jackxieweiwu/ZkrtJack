package com.zkrt.zkrtdrone.view.myfragment.camera;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.fynn.switcher.Switch;
import com.triggertrap.seekarc.SeekArc;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.view.dialog.CustomDialog;
import com.zkrt.zkrtdrone.view.myfragment.mount.MountFragment;
import com.zkrt.zkrtdrone.view.widget.joystick.JoystickMovedListener;
import com.zkrt.zkrtdrone.view.widget.joystick.joystick;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;

/**
 * Created by jack_xie on 17-2-13.
 */

public class ModelCameraAndOne extends BaseMvpFragment{
    private int no1 = 1500;
    private int no2 = 1500;
    private int no7_1 = 0;
    private int no7_2 = 0;
    private int no72 = 0;
    private int no71 = 0;
    private int take_picture = 0;
    private int recording = 0;
    private int number3d = 1;
    private int numberPao = 1;
    private CustomDialog dialog;
    private int numberzs = 0;
    private int redQh = 0;
    private int colorQh = 0;
    private int moudleQh = 0;
    private int zoomType = -1;
    private boolean zoomStatus = false;


    @BindView(R.id.model_camera_rela) RelativeLayout model_camera_rela;
    @BindView(R.id.camera_mount_joy) RelativeLayout camera_mount_joy;
    @BindView(R.id.model_camera_switch) Switch model_camera_switch;
    @BindView(R.id.mount_camera_linear)RelativeLayout mount_camera_linear;
    //@BindView(R.id.directionJoystickRight)joystick directionJoystickRight;
    @BindView(R.id.seekArcComplete)SeekArc seekArcComplete;

    @Override
    protected int getFragmentViewId() {
        return R.layout.model_camera;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        seekArcComplete.setProgressColor(android.R.color.transparent);
        //directionJoystickRight.setOnJostickMovedListener(lJoystick);
        MountFragment mountFragment = (MountFragment) DJISampleApplication.fragmentManager.findFragmentByTag("MountFragment");
        mountFragment.setLinear(mount_camera_linear,model_camera_rela,camera_mount_joy);
        dialog = mountFragment.getDialog();

        /*seekArcComplete.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                if(i>50){
                    setZoomMin(false);
                }else if(i <50){
                    setZoomBig(false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
                seekArcComplete.setProgress(50);
                setStopZoom(false);
            }
        });*/
    }

    public RelativeLayout getCameraLinear(){
        return mount_camera_linear;
    }

    public RelativeLayout getCamerarela(){
        return model_camera_rela;
    }

    public void changeDialog(CustomDialog dialogs){
        this.dialog = dialogs;
    }

    JoystickMovedListener lJoystick = new JoystickMovedListener() {
        @Override
        public void OnReturnedToCenter() {
           /* no1 = 1500;
            no2 = 1500;
            dialogGotoDate(true);*/
        }

        @Override
        public void OnReleased() {

        }

        @Override
        public void OnMoved(double pan, double tilt) {
            double one = pan-1500;
            double two = tilt-1500;
                no1 = (int)(1500-one);
                if(no1 == 0){
                no1 = 1500;
            }
                no2 = (int)(1500-two);
            if(no2 == 0){
                no2 = 1500;
            }
            dialogGotoDate(true,"");
        }
    };

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        model_camera_switch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                dialog.serGotoOnboardData(0,number3d,"camera_3d",true,"");
                if(number3d >= 1){
                    number3d--;
                }else{
                    number3d++;
                }
            }
        });
    }

    /*@OnClick(R.id.btn_min)
    public void btnMin(View v){
        setZoomMin(false);
    }*/

    /*@OnClick(R.id.btn_max)
    public void btnMax(View v){
        setZoomBig(false);
    }*/

    @OnClick(R.id.btn_rec)
    public void btnRec(View v){
        setRecVideo(true);
    }

    @OnClick(R.id.btn_take)
    public void btnTake(View v){
        setTakePhoto(true);
    }

    //红外切换
    @OnClick(R.id.btn_qh)
    public void btnQh(View v){
        redQh++;
        dialogGotoDate(true,"");
    }

    //颜色切换
    @OnClick(R.id.btn_colorqh)
    public void setColorQh(View v){
        colorQh++;
        dialogGotoDate(true,"");
    }

    //模式切换
    @OnClick(R.id.btn_modleqh)
    public void setMoudleQh(View v){
        moudleQh++;
        dialogGotoDate(true,"");
    }

    //变焦大
    private void setZoomBig(boolean b){
        no7_2++;
        zoomType = 0;
        dialogGotoDate(b,"");
        no72 = no7_2;
    }

    //变焦小
    private void setZoomMin(boolean b){
        no7_1++;
        zoomType = 1;
        dialogGotoDate(b,"");
        no71 = no7_1;
    }

    //停止放大缩小
    public void setStopZoom(boolean b){
        if(zoomType == 0){
            dialogGotoDate(b,"stopMax");
        }else if(zoomType == 1){
            dialogGotoDate(b,"stopMin");
        }
    }

    //拍照
    public void setTakePhoto(boolean b){
        take_picture++;
        dialogGotoDate(b,"");
    }

    //录像
    public void setRecVideo(boolean b){
        recording++;
        dialogGotoDate(b,"");
    }

    public void dialogGotoDate(boolean bool,String zoomStop){
        dialog.serDataValues(0,0,"camera_mount",bool,zoomStop,
                take_picture,recording,no7_2,no7_1,no1,no2,redQh,colorQh,moudleQh,no72,no71);
    }
    public void setZhaoShe(int number ,boolean bool){
        if(number == 1 && numberzs == 0){
            numberzs =1;
            dialog.setExposure(true,bool);
        }else if(number == 0 && numberzs == 1){
            dialog.setExposure(false,bool);
            numberzs = 0;
        }
    }

    //抛投
    public void setPaoTou(int number){
        if(number== 1){
            Utils.setResultToToast(mActivity,"aaaa---"+numberPao);
            if(numberPao == 4){
                dialog.setRoling(numberPao-1, false,false);
                numberPao = 1;
                return;
            }
            dialog.setRoling(numberPao, true,false);
            numberPao++;
        }
    }

    //变焦
    public void setComAssisZoom(int zoom, boolean b){
        if(zoom>120){
            setZoomBig(b);
            zoomStatus = true;
        }else if(zoom<=120 && zoom>=85){
            if(zoomStatus) {
                setStopZoom(b);
            }
            zoomStatus = false;
        }else if(zoom<85){
            setZoomMin(b);
            zoomStatus = true;
        }
    }

    //遥感
    public void setRemote(int upOrDow, int leftOrRight){
        //left right
        no2 = (int) (((200-upOrDow)+1350)+((200-upOrDow)*0.5d))-3;
        no1 = (int) ((leftOrRight+1350)+(leftOrRight*0.5d))-3;
        dialogGotoDate(false,"");
    }

    //确认避障
    public void setObstacleNoOff(boolean bool,String cm,String cm_speed){
        if(cm_speed.equals("") || cm_speed == "") cm_speed = "0";
       float f =  Float.parseFloat(cm_speed);
        java.text.DecimalFormat df =new java.text.DecimalFormat("#.0");
        df.format(f);
        if(dialog !=null)dialog.setObtacle(bool,cm,(int)f*10);
    }

    //脚架  校准 是否自动 脚架控制
    public void setTripodNoOff(int number,boolean bool,String name){
        dialog.setTripoda(number,bool,name);
    }
}
