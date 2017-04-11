package zkrtdrone.zkrt.com.maplib.until;

import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import zkrtdrone.zkrt.com.maplib.bean.Gps;

/**
 * Created by jack_xie on 16-12-27.
 */

public class Converter {
    private static double a = 6378245.0;
    private static double ee = 0.00669342162296594323;
    public static double pi = 3.1415926535897932384626;

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(x > 0 ? x : -x);
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x
                * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0
                * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y
                * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(x > 0 ? x : -x);
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x
                * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0
                * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x
                / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    public static LatLng getGPStoAmap(LatLng sourceLatLng, Context mContext){
        CoordinateConverter converter  = new CoordinateConverter(mContext);
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    public static Gps gcj_To_Gps84(LatLng latLng) {
        Gps gps = transform(latLng.latitude, latLng.longitude);
        double lontitude = latLng.longitude * 2 - gps.getWgLon();
        double latitude = latLng.latitude * 2 - gps.getWgLat();
        return new Gps(latitude, lontitude);
    }

    public static Gps transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new Gps(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLat, mgLon);
    }


    // WGS-84 to GCJ-02 WGS-84：是国际标准，GPS坐标
    public static LatLonPoint toGCJ02Point(double latitude, double longitude) {
        LatLonPoint dev = calDev(latitude, longitude);
        double retLat = latitude + dev.getLatitude();
        double retLon = longitude + dev.getLongitude();
        return new LatLonPoint(retLat, retLon);
    }

    // GCJ-02 to WGS-84
    public static LatLonPoint toWGS84Point(double latitude, double longitude) {
        LatLonPoint dev = calDev(latitude, longitude);
        double retLat = latitude - dev.getLatitude();
        double retLon = longitude - dev.getLongitude();
        dev = calDev(retLat, retLon);
        retLat = latitude - dev.getLatitude();
        retLon = longitude - dev.getLongitude();
        return new LatLonPoint(retLat, retLon);
    }

    private static LatLonPoint calDev(double wgLat, double wgLon) {
        if (isOutOfChina(wgLat, wgLon)) {
            return new LatLonPoint(0, 0);
        }
        double dLat = calLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = calLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        return new LatLonPoint(dLat, dLon);
    }

    private static boolean isOutOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double calLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double calLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    public static void transform2(double wgLat, double wgLon, double[] latlng) {
        if (outOfChina(wgLat, wgLon)) {
            latlng[0] = wgLat;
            latlng[1] = wgLon;
            return;
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        latlng[0] = wgLat + dLat;
        latlng[1] = wgLon + dLon;
    }
}
