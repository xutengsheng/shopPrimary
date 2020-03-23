package com.xts.shop.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xts.shop.R;
import com.xts.shop.adapter.GoodListAdapter;
import com.xts.shop.bean.GoodListBean;
import com.xts.shop.net.ApiService;
import com.xts.shop.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.fl)
    FlowLayout fl;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.tv_search_no_result)
    TextView tvSearchNoResult;
    @BindView(R.id.rv_search_result)
    RecyclerView rvSearchResult;
    private String keyWord;
    private ArrayList<GoodListBean.DataBeanX.DataBean> list;
    private GoodListAdapter adapter;
    private ArrayList<String> historyList;
    private String keyword, sort = "default", order = "desc";
    private int page = 1, size = 100, categoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        historyList = new ArrayList<>();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //隐藏软键盘
                    hideKeyboard(etSearch);

                    //在这里做操作，一般网络请求、数据库查询
                    keyWord = etSearch.getText().toString();
                    if (list != null && list.size() > 0) {
                        list.clear();
                    }
                    initData();
                    return true;
                }
                return false;
            }
        });

        rvSearchResult.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<>();
        adapter = new GoodListAdapter(this, list);
        rvSearchResult.setAdapter(adapter);
    }

    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void initData() {
        rvSearchResult.setVisibility(View.GONE);
        tvSearchNoResult.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(keyWord)) {
            tvHistory.setVisibility(View.GONE);
            fl.setVisibility(View.GONE);

            if (!historyList.contains(keyWord)) {
                historyList.add(keyWord);
                TextView lable = (TextView) LayoutInflater.from(this).inflate(R.layout.item_fl, null);
                lable.setText(keyWord);

                lable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etSearch.setText(lable.getText().toString());
                    }
                });

                fl.addView(lable);
            }

            getNetResponse();
        } else {
            tvHistory.setVisibility(View.VISIBLE);
            fl.setVisibility(View.VISIBLE);
        }
    }

    private void getNetResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.sBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Flowable<GoodListBean> flowable = apiService.getGoodsList(keyWord, page, size, sort, order, categoryId);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<GoodListBean>() {
                    @Override
                    public void onNext(GoodListBean goodListBean) {
                        List<GoodListBean.DataBeanX.DataBean> data = goodListBean.getData().getData();
                        if (data.size() > 0) {
                            rvSearchResult.setVisibility(View.VISIBLE);
                            list.addAll(data);
                            adapter.notifyDataSetChanged();
                        } else {
                            tvSearchNoResult.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("TAG", "网络请求失败：" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        finish();
    }
}
