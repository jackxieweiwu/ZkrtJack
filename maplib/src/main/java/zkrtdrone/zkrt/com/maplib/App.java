package zkrtdrone.zkrt.com.maplib;

import android.content.res.Resources;

import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication;

/**
 * Created by jack_xie on 17-2-9.
 */

public class App {
    public volatile static BaseApplication app;

    public static final Resources getResources(){
        return app.getResources();
    }
}
