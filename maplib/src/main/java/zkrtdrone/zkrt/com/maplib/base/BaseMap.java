package zkrtdrone.zkrt.com.maplib.base;

import android.graphics.Color;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.gesture.GestureOverlayView;
import android.os.CountDownTimer;
import android.view.MotionEvent;

import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.ToastUtil;
import zkrtdrone.zkrt.com.maplib.App;
import zkrtdrone.zkrt.com.maplib.BaseFragmentMap;
import zkrtdrone.zkrt.com.maplib.bean.LonglatLog;
import zkrtdrone.zkrt.com.maplib.bean.MissionLatLon;
import zkrtdrone.zkrt.com.maplib.bean.PathConfig;
import zkrtdrone.zkrt.com.maplib.until.MapHelp;
import zkrtdrone.zkrt.com.maplib.until.MapUtil;

import static zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication.rightMenu;

/**
 * Created by jack_xie on 17-2-12.
 */

public class BaseMap<P extends BasePresenter, M extends BaseModel> extends BaseFragmentMap
        implements GestureOverlayView.OnGestureListener,
        AMap.OnMapClickListener, AMap.OnMarkerDragListener,
        AMap.CancelableCallback {
    public boolean isAdd = false;
    public float numberCalcu;
    public List<MissionLatLon> wayMissionList= new ArrayList<>();
    public List<Polyline> wayMissionListPoly= new ArrayList<>();
    public List<LatLng> latLnglist = new ArrayList<LatLng>();
    private Map<String,Polyline> polylineMap = new HashMap<String,Polyline>();
    public HashBiMap<Integer, Marker> mBiMarkersMap = new HashBiMap<Integer, Marker>();
    //拖动计算
    private int markerId = -1;
    private MissionLatLon missionLatLons;
    private double toleranceInPixels;

    @Override
    protected void amapOnclick() {
        super.amapOnclick();
        if(aMap != null){
            aMap.setOnMapClickListener(this);
            aMap.setOnMarkerDragListener(this);
        }
    }

    @Override
    protected void getsturview(GestureOverlayView overlay) {
        super.getsturview(overlay);
        if(overlay != null){
            overlay.addOnGestureListener(this);
            overlay.setEnabled(false);
            overlay.setGestureStrokeWidth(scaleDpToPixels(STROKE_WIDTH));
            toleranceInPixels = scaleDpToPixels(TOLERANCE);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (isAdd == true && rightMenu == true){
            markWaypoint(latLng,false);
        }else{
            mapOnclick();
        }
    }

    public void mapOnclick() {}

    public void wayMapPlayout(int color){
        if(wayMissionList.size()>=2){
            wayMissionListPoly.add(aMap.addPolyline(new PolylineOptions().
                    color(color).width(1).zIndex(1).addAll(latLnglist)));
        }
    }

    public void markWaypoint(LatLng point,boolean bool){
        int numaa = wayMissionList.size()+1;
        Marker mark = MapUtil.customMarker(aMap,mActivity,point,numaa+"");
        mark.setDraggable(true);
        if(mark !=null){
            //航点划线
            addMinssionPolay(point,mark,numaa,bool);
        }
    }

    public void addMinssionPolay(LatLng point, Marker marker,int numaa,boolean boolag){}

    public void startDronePolay() {
        //运行轨迹
        //setOptionsPolyline(droneLocationLat,droneLocationLng);
        //setPolyLineNpole();
        // 划线
        setPolyLine();
    }

    public void setIsAddWay(boolean isBool){
        this.isAdd = isBool;
    }

    //地球北极地理坐标
    public void setPolyLineNpole(){
        List<LatLng> pathPoints = new ArrayList<LatLng>(2);
        pathPoints.add(new LatLng(181, 181));
        pathPoints.add(new LatLng(droneLocationLat, droneLocationLng));

        List<PathConfig> listN = new ArrayList<>();
        listN.add(new PathConfig(pathPoints,2, Color.RED));
        updatePaths(listN,"N_drone");
    }

    //定义无人机运行轨迹
    /*public void setOptionsPolyline(double lat,double lng){
        if(lat == NaN) return;
        ll=new LatLng(lat,lng);
        List<LatLng> pathPoints = new ArrayList<LatLng>(2);
        pathPoints.add(oldll);
        pathPoints.add(ll);

        List<PathConfig> listN = new ArrayList<>();
        listN.add(new PathConfig(pathPoints,2, Color.BLACK));
        updatePaths(listN,"drone_poly");
        oldll = ll;
    }*/

    //绘制线条
    public void setPolyLine(){
        List<LatLng> pathPoints = new ArrayList<LatLng>(2);
        pathPoints.add(new LatLng(peploeLocationLat, peploeLocationLng));
        pathPoints.add(new LatLng(droneLocationLat, droneLocationLng));

        List<PathConfig> listN = new ArrayList<>();
        listN.add(new PathConfig(pathPoints,2, Color.BLUE));
        updatePaths(listN,"peplo_drone");
    }

    /**
     * @description: 添加一段轨迹
     * @return
     */
    private Polyline addAPath(PathConfig path,String name){
        Polyline pl = setAddPath(path);
        polylineMap.put(name,pl);
        return pl;
    }

    private Polyline setAddPath(PathConfig path){
        Polyline l = aMap.addPolyline(new PolylineOptions()
                .width(path.lineWidth)
                .color(path.lineColor)
                .visible(true)
                .geodesic(true)
                .addAll(path.points));
        return l;
    }

    /**
     * @description: 更新轨迹
     * drone pl
     * @param points
     */
    private void updatePaths(List<PathConfig> points, final String name){
        PathConfig pc = points.get(0);
        if(polylineMap.size()<=0){
            addAPath(pc,name);
        }

        for (String o : polylineMap.keySet()) {
            Polyline pl =  polylineMap.get(o);
            if(o.equals("N_drone") || o.equals("drone_poly") ||
                    o.equals("peplo_drone")){
                pl.setPoints(pc.points);
                pl.setColor(pc.lineColor);
                pl.setWidth(pc.lineWidth);
                pl.setVisible(true);
            }else{
                addAPath(pc,name);
            }
        }
    }

    //定位当前的位置中心，  是无人机   还是使用者
    public void goToPepleOrDrone(int number){
        if(number == 0){
            setUpMap();
            MapUtil.moveTo(aMap,new LatLng(peploeLocationLat,peploeLocationLng),true);
        }else if(number == 1){
            MapUtil.moveTo(aMap,new LatLng(droneLocationLat,droneLocationLng),true);
        }
    }

    public void updatePolyLine(){
        for (Polyline polyline : wayMissionListPoly){
            polyline.setPoints(latLnglist);
        }
    }

    public void cleanPolyLineMarkLatlan(){
        for (Polyline polyline : wayMissionListPoly){
            polyline.setVisible(false);
            polyline.remove();
        }
        wayMissionListPoly.clear();
        wayMissionList.clear();
        latLnglist.clear();
        clearMarkers();
        numberCalcu=0f;
    }

    public void clearMarkers() {
        for (Marker marker : mBiMarkersMap.valueSet()) {
            marker.remove();
        }
        mBiMarkersMap.clear();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        markerId = mBiMarkersMap.getKey(marker)-1;
        missionLatLons = wayMissionList.get(markerId);
        latLnglist.set(markerId,marker.getPosition());
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        latLnglist.set(markerId,marker.getPosition());
        updatePolyLine();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latLnglist.set(markerId,marker.getPosition());
        updatePolyLine();
        missionLatLons.getLatLong().setLatLng(marker.getPosition());
    }

    //计算距离
    public void CalculationDistance(){
        numberCalcu = 0;
        for(int i=0;i<wayMissionList.size();i++){
            if(i==wayMissionList.size())return;
            if(i+1==wayMissionList.size()) {
                setWayDistance();
                return;
            }
            numberCalcu+= AMapUtils.calculateLineDistance(wayMissionList.get(i).getLatLong().getLatLng(),
                    wayMissionList.get(i+1).getLatLong().getLatLng());
        }
    }

    public void setWayDistance(){}

    @Override
    public void onFinish() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
        //overlay.setEnabled(false);
        List<LatLng> path = decodeGesture();
        if (path.size() > 1) {
            path = MapHelp.simplify(path, toleranceInPixels);
        }
        latLnglist =  projectPathIntoMap(path);
        for(int i = 0;i<latLnglist.size();i++){
            markWaypoint(latLnglist.get(i),true);
        }
    }

    public void gestureWatMarker() {}

    @Override
    public void onGesture(GestureOverlayView overlay, MotionEvent event) {

    }

    @Override
    public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {

    }

    @Override
    public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {

    }
}
