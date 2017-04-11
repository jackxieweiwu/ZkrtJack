package com.zkrt.zkrtdrone.view.layout.text;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting.CommonSettingFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.framgnet_aasd, new CommonSettingFragment(), "com").commit();
    }
}
