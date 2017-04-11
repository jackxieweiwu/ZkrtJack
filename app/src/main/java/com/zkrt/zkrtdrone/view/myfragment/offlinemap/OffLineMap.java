package com.zkrt.zkrtdrone.view.myfragment.offlinemap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.offline.OfflineDownloadedAdapter;
import com.zkrt.zkrtdrone.offline.OfflineListAdapter;
import com.zkrt.zkrtdrone.offline.OfflinePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.ToastUtil;

/**
 * Created by jack_xie on 17-1-21.
 */

public class OffLineMap extends BaseMvpFragment implements OfflineMapManager.OfflineMapDownloadListener,
        ViewPager.OnPageChangeListener{
    @BindView(R.id.content_viewpage)ViewPager mContentViewPage;
    @BindView(R.id.download_list_text)TextView mDownloadText;
    @BindView(R.id.downloaded_list_text) TextView mDownloadedText;
    private OfflineDownloadedAdapter mDownloadedAdapter;
    private PagerAdapter mPageAdapter;
    private ExpandableListView mAllOfflineMapList;
    private ListView mDownLoadedList;
    private OfflineListAdapter adapter;
    /**
     * 更新所有列表
     */
    private final static int UPDATE_LIST = 0;
    private final static int SHOW_MSG = 1;
    private final static int DISMISS_INIT_DIALOG = 2;
    private final static int SHOW_INIT_DIALOG = 3;
    private OfflineMapManager amapManager = null;// 离线地图下载控制器
    private List<OfflineMapProvince> provinceList = new ArrayList<OfflineMapProvince>();// 保存一级目录的省直辖市
    @Override
    protected int getFragmentViewId() {
        return R.layout.offline_map_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initAllCityList();
        initDownloadedList();
        mPageAdapter = new OfflinePagerAdapter(mContentViewPage,
                mAllOfflineMapList, mDownLoadedList);
        mContentViewPage.setAdapter(mPageAdapter);
        mContentViewPage.setCurrentItem(0);
        mContentViewPage.setOnPageChangeListener(this);
    }

    @OnClick(R.id.download_list_text)
    public void downLoad_list_Texta(View v){
        int paddingHorizontal = mDownloadText.getPaddingLeft();
        int paddingVertical = mDownloadText.getPaddingTop();
        mContentViewPage.setCurrentItem(0);
        mDownloadText
                .setBackgroundResource(R.mipmap.offlinearrow_tab1_pressed);
        mDownloadedText
                .setBackgroundResource(R.mipmap.offlinearrow_tab2_normal);
        mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
                paddingHorizontal, paddingVertical);
        mDownloadText.setPadding(paddingHorizontal, paddingVertical,
                paddingHorizontal, paddingVertical);
        mDownloadedAdapter.notifyDataChange();
    }

    @OnClick(R.id.downloaded_list_text)
    public void downloaded_list_texta(View v){
        int paddingHorizontal = mDownloadedText.getPaddingLeft();
        int paddingVertical = mDownloadedText.getPaddingTop();
        mContentViewPage.setCurrentItem(1);

        mDownloadText
                .setBackgroundResource(R.mipmap.offlinearrow_tab1_normal);
        mDownloadedText
                .setBackgroundResource(R.mipmap.offlinearrow_tab2_pressed);
        mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
                paddingHorizontal, paddingVertical);
        mDownloadText.setPadding(paddingHorizontal, paddingVertical,
                paddingHorizontal, paddingVertical);

        mDownloadedAdapter.notifyDataChange();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_LIST:
                    if (mContentViewPage.getCurrentItem() == 0) {
                        ((BaseExpandableListAdapter) adapter)
                                .notifyDataSetChanged();
                    } else {
                        mDownloadedAdapter.notifyDataChange();
                    }

                    break;
                case SHOW_MSG:
