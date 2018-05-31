package com.yunongwang.yunongwang.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunongwang.yunongwang.R;
import com.yunongwang.yunongwang.adapter.ExpiredAdapter;
import com.yunongwang.yunongwang.bean.RedPacketBean;
import com.yunongwang.yunongwang.util.Constant;
import com.yunongwang.yunongwang.util.OkUtils;
import com.yunongwang.yunongwang.util.SharePrefsHelper;
import com.yunongwang.yunongwang.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yyyy on 2018/5/17.
 * 域农红包已过期fragment
 */

public class ExpiredFragment extends BaseFragment {
    public ArrayList<RedPacketBean> list = new ArrayList<>();
    //用户Id
    private String userId;
    //page页数
    private int PAGE_NO = 1;
    private RedPacketBean bean;
    private PullToRefreshListView list_rp_expired;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_expired,container,false);
        list_rp_expired= (PullToRefreshListView) v.findViewById(R.id.list_rp_expired);
        new Handler(Looper.getMainLooper()).postDelayed(LOAD_DATA,50);

//        requestData();
//        initListView();
        return v;
    }

    private Runnable LOAD_DATA = new Runnable() { @Override public void run() {

        requestData();
        initListView();

    } };


    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser)
            new Handler(Looper.getMainLooper()).removeCallbacks(LOAD_DATA); }

    private void initListView() {
        ILoadingLayout startLabels = list_rp_expired.getLoadingLayoutProxy(true, true);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = list_rp_expired.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        list_rp_expired.setMode(PullToRefreshBase.Mode.BOTH);//两端刷新

        list_rp_expired.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {

            }
        });
        list_rp_expired.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                requestData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list_rp_expired.onRefreshComplete();
                    }
                },3000);
                refreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("加载完成");
                        list_rp_expired.onRefreshComplete();
                    }
                },3000);
                refreshComplete();
            }
        });
    }

    /**
     * 刷新完成时关闭
     */
    public void refreshComplete() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list_rp_expired.onRefreshComplete();
            }
        }, 1000);
    }

    private void requestData() {
        userId = SharePrefsHelper.getString(SharePrefsHelper.UserId, "");
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("page", PAGE_NO + "");
        map.put("screen", 2 + "");
        OkUtils.UploadSJ(Constant.MINE_REDPACKET_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AvailableFragment", "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("ExpiredFragment=======", s);
                Gson gson = new Gson();
                bean = gson.fromJson(s, RedPacketBean.class);
                final String msg=bean.getMassage();
                final int code=bean.getCode();
//                Log.e("ExpiredFragment000",code+"");
//                Log.e("ExpiredFragment1111", msg);
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        Looper.prepare();
//                        if (500==code){
//                            ToastUtil.showToast("请求成功");
//                            Looper.loop();
//                        }
//                        else{
//                            ToastUtil.showToast(msg);
//                        }
//                    }
//                }.start();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ExpiredAdapter adapter = new ExpiredAdapter(getActivity(),list);
                        list_rp_expired.setAdapter(adapter);
                    }
                });
            }
        });

    }

    @Override
    public void loadData(String url, boolean isRefresh, boolean isTurn) {

    }
}
