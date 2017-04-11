package com.zkrt.zkrtdrone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.view.widget.HMySeelBar;

import dji.common.airlink.ChannelInterference;

/**
 * Created by jack_xie on 17-3-24.
 */

public class ReoutStatusRecyleViewAdapter extends RecyclerView.Adapter<ReoutStatusRecyleViewAdapter.LiveRoomRecyclerViewHolder> {
    public static ChannelInterference[] channelInterferences;
    private Context mContext;
    private LayoutInflater mInflater;
    private int numSumRou = -1;

    public void initData(ChannelInterference[] channelInterference, int sumRoutatus){
        this.channelInterferences = channelInterference;
        this.numSumRou = sumRoutatus;
    }

    public ReoutStatusRecyleViewAdapter(Context context,ChannelInterference[] ss){
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.channelInterferences = ss;
    }

    @Override
    public int getItemCount() {
        return channelInterferences == null ? 0 : channelInterferences.length;
    }

    @Override
    public LiveRoomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReoutStatusRecyleViewAdapter.LiveRoomRecyclerViewHolder(mInflater.inflate(R.layout.channelinterference,
                parent, false));
    }

    @Override
    public void onBindViewHolder(LiveRoomRecyclerViewHolder holder, int position) {
        holder.setData(channelInterferences[position]);
    }

    public class LiveRoomRecyclerViewHolder extends RecyclerView.ViewHolder {
        public HMySeelBar seekar_list_route_item;
        public TextView txt_route_list_item;

        public LiveRoomRecyclerViewHolder(View layout) {
            super(layout);
            seekar_list_route_item = (HMySeelBar) layout.findViewById(R.id.seekar_list_route_item);
            seekar_list_route_item.setMax(100);
            txt_route_list_item = (TextView) layout.findViewById(R.id.txt_route_list_item);
        }

        public  void setData(ChannelInterference channelInterference){
            if (channelInterference == null)return;

            //portrait 链接
            String name = "";
            int channelNum = channelInterference.getChannel();
            if(channelNum == 0) name = "信道13";
            if(channelNum == 1) name = "信道14";
            if(channelNum == 2) name = "信道15";
            if(channelNum == 3) name = "信道16";
            if(channelNum == 4) name = "信道17";
            if(channelNum == 5) name = "信道18";
            if(channelNum == 6) name = "信道19";
            if(channelNum == 7) name = "信道20";
            txt_route_list_item.setText(name);
            int numbera = 100+channelInterference.getPower();
            if(channelNum == numSumRou){
                txt_route_list_item.setTextColor(mContext.getResources().getColor(R.color.holo_blue_dark));
            }else if (numbera > 0 && numbera < 20) {
                txt_route_list_item.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
            }
            if (numbera >= 20 && numbera <= 50) {
                txt_route_list_item.setTextColor(mContext.getResources().getColor(R.color.yellow));
            }
            if (numbera > 50) {
                txt_route_list_item.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
            }
            seekar_list_route_item.setProgress(numbera);

            /*if(channelNum == numSumRou){
                seekar_list_route_item.setProgressDrawable(mContext.getDrawable(android.R.color.holo_blue_dark));
            }

            if (numbera > 0 && numbera < 30) {
                seekar_list_route_item.setProgressDrawable(mContext.getDrawable(android.R.color.holo_green_dark));
            }
            if (numbera >= 30 && numbera <= 50) {
                seekar_list_route_item.setProgressDrawable(mContext.getDrawable(R.color.yellow));
            }

            if (numbera > 50) {
                seekar_list_route_item.setProgressDrawable(mContext.getDrawable(android.R.color.holo_red_dark));
            }*/

            //seekar_list_route_item.setBackgroundColor();
        /*String portrait =  itemModel.portrait;//每一个item在ArrayList中的位置的liveCreator(UserInfoModel)的图片的链接
        mNewsPicture.setImageURI(ImageUrlParser.avatarRoomHeadImageUrl(portrait));*/
        }
    }
}
