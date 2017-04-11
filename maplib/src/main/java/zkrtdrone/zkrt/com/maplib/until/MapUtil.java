package zkrtdrone.zkrt.com.maplib.until;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.R;
import zkrtdrone.zkrt.com.maplib.until.location.LocationUtil;

import static zkrtdrone.zkrt.com.maplib.App.getResources;

/**
 * Created by jack_xie on 17-2-9.
 */

public class MapUtil {
    /**
     * @description: 地图移动到另一点
     * @author: chenshiqiang E-mail:csqwyyx@163.com
     * @param map
     * @param loc
     */
    public static void moveTo(AMap map, LatLng loc, boolean animate){
        float zoom = map.getCameraPosition().zoom;
        moveTo(map, loc, zoom, animate);
    }


    /**
     *
     * @description: 地图移动到另一点
     * @author: xie_jack E-mail:csqwyyx@163.com
     * @param map
     * @param loc
     * @param zoom 2.0-21.0
     * @param animate
     */
    public static void moveTo(AMap map, LatLng loc, float zoom, boolean animate){
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(loc, zoom);
        if(animate){
            map.animateCamera(cu,1000,null);
        }else{
            map.moveCamera(cu);
        }
    }

    /**
     * 在地图上画marker
     * @param point      marker坐标点位置（example:LatLng point = new LatLng(39.963175,
     *                   116.400244); ）
     * @param markerIcon 图标
     * @return Marker对象
     */
    public static Marker drawMarkerOnMap(AMap aMap, LatLng point, Bitmap markerIcon) {
        if (aMap != null && point != null) {
            Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
                    .position(point)
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon)));
            return marker;
        }
        return null;
    }

    //自定义marker
    public static Marker customMarker(AMap aMap, Activity activity, LatLng pos, String number){
        View view = getMarkerWay(activity);
        TextView mark_txt = (TextView) view.findViewById(R.id.txt_marker);
        mark_txt.setText(number+"");
        return MapUtil.drawMarkerOnMap(aMap,pos, MapUtil.convertViewToBitmap(view));
    }

    public static View getMarkerWay(Activity activity){
        View view = View.inflate(activity,R.layout.map_marker, null);
        return view;
    }


    /**
     * @description: 移动到当前位置
     * @author: xie_jack E-mail:csqwyyx@163.com
     * @param map
     * @return
     */
    /*public static boolean moveToCurLocation(AMap map){
        Location loc = MyLocationManager.getInstance().getLatestKnowLocation();
        if(loc != null){
            moveTo(map, LocationUtil.getLatLon(loc), true);
            return true;
        }
        return false;
    }*/

    public static void centerPoints(AMap map, List<LatLng> points){
        if(points == null || points.size() < 2){
            return;
        }

        LatLng first = points.get(0);
        double minLat = first.latitude;
        double maxLat = first.latitude;
        double minLon = first.longitude;
        double maxLon = first.longitude;
        for(int i = 1, num = points.size() ; i < num ; i++){
            LatLng t = points.get(i);
            if(t.latitude > maxLat){
                maxLat = t.latitude;
            }
            if(t.latitude < minLat){
                minLat = t.latitude;
            }
            if(t.longitude > maxLon){
                maxLon = t.longitude;
            }
            if(t.longitude < minLon){
                minLon = t.longitude;
            }
        }

        LatLngBounds bounds = new LatLngBounds(new LatLng(minLat, minLon), new LatLng(maxLat, maxLon));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }

    //view 转bitmap
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static Marker addMarker(LatLng latlng, AMap aMap) {
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.navi_map_gps_locked1sss);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        return aMap.addMarker(options);
    }

    public static boolean checkGpsCoordinates(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    /*public static void centerTrackPoints(AMap map, List<TrackPoint> points){
        if(points == null || points.size() < 2){
            return;
        }

        TrackPoint first = points.get(0);
        double minLat = first.getLatitude();
        double maxLat = first.getLatitude();
        double minLon = first.getLongitude();
        double maxLon = first.getLongitude();
        for(int i = 1, num = points.size() ; i < num ; i++){
            TrackPoint t = points.get(i);
            if(t.getPointStatus() == TrackPointStatus.normal.getValue()){
                if(t.getLatitude() > maxLat){
                    maxLat = t.getLatitude();
                }
                if(t.getLatitude() < minLat){
                    minLat = t.getLatitude();
                }
                if(t.getLongitude() > maxLon){
                    maxLon = t.getLongitude();
                }
                if(t.getLongitude() < minLon){
                    minLon = t.getLongitude();
                }
            }
        }

        LatLngBounds bounds = new LatLngBounds(new LatLng(minLat, minLon), new LatLng(maxLat, maxLon));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }*/
}
