package com.yunongwang.yunongwang.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yunongwang.yunongwang.R;
import com.yunongwang.yunongwang.adapter.RedPocketTabAdapter;
import com.yunongwang.yunongwang.fragment.AvailableFragment;
import com.yunongwang.yunongwang.fragment.ExpiredFragment;
import com.yunongwang.yunongwang.fragment.UsedFragment;
import com.yunongwang.yunongwang.view.red.NoPreloadViewPager;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WANGDONGFEI on 2018/5/20.
 */

public class RedPocketActivity extends AppCompatActivity {
    @BindView(R.id.iv_back)
    ImageView ivback;
    @BindView(R.id.tv_explain)
    TextView tvexplain;
    @BindView(R.id.tabs)
    TabLayout tabs;

    private AvailableFragment availableFragment;
    private UsedFragment usedFragment;
    private ExpiredFragment expiredFragment;
    private NoPreloadViewPager vp;
//    private ViewPager vp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.titleThem), 1);

        RedPocketTabAdapter redPocketTabAdapter = new RedPocketTabAdapter(getSupportFragmentManager());
//        if (availableFragment == null){
//            availableFragment = new AvailableFragment();
//        }
//        if (usedFragment == null){
//            usedFragment = new UsedFragment();
//        }
//        if (expiredFragment == null){
//            expiredFragment = new ExpiredFragment();
//        }
//        redPocketTabAdapter.addFragment(availableFragment);
//        redPocketTabAdapter.addFragment(usedFragment);
//        redPocketTabAdapter.addFragment(expiredFragment);


        tabs.addTab(tabs.newTab().setText("可使用"), 0);
        tabs.addTab(tabs.newTab().setText("已使用"), 1);
        tabs.addTab(tabs.newTab().setText("已过期"), 2);

        vp = (NoPreloadViewPager) findViewById(R.id.vp);
//        vp = (ViewPager) findViewById(R.id.vp);

        vp.setOffscreenPageLimit(0);//让viewpager默认加载的页面置为0 就是不加载
        vp.setAdapter(redPocketTabAdapter);
//        tabs.setupWithViewPager(vp);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                vp.setCurrentItem( tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        tabs.setScrollPosition(0,0f,true);
                        break;
                    case 1:
                        tabs.setScrollPosition(1,0f,true);
                        break;
                    case 2:
                        tabs.setScrollPosition(2,0f,true);
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置tabLayout下划线长度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip,
                Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip,
                Resources.getSystem().getDisplayMetrics());
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_explain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_explain:
                //TODO 说明
                AlertDialog.Builder builder = new AlertDialog.Builder(RedPocketActivity.this);
                builder.setView(R.layout.dialog_redpacket_explain);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
    }
}
