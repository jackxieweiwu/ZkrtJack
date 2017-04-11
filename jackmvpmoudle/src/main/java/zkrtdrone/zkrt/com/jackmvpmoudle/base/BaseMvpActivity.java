package zkrtdrone.zkrt.com.jackmvpmoudle.base;

import android.Manifest;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import dji.sdk.sdkmanager.DJISDKManager;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.InstanceUtil;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.ViewUtil;

import static zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication.settingBool;


/**
 * Use: to extends this class
 * If you need MVP, implement BaseView
 */
public abstract class BaseMvpActivity<P extends BasePresenter, M extends BaseModel>
        extends AppCompatActivity{
    private Unbinder unbinder;
    protected P mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Window window=this.getWindow();
        window.setFlags(flag, flag);
        //ViewUtil.hideVirtualKey(this);
        super.onCreate(savedInstanceState);
        BaseApplication.fragmentManager = getSupportFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,Manifest.permission.READ_CONTACTS,
                            Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.WRITE_SETTINGS
                    }
                    , 1);
        }
        /**
         * each time the USB from the RC is connected/disconnected,
         * the phone will prompt the user to select the app they want
         * to connect
         */
        Intent aoaIntent = getIntent();
        if (aoaIntent!=null) {
            String action = aoaIntent.getAction();
            if (action== UsbManager.ACTION_USB_ACCESSORY_ATTACHED) {
                Intent attachedIntent=new Intent();

                attachedIntent.setAction(DJISDKManager.USB_ACCESSORY_ATTACHED);
                sendBroadcast(attachedIntent);
            }
        }

        setContentView(this.getLayoutId());
        BaseApplication.activity = this;
        unbinder = ButterKnife.bind(this);
        mPresenter = InstanceUtil.getInstance(this, 0);
        if (this instanceof BaseView) mPresenter.setVM(this, InstanceUtil.getInstance(this, 1));
        init(savedInstanceState);
    }

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mPresenter != null) mPresenter.onDetached();
        unbinder.unbind();

        settingBool = false;
    }
    public abstract int getLayoutId();
}
