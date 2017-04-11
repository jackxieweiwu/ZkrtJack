package zkrtdrone.zkrt.com.maplib;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.ToastUtil;
import zkrtdrone.zkrt.com.maplib.requestpermissions.HintDialogFragment;
import zkrtdrone.zkrt.com.maplib.until.MapHelp;
import zkrtdrone.zkrt.com.maplib.until.MapUtil;
import zkrtdrone.zkrt.com.maplib.until.location.LocationUtil;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;

import static android.R.id.progress;
import static zkrtdrone.zkrt.com.maplib.until.MapUtil.checkGpsCoordinates;

/**
 * Created by jack_xie on 16-12-19.
 */

public abstract class BaseFragmentMap extends
        BaseMvpFragment implements  LocationSource,AMapLocationListener,HintDialogFragment.DialogFragmentCallback {
    //private MapView mapview;
    public ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    public AMap aMap;
    private TextureMapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    public static final int STROKE_WIDTH = 3;
    public static final int TOLERANCE = 21;
    public float zoomlevel = (float) 18.0;
    private boolean mFirstFix = false;
    private Circle mCircle;
    private Circle circle;
    public AMapLocation amapLocationa;
    private GestureOverlayView overlay;
    private SensorEventHelper mSensorHelper;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    public double droneLocationLat = 181, droneLocationLng= 181;
    public double peploeLocationLat = 181,peploeLocationLng= 181;
    private Marker mLocMarker;
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int STORAGE_COARSE_PERMISSION_CODE = 102;

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        //mapview = (MapView) view.findViewById(R.id.map_view);
        mapView = (TextureMapView) view.findViewById(R.id.map);
        overlay = (GestureOverlayView) view.findViewById(R.id.overlay);
        checkStoragePermission();//初始化请求权限，存储权限
        getsturview(overlay);
        initView();
    }
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            requestPermission(STORAGE_PERMISSION_CODE);
        } else {
            //同组的权限，只要有一个已经授权，则系统会自动授权同一组的所有权限，比如WRITE_EXTERNAL_STORAGE和READ_EXTERNAL_STORAGE
            ToastUtil.show(mActivity,"已获取存储的读写权限");
        }

        if (ContextCompat.checkSelfPermission(mActivity,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            requestPermission(LOCATION_PERMISSION_CODE);
        } else {
            //同组的权限，只要有一个已经授权，则系统会自动授权同一组的所有权限，比如WRITE_EXTERNAL_STORAGE和READ_EXTERNAL_STORAGE
            ToastUtil.show(mActivity,"已获取网络定位权限权限");
        }

        if (ContextCompat.checkSelfPermission(mActivity,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            requestPermission(STORAGE_COARSE_PERMISSION_CODE);
        } else {
            //同组的权限，只要有一个已经授权，则系统会自动授权同一组的所有权限，比如WRITE_EXTERNAL_STORAGE和READ_EXTERNAL_STORAGE
            ToastUtil.show(mActivity,"已获取网络定位权限权限");
        }
    }

    private void requestPermission(int permissioncode) {
        String permission = getPermissionString(permissioncode);
        if (!IsEmptyOrNullString(permission)){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    permission)) {
                if(permissioncode == LOCATION_PERMISSION_CODE) {
                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.location_description_title,
                            R.string.location_description_why_we_need_the_permission,
                            permissioncode);
                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                } else if (permissioncode == STORAGE_PERMISSION_CODE) {
                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.storage_description_title,
                            R.string.storage_description_why_we_need_the_permission,
                            permissioncode);
                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                }
            } else {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{permission}, permissioncode);
            }
        }
    }

    private String getPermissionString(int requestCode){
        String permission = "";
        switch (requestCode){
            case LOCATION_PERMISSION_CODE:
                permission = Manifest.permission.ACCESS_FINE_LOCATION;
                break;
            case STORAGE_PERMISSION_CODE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
            case STORAGE_COARSE_PERMISSION_CODE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
        }
        return permission;
    }


    @Override
    public void doPositiveClick(int requestCode) {
        String permission = getPermissionString(requestCode);
        if (!IsEmptyOrNullString(permission)){
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{permission},
                        requestCode);
            }else{
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                startActivity(intent);
            }
        }
    }

    @Override
    public void doNegativeClick(int requestCode) {

    }

    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    public void enableGestureDetection() {
        overlay.setEnabled(true);
    }

    public void disableGestureDetection() {
        overlay.setEnabled(false);
    }

    protected void getsturview(GestureOverlayView overlay){}

    public void setDroneGPS(double droneLocationLats, double droneLocationLngs){
        this.droneLocationLat = droneLocationLats;
        this.droneLocationLng = droneLocationLngs;
    }

    public int scaleDpToPixels(double value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) Math.round(value * scale);
    }

    protected void initView(){}

    @Override
    protected int getFragmentViewId() {
        return R.layout.my_map_linearlayout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
            setUpMap();
            mapSetting();
            amapOnclick();
        }
        mSensorHelper = new SensorEventHelper(mActivity);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    protected void amapOnclick(){}

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    /**
     * 设置一些amap的属性
     */
    public void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
    }

    //放大  缩小
    public void goToZoom(int number){
        if(0 == number){
            aMap.moveCamera(CameraUpdateFactory.zoomIn());
        }else if(1 == number){
            aMap.moveCamera(CameraUpdateFactory.zoomOut());
        }
    }

    //map setting
    private void mapSetting(){
        UiSettings mUiSettings = aMap.getUiSettings();//实例化UiSettings类
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setScaleControlsEnabled(true);//显示比例尺控件
        mUiSettings.setMyLocationButtonEnabled(false); // 显示默认的定位按钮
        aMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomlevel));
        mUiSettings.setCompassEnabled(false);//指南针
    }

    //定义地图画园
    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            amapLocationa = amapLocation;
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                if(amapLocation.getTime() == 0) amapLocation.setTime(System.currentTimeMillis());
                LocationUtil.wgsToGcj(amapLocation, false);
                peploeLocationLat = amapLocation.getLatitude();
                peploeLocationLng = amapLocation.getLongitude();
                BaseApplication.peploLat = peploeLocationLat;
                BaseApplication.peploLng = peploeLocationLng;
                LatLng location = new LatLng(peploeLocationLat, peploeLocationLng);
                if(mLocMarker != null) mLocMarker.remove();
                mLocMarker = MapUtil.addMarker(location,aMap);//添加定位图标
                if (!mFirstFix) {
                    mFirstFix = true;
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                }
            } else {
                //ToastUtil.show(mActivity,"定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo());
            }
        }
    }

    public AMapLocation getAmapLocation(){
        return amapLocationa;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mActivity);
            mLocationOption = new AMapLocationClientOption();
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//Device_Sensors   //Hight_Accuracy
            // 使用连续定位
            mLocationOption.setOnceLocation(false);
            mLocationOption.setInterval(10*1000);
            mLocationOption.setNeedAddress(true);
            mLocationOption.setMockEnable(false);
            mLocationOption.setKillProcess(true);
            mlocationClient.setLocationOption(mLocationOption);
            //设置定位监听
            mlocationClient.setLocationListener(this);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    //更换地图的效果（普通，卫星地图）
    public void goToMapType(int number){
        aMap.setMapType(number);
    }

    //得到当前地图的样式
    public int getMapType(){
        if(aMap != null){
            return aMap.getMapType();
        }else {
            return 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView !=null)mapView.onDestroy();
        if(mlocationClient!=null)mlocationClient.onDestroy();//销毁定位客户端。
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mlocationClient !=null)mlocationClient.stopLocation();//停止定位
    }

    public void setAmapCirCle(final LatLng pos){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (circle != null) {
                    circle.remove();
                }
                if (checkGpsCoordinates(droneLocationLat, droneLocationLng)) {
                    circle = aMap.addCircle(new CircleOptions().
                            center(pos).
                            radius(0.5).
                            fillColor(Color.argb(progress, 1, 1, 1)).
                            strokeColor(Color.argb(progress, 1, 1, 1)));
                }
            }
        });
    }

    public List<LatLng> projectPathIntoMap(List<LatLng> path) {
        List<LatLng> coords = new ArrayList<LatLng>();
        Projection projection = aMap.getProjection();

        for (LatLng point : path) {
            LatLng coord = projection.fromScreenLocation(new Point((int) point.latitude,(int) point.longitude));
            coords.add(MapHelp.mapLatLngToCoord(coord));
        }
        return coords;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapView !=null)mapView.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        deactivate();
        mFirstFix = false;
    }

    public List<LatLng> decodeGesture() {
        List<LatLng> path = new ArrayList<LatLng>();
        extractPathFromGesture(path);
        return path;
    }

    private void extractPathFromGesture(List<LatLng> path) {
        float[] points = overlay.getGesture().getStrokes().get(0).points;
        for (int i = 0; i < points.length; i += 2) {
            path.add(new LatLng((int) points[i], (int) points[i + 1]));
        }
    }
}
