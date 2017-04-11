package zkrtdrone.zkrt.com.maplib.configer;


import zkrtdrone.zkrt.com.maplib.map_type.MapType;

/**
 * Created by jack_xie on 17-2-9.
 */

public class Configer {
    //************************* 发布需要配置 ****************************

    /**
     * 其他需要配置：
     * 1、versionCode、versionName（必须）
     * 2、UMENG_CHANNEL（必须）Channels
     */

    /**
     * 是否是调试模式,发布需要修改为false
     */
    public static final boolean isDebugMode = true; //false

    /**
     * 选择的地图类型
     */
    public static final MapType mapType = MapType.AMap;

}
