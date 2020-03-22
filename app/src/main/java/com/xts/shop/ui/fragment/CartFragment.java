package com.xts.shop.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xts.shop.R;
import com.xts.shop.adapter.CarRlvAdapter;
import com.xts.shop.bean.CarInfo;
import com.xts.shop.bean.GoodsDetailBean;
import com.xts.shop.net.ApiService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartFragment extends Fragment implements View.OnClickListener {

    private TextView car_tv_count_price;
    private CarRlvAdapter carRlvAdapter;

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_cart, container, false);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView(getView());
    }

    @SuppressLint("CheckResult")
    private void initData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.sBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Flowable<CarInfo> flowable = apiService.getCarDtate();
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<CarInfo>() {
                    @Override
                    public void onNext(CarInfo carInfo) {
                        ArrayList<CarInfo.DataBean.CartListBean> cartList =
                                (ArrayList<CarInfo.DataBean.CartListBean>) carInfo.getData().getCartList();
                        carRlvAdapter.refrshAdapter(cartList);

                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("tag","===> "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initView(View view) {

        RecyclerView car_recycler = view.findViewById(R.id.car_recycler);
        car_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        carRlvAdapter = new CarRlvAdapter(getContext());
        car_recycler.setAdapter(carRlvAdapter);

        Button car_btn_car_press = view.findViewById(R.id.car_btn_car_press);
        CheckBox car_cb_all = view.findViewById(R.id.car_cb_all);
        TextView car_tv_edit_state = view.findViewById(R.id.car_tv_edit_state);
        car_tv_count_price = view.findViewById(R.id.car_tv_count_price);

        car_btn_car_press.setOnClickListener(this);
        car_cb_all.setOnClickListener(this);
        car_tv_edit_state.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_btn_car_press:

                break;

            case R.id.car_cb_all:

                break;

            case R.id.car_tv_edit_state:

                break;
        }
    }
}
