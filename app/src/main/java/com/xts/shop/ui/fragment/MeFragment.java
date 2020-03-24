package com.xts.shop.ui.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xts.shop.MeReceiver;
import com.xts.shop.MyService;
import com.xts.shop.R;

/**
 * 广播是android 四大组件之一  跨进程 跨模块 跨（域）  通信
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private ImageView mImgUser;
    private TextView mUsename;
    private RelativeLayout mLayoutName;
    private GridView mGridview;

    String []  titles = {"优惠卷","我的订单","礼品卡","我的收藏","我的足迹","会员福利"};
    private MeReceiver meReceiver;

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_me, container, false);
        initView(inflate);
        initReceiver();

        return inflate;
    }

    /**
     * 动态  注册广播 ----取消注册（内存泄漏）
     */
    private void initReceiver() {
        meReceiver = new MeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.test.me_msg");

        getActivity().registerReceiver(meReceiver,intentFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().unregisterReceiver(meReceiver);
    }

    private void initView(@NonNull final View itemView) {
        mImgUser = (ImageView) itemView.findViewById(R.id.user_img);
        mUsename = (TextView) itemView.findViewById(R.id.usename);
        mLayoutName = (RelativeLayout) itemView.findViewById(R.id.name_layout);
        mLayoutName.setOnClickListener(this);
        mGridview = (GridView) itemView.findViewById(R.id.gridview);
        mGridview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public Object getItem(int i) {
                return titles[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View root = LayoutInflater.from(getActivity()).inflate(R.layout.me_grid_item,null);

                TextView titleTv = root.findViewById(R.id.title);
                titleTv.setText(titles[i]);
                return root;
            }
        });

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                Log.d(TAG, "onItemClick: ");
                // 发送广播  action data
                Intent intent = new Intent();
                intent.setAction("com.test.me_msg");
                intent.putExtra("title",titles[pos]);

                getActivity().sendBroadcast(intent);
            }
        });
    }

    private static final String TAG = "MeFragment";
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_layout:
                // TODO 20/03/24

//                启动服务
                Intent intent = new Intent(getActivity(), MyService.class);
                getActivity().startService(intent);

                break;
            default:
                break;
        }
    }
}
