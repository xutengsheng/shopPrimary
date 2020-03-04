package com.xts.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xts.shop.adapter.VpImageAdapter;

import java.util.ArrayList;

//generatefindviewbyid
public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private ViewPager mVp;
    private TabLayout mTabLayout;
    private ArrayList<String> mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        initTitles();
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mVp = (ViewPager) findViewById(R.id.vp);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

        toolbar();
        vp();
        tab();

        //关联viewpager和TabLayout,需要他们联动
        //viewpager翻页监听
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滑动回调
            }

            @Override
            public void onPageSelected(int position) {
                //页面选中回调
                //让对应的tab选中
                mTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面滑动状态改名
            }
        });

        //tab选中监听
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab选中回调
                //让viewpager翻到对应页面
                int position = tab.getPosition();
                //设置当前的条目
                mVp.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tab取消选中
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //tab重复选中
            }
        });


    }

    private void vp() {
        //类似轮播图的使用(view)
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.tab, null);
            TextView tv = inflate.findViewById(R.id.tv);
            tv.setText("页面:"+i);
            views.add(inflate);
        }

        VpImageAdapter adapter = new VpImageAdapter(views);
        mVp.setAdapter(adapter);

        //配合Fragment使用
    }

    private void toolbar() {
        //设置标题
        //代码提示alt+enter
        mToolBar.setTitle(R.string.title);
        //默认的样式里面有ActionBar,需要使用toolbar代替
        //需要在样式中设置为<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        setSupportActionBar(mToolBar);
    }

    private void tab() {
        //mTabLayout.addTab(mTabLayout.newTab().setText(R.string.main_page));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView(0)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView(1)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView(2)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView(3)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView(4)));
    }

    private void initTitles() {
        //ctrl+d 复制一行
        //ctrl+alt+f 变成员变量
        mTitles = new ArrayList<>();
        //R.string.main_page
        String str = getResources().getString(R.string.main_page);
        mTitles.add(str);
        mTitles.add(getResources().getString(R.string.section));
        mTitles.add(getResources().getString(R.string.category));
        mTitles.add(getResources().getString(R.string.cart));
        mTitles.add(getResources().getString(R.string.me));

    }

    //对外提供tab的view
    public View tabView(int position){
        View inflate = LayoutInflater.from(this).inflate(R.layout.tab, null);
        ImageView iv = inflate.findViewById(R.id.iv);
        TextView tv = inflate.findViewById(R.id.tv);
        switch (position){
            case 0:
                //选择器,根据不同的状态选用不同的资源
                iv.setImageResource(R.drawable.se_main_page);
                break;
            case 1:
                iv.setImageResource(R.drawable.se_section);
                break;
            case 2:
                iv.setImageResource(R.drawable.se_category);
                break;
            case 3:
                iv.setImageResource(R.drawable.se_cart);
                break;
            case 4:
                iv.setImageResource(R.drawable.se_me);
                break;
        }

        tv.setText(mTitles.get(position));
        return inflate;
    }

}
