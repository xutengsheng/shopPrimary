package com.xts.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xts.shop.adapter.VpFragmentAdapter;
import com.xts.shop.adapter.VpImageAdapter;
import com.xts.shop.ui.fragment.CartFragment;
import com.xts.shop.ui.fragment.MainPageFragment;
import com.xts.shop.ui.fragment.MeFragment;
import com.xts.shop.ui.fragment.SortFragment;
import com.xts.shop.ui.fragment.TopicFragment;

import java.util.ArrayList;

//generatefindviewbyid
public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private ViewPager mVp;
    private TabLayout mTabLayout;
    private ArrayList<String> mTitles;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        initTitles();
        initFragments();
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mVp = (ViewPager) findViewById(R.id.vp);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

        toolbar();
        vp();
        //关联viewpager和TabLayout
        mTabLayout.setupWithViewPager(mVp);
        for (int i = 0; i < mFragments.size(); i++) {
            //获取已有的tab,替换里面的view
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(tabView(i));
        }

    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(MainPageFragment.newInstance());
        mFragments.add(TopicFragment.newInstance());
        mFragments.add(SortFragment.newInstance());
        mFragments.add(CartFragment.newInstance());
        mFragments.add(MeFragment.newInstance());
    }

    private void vp() {
        //配合Fragment使用
        VpFragmentAdapter fragmentAdapter =
                new VpFragmentAdapter(getSupportFragmentManager(),
                        mFragments, mTitles
                );
        mVp.setAdapter(fragmentAdapter);
    }

    private void toolbar() {
        //设置标题
        //代码提示alt+enter
        mToolBar.setTitle(R.string.title);
        //默认的样式里面有ActionBar,需要使用toolbar代替
        //需要在样式中设置为<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        setSupportActionBar(mToolBar);
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
    public View tabView(int position) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.tab, null);
        ImageView iv = inflate.findViewById(R.id.iv);
        TextView tv = inflate.findViewById(R.id.tv);
        switch (position) {
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

    //optionMenu使用2步
    //1.创建选项菜单
    //2.菜单的点击事件

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //添加菜单2种方式
        //groupid,菜单组的id
        //itemid,菜单的id
        //order,排序,数字越小排位靠上
        //title,菜单标题
        //添加方式1
        /*menu.add(0,0,1,"删除");
        menu.add(0,1,0,"添加");*/
        //添加方式2
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //2.菜单的点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                showToast(item.getTitle());
                break;
            case R.id.delete:
                showToast(item.getTitle());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("tag","MainActivity销毁");
    }
}