//				Toast.makeText(OfflineMapActivity.this, (String) msg.obj,
//						Toast.LENGTH_SHORT).show()
                    ToastUtil.show(mActivity, (String)msg.obj);
                    break;

                case DISMISS_INIT_DIALOG:
                    handler.sendEmptyMessage(UPDATE_LIST);
                    break;
                case SHOW_INIT_DIALOG:

                    break;

                default:
                    break;
            }
        }
    };



    /**
     * 初始化所有城市列表
     */
    public void initAllCityList() {
        // 扩展列表
        View provinceContainer = LayoutInflater.from(getContext())
                .inflate(R.layout.offline_province_listview, null);
        mAllOfflineMapList = (ExpandableListView) provinceContainer
                .findViewById(R.id.province_download_list);

        amapManager = new OfflineMapManager(getContext(), this);
        initProvinceListAndCityMap();
        adapter = new OfflineListAdapter(provinceList, amapManager,
                mActivity);
        // 为列表绑定数据源
        mAllOfflineMapList.setAdapter(adapter);
        // adapter实现了扩展列表的展开与合并监听
        mAllOfflineMapList.setOnGroupCollapseListener(adapter);
        mAllOfflineMapList.setOnGroupExpandListener(adapter);
        mAllOfflineMapList.setGroupIndicator(null);
    }

    /**
     * sdk内部存放形式为<br>
     * 省份 - 各自子城市<br>
     * 北京-北京<br>
     * ...<br>
     * 澳门-澳门<br>
     * 概要图-概要图<br>
     * <br>
     * 修改一下存放结构:<br>
     * 概要图-概要图<br>
     * 直辖市-四个直辖市<br>
     * 港澳-澳门香港<br>
     * 省份-各自子城市<br>
     */
    private void initProvinceListAndCityMap() {

        List<OfflineMapProvince> lists = amapManager
                .getOfflineMapProvinceList();

        provinceList.add(null);
        provinceList.add(null);
        provinceList.add(null);
        // 添加3个null 以防后面添加出现 index out of bounds

        ArrayList<OfflineMapCity> cityList = new ArrayList<OfflineMapCity>();// 以市格式保存直辖市、港澳、全国概要图
        ArrayList<OfflineMapCity> gangaoList = new ArrayList<OfflineMapCity>();// 保存港澳城市
        ArrayList<OfflineMapCity> gaiyaotuList = new ArrayList<OfflineMapCity>();// 保存概要图

        for (int i = 0; i < lists.size(); i++) {
            OfflineMapProvince province = lists.get(i);
            if (province.getCityList().size() != 1) {
                // 普通省份
                provinceList.add(i + 3, province);
                // cityMap.put(i + 3, cities);
            } else {
                String name = province.getProvinceName();
                if (name.contains("香港")) {
                    gangaoList.addAll(province.getCityList());
                } else if (name.contains("澳门")) {
                    gangaoList.addAll(province.getCityList());
                } else if (name.contains("全国概要图")) {
                    gaiyaotuList.addAll(province.getCityList());
                } else {
                    // 直辖市
                    cityList.addAll(province.getCityList());
                }
            }
        }

        // 添加，概要图，直辖市，港口
        OfflineMapProvince gaiyaotu = new OfflineMapProvince();
        gaiyaotu.setProvinceName("概要图");
        gaiyaotu.setCityList(gaiyaotuList);
        provinceList.set(0, gaiyaotu);// 使用set替换掉刚开始的null

        OfflineMapProvince zhixiashi = new OfflineMapProvince();
        zhixiashi.setProvinceName("直辖市");
        zhixiashi.setCityList(cityList);
        provinceList.set(1, zhixiashi);

        OfflineMapProvince gaogao = new OfflineMapProvince();
        gaogao.setProvinceName("港澳");
        gaogao.setCityList(gangaoList);
        provinceList.set(2, gaogao);

        // cityMap.put(0, gaiyaotuList);// 在HashMap中第0位置添加全国概要图
        // cityMap.put(1, cityList);// 在HashMap中第1位置添加直辖市
        // cityMap.put(2, gangaoList);// 在HashMap中第2位置添加港澳

    }

    /**
     * 初始化已下载列表
     */
    public void initDownloadedList() {
        mDownLoadedList = (ListView) LayoutInflater.from(
                getContext()).inflate(
                R.layout.offline_downloaded_list, null);
        android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(
                android.widget.AbsListView.LayoutParams.MATCH_PARENT,
                android.widget.AbsListView.LayoutParams.WRAP_CONTENT);
        mDownLoadedList.setLayoutParams(params);
        mDownloadedAdapter = new OfflineDownloadedAdapter(getContext(), amapManager);
        mDownLoadedList.setAdapter(mDownloadedAdapter);
    }

    /**
     * 暂停所有下载和等待
     */
    private void stopAll() {
        if (amapManager != null) {
            amapManager.stop();
        }
    }

    /**
     * 继续下载所有暂停中
     */
    private void startAllInPause() {
        if (amapManager == null) {
            return;
        }
        for (OfflineMapCity mapCity : amapManager.getDownloadingCityList()) {
            if (mapCity.getState() == OfflineMapStatus.PAUSE) {
                try {
                    amapManager.downloadByCityName(mapCity.getCity());
                } catch (AMapException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 取消所有<br>
     * 即：删除下载列表中除了已完成的所有<br>
     * 会在OfflineMapDownloadListener.onRemove接口中回调是否取消（删除）成功
     */
    private void cancelAll() {
        if (amapManager == null) {
            return;
        }
        for (OfflineMapCity mapCity : amapManager.getDownloadingCityList()) {
            if (mapCity.getState() == OfflineMapStatus.PAUSE) {
                amapManager.remove(mapCity.getCity());
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int paddingHorizontal = mDownloadedText.getPaddingLeft();
        int paddingVertical = mDownloadedText.getPaddingTop();

        switch (position) {
            case 0:
                mDownloadText
                        .setBackgroundResource(R.mipmap.offlinearrow_tab1_pressed);
                mDownloadedText
                        .setBackgroundResource(R.mipmap.offlinearrow_tab2_normal);
                // mPageAdapter.notifyDataSetChanged();
                break;
            case 1:
                mDownloadText
                        .setBackgroundResource(R.mipmap.offlinearrow_tab1_normal);

                mDownloadedText
                        .setBackgroundResource(R.mipmap.offlinearrow_tab2_pressed);
                // mDownloadedAdapter.notifyDataChange();
                break;
        }
        handler.sendEmptyMessage(UPDATE_LIST);
        mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
                paddingHorizontal, paddingVertical);
        mDownloadText.setPadding(paddingHorizontal, paddingVertical,
                paddingHorizontal, paddingVertical);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDownload(int status, int completeCode, String downName) {
        switch (status) {
            case OfflineMapStatus.SUCCESS:
                // changeOfflineMapTitle(OfflineMapStatus.SUCCESS, downName);
                break;
            case OfflineMapStatus.LOADING:
                Log.d("amap-download", "download: " + completeCode + "%" + ","
                        + downName);
                // changeOfflineMapTitle(OfflineMapStatus.LOADING, downName);
                break;
            case OfflineMapStatus.UNZIP:
                Log.d("amap-unzip", "unzip: " + completeCode + "%" + "," + downName);
                // changeOfflineMapTitle(OfflineMapStatus.UNZIP);
                // changeOfflineMapTitle(OfflineMapStatus.UNZIP, downName);
                break;
            case OfflineMapStatus.WAITING:
                Log.d("amap-waiting", "WAITING: " + completeCode + "%" + ","
                        + downName);
                break;
            case OfflineMapStatus.PAUSE:
                Log.d("amap-pause", "pause: " + completeCode + "%" + "," + downName);
                break;
            case OfflineMapStatus.STOP:
                break;
            case OfflineMapStatus.ERROR:
                Log.e("amap-download", "download: " + " ERROR " + downName);
                break;
            case OfflineMapStatus.EXCEPTION_AMAP:
                Log.e("amap-download", "download: " + " EXCEPTION_AMAP " + downName);
                break;
            case OfflineMapStatus.EXCEPTION_NETWORK_LOADING:
                Log.e("amap-download", "download: " + " EXCEPTION_NETWORK_LOADING "
                        + downName);
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT)
                        .show();
                amapManager.pause();
                break;
            case OfflineMapStatus.EXCEPTION_SDCARD:
                Log.e("amap-download", "download: " + " EXCEPTION_SDCARD "
                        + downName);
                break;
            default:
                break;
        }

        // changeOfflineMapTitle(status, downName);
        handler.sendEmptyMessage(UPDATE_LIST);
    }

    @Override
    public void onCheckUpdate(boolean hasNew, String name) {
        Message message = new Message();
        message.what = SHOW_MSG;
        message.obj = "CheckUpdate " + name + " : " + hasNew;
        handler.sendMessage(message);
    }

    @Override
    public void onRemove(boolean success, String name, String describe) {
        handler.sendEmptyMessage(UPDATE_LIST);
        Message message = new Message();
        message.what = SHOW_MSG;
        message.obj = "onRemove " + name + " : " + success + " , " + describe;
        handler.sendMessage(message);
    }
}
