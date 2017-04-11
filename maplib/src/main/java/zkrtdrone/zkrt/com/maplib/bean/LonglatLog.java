package zkrtdrone.zkrt.com.maplib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by jack_xie on 2016/9/13.
 * 航点
 */
public class LonglatLog implements Parcelable, Serializable {
    private LatLng latLng;
    private String llName;  //地名
    private String llTIme;  //时间

    public static Creator<LonglatLog> getCREATOR() {
        return CREATOR;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public LonglatLog() {
    }


    public LonglatLog(LatLng latLng, String llName, String llTIme) {
        this.latLng = latLng;
        this.llName = llName;
        this.llTIme = llTIme;
    }

    protected LonglatLog(Parcel in) {
        llName = in.readString();
        llTIme = in.readString();
    }

    public static final Creator<LonglatLog> CREATOR = new Creator<LonglatLog>() {
        @Override
        public LonglatLog createFromParcel(Parcel in) {
            return new LonglatLog(in);
        }

        @Override
        public LonglatLog[] newArray(int size) {
            return new LonglatLog[size];
        }
    };


    public String getLlName() {
        return llName;
    }

    public void setLlName(String llName) {
        this.llName = llName;
    }

    public String getLlTIme() {
        return llTIme;
    }

    public void setLlTIme(String llTIme) {
        this.llTIme = llTIme;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }
}
