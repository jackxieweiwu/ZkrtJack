package com.zkrt.zkrtdrone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;

import java.util.ArrayList;
import java.util.List;

import zkrtdrone.zkrt.com.maplib.bean.MissionLatLon;

/**
 * Created by jack_xie on 16-12-28.
 */

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.ViewHolder> implements View.OnClickListener  {
    private List<MissionLatLon> listacc = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int possion = -1;

    public MissionAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setListMission(List<MissionLatLon> lista){
        listacc.clear();
        listacc.addAll(lista);
        this.notifyDataSetChanged();
    }

    public void setMissionIndex(int pos){
        this.possion = pos;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(MissionLatLon)v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , MissionLatLon data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowNameView;
        TextView rowAltitudeView;
        TextView txt_edit_camera;
        RelativeLayout dragHandler;
        public ViewHolder(View view){
            super(view);
            rowNameView = (TextView) view .findViewById(R.id.rowNameView);
            rowAltitudeView = (TextView) view.findViewById(R.id.rowAltitudeView);
            txt_edit_camera = (TextView) view.findViewById(R.id.txt_edit_camera);
            dragHandler = (RelativeLayout) view.findViewById(R.id.dragHandler);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_way_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(listacc.get(position));
        holder.rowNameView.setText(listacc.get(position).getNumber()+"");
        holder.txt_edit_camera.setText(listacc.get(position).getMotion());
        holder.rowAltitudeView.setText(listacc.get(position).getDistance()+"m");
        if(possion == position){  //listacc.get(position).isBool()
            holder.dragHandler.setBackgroundColor(mContext.getResources().getColor(R.color.account_txt_blue));
        }else{
            holder.dragHandler.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_mission_button));
        }
    }

    @Override
    public int getItemCount() {
        return listacc.size();
    }
}
