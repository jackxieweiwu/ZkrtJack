package zkrtdrone.zkrt.com.maplib.until;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack_xie on 17-2-22.
 */

public class MapHelp {
    static double x_pi = Converter.pi * 3000.0 / 180.0;
    /**
     * Based on the Ramer–Douglas–Peucker algorithm algorithm
     * http://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm
     */
    public static List<LatLng> simplify(List<LatLng> list, double tolerance) {
        int index = 0;
        double dmax = 0;
        int lastIndex = list.size() - 1;

        // Find the point with the maximum distance
        for (int i = 1; i < lastIndex; i++) {
            double d = pointToLineDistance(list.get(0), list.get(lastIndex), list.get(i));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        // If max distance is greater than epsilon, recursively simplify
        List<LatLng> ResultList = new ArrayList<LatLng>();
        if (dmax > tolerance) {
            // Recursive call
            List<LatLng> recResults1 = simplify(list.subList(0, index + 1), tolerance);
            List<LatLng> recResults2 = simplify(list.subList(index, lastIndex + 1), tolerance);

            // Build the result list
            recResults1.remove(recResults1.size() - 1);
            ResultList.addAll(recResults1);
            ResultList.addAll(recResults2);
        } else {
            ResultList.add(list.get(0));
            ResultList.add(list.get(lastIndex));
        }

        // Return the result
        return ResultList;
    }

    public static double pointToLineDistance(LatLng L1, LatLng L2, LatLng P) {
        double A = P.latitude - L1.latitude;
        double B = P.longitude - L1.longitude;
        double C = L2.latitude - L1.latitude;
        double D = L2.longitude - L1.longitude;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = dot / len_sq;

        double xx, yy;

        if (param < 0) // point behind the segment
        {
            xx = L1.latitude;
            yy = L1.longitude;
        } else if (param > 1) // point after the segment
        {
            xx = L2.latitude;
            yy = L2.longitude;
        } else { // point on the side of the segment
            xx = L1.latitude + param * C;
            yy = L1.longitude + param * D;
        }

        return Math.hypot(xx - P.latitude, yy - P.longitude);
    }

    public static LatLng mapLatLngToCoord(LatLng point) {
        double d[] = new double[2];
        //untransformBaidu(point.latitude,point.longitude,d);
        untransform(point.latitude,point.longitude,d);
        return new LatLng(d[0],d[1]);
    }

    public static void untransformBaidu(double wgLat, double wgLon, double[] latlng)
    {
        double x = wgLon - 0.0065, y = wgLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        untransform(gg_lat,gg_lon,latlng);
    }


    public static void untransform(double wgLat, double wgLon, double[] latlng) {
        double d[] = new double[2];
        Converter.transform2(wgLat,wgLon,d);
        latlng[0] = wgLat+(wgLat-d[0]);
        latlng[1] = wgLon+(wgLon-d[1]);
    }
}
