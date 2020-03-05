package com.xts.shop.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xts.shop.R;
import com.xts.shop.adapter.RlvTopicAdapter;
import com.xts.shop.bean.TopicBean;
import com.xts.shop.net.ApiService;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopicFragment extends Fragment {
    public static final String TAG = "TopicFragment";

    private RecyclerView mRlv;
    private SmartRefreshLayout mSrl;
    private int mPage = 1;
    private int mSize = 10;
    private RlvTopicAdapter mAdapter;
    private int mTotalPages;

    public static TopicFragment newInstance() {
        TopicFragment fragment = new TopicFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_topic, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    //关于数据
    @SuppressLint("CheckResult")
    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.sBaseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持rxjava
                .addConverterFactory(GsonConverterFactory.create())//数据转换工厂
                .build();

        retrofit.create(ApiService.class)
                .getTopic(mPage, mSize)
                .subscribeOn(Schedulers.io())//被观察者执行的子线程
                .observeOn(AndroidSchedulers.mainThread())//观察者运行的线程,主线程
                .subscribeWith(new ResourceSubscriber<TopicBean>() {
                    @Override
                    public void onNext(TopicBean topicBean) {
                        Log.d(TAG, "onNext: " + topicBean.toString());
                        //网络数据异步回来后塞到界面中展示
                        if (topicBean != null && topicBean.getErrno() == 0
                                && topicBean.getData() != null && topicBean.getData().getData() != null
                                && topicBean.getData().getData().size() > 0) {
                            mTotalPages = topicBean.getData().getTotalPages();
                            mAdapter.addData(topicBean.getData().getData());
                        }

                        //隐藏下拉刷新和上拉加载的头/脚布局
                        hideHeader();

                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: " + t.toString());
                        hideHeader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void hideHeader() {
        mSrl.finishRefresh();
        mSrl.finishLoadMore();
    }

    //关于界面
    private void initView(@NonNull View view) {
        mRlv = view.findViewById(R.id.rlv);
        mSrl = view.findViewById(R.id.srl);
        //布局管理器,适配器adapter
        mRlv.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<TopicBean.DataBeanX.DataBean> list = new ArrayList<>();
        mAdapter = new RlvTopicAdapter(getContext(), list);
        mRlv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RlvTopicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getContext(), "item click:"+position, Toast.LENGTH_SHORT).show();
            }
        });

        mSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //上拉加载更多
                if (mPage < mTotalPages){
                    mPage++;
                    initData();
                }else {
                    Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新
                mAdapter.mList.clear();
                mPage = 1;
                initData();
            }
        });
    }


}
