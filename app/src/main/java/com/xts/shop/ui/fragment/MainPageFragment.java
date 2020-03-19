package com.xts.shop.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xts.shop.GoodsDetailActivity;
import com.xts.shop.R;
import com.xts.shop.adapter.HomeAdapter;
import com.xts.shop.bean.HomeBean;
import com.xts.shop.net.ApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  RecyclerView基本使用以及多布局实现
 *  1.添加 依赖，创建布局，找控件
 *  2.设置布局管理器：LinearLayoutManager、GridLayoutManager、StaggeredGridLayoutManager
 *  3.创建适配器（*****）
 *      单布局：
 *      多布局：
 *  4.设置适配器
 */
public class MainPageFragment extends Fragment {

    private RecyclerView rv;
    private HomeAdapter adapter;
    private ArrayList<HomeBean.DataBean.BannerBean> banners;
    private ArrayList<HomeBean.DataBean.CategoryListBean.GoodsListBean> list;
    //    private ArrayList<HomeBean.DataBean.NewGoodsListBean> list;

    public static MainPageFragment newInstance() {
        MainPageFragment fragment = new MainPageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main_page, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initData() {
        //获取retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.sBaseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //获取接口对象
        ApiService apiService = retrofit.create(ApiService.class);

        //调用接口方法
        Flowable<HomeBean> homeData = apiService.getHomeData();

        //执行请求
        homeData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<HomeBean>() {
                    @Override
                    public void onNext(HomeBean homeBean) {
                        List<HomeBean.DataBean.BannerBean> banner = homeBean.getData().getBanner();
                        List<HomeBean.DataBean.CategoryListBean.GoodsListBean> goodsList = homeBean.getData().getCategoryList().get(0).getGoodsList();

                        banners.addAll(banner);
                        list.addAll(goodsList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getActivity(), "网络错误："+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initView(View view) {

        rv = view.findViewById(R.id.rv);
//        new GridLayoutManager():网格布局
////        new StaggeredGridLayoutManager():瀑布流效果
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        banners = new ArrayList<>();
//        list = new ArrayList<>();
        list = new ArrayList<>();

//        adapter = new HomeAdapter(getActivity(), banners, list);
        adapter = new HomeAdapter(getActivity(),banners,list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickLis(new HomeAdapter.OnItemClickLis() {
            @Override
            public void onItemClick(int position) {
                //得到id  跳转到详情页面,把id传递过去
                int id = list.get(position).getId();
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

    }
}
