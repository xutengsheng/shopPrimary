package com.xts.shop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xts.shop.adapter.RlvGoodsDetailAttributeAdapter;
import com.xts.shop.bean.GoodsDetailBean;
import com.xts.shop.net.ApiService;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoodsDetailActivity extends AppCompatActivity {

    @BindView(R.id.banner_goodsdetail)
    Banner mBannerGoodsdetail;
    @BindView(R.id.txt_name)
    TextView mTxtName;
    @BindView(R.id.txt_brief)
    TextView mTxtBrief;
    @BindView(R.id.txt_price)
    TextView mTxtPrice;
    @BindView(R.id.txt_count)
    TextView mTxtCount;
    @BindView(R.id.img_right)
    ImageView mImgRight;
    @BindView(R.id.img_avatar)
    ImageView mImgAvatar;
    @BindView(R.id.txt_nickname)
    TextView mTxtNickname;
    @BindView(R.id.txt_addtime)
    TextView mTxtAddtime;
    @BindView(R.id.txt_content)
    TextView mTxtContent;
    @BindView(R.id.img_pic_url)
    ImageView mImgPicUrl;
    @BindView(R.id.rec_attribute)
    RecyclerView mRecAttribute;
    @BindView(R.id.web_desc)
    WebView mWebDesc;
    private int mId;
    private List<GoodsDetailBean.DataBeanX.AttributeBean> mAttribute;
    private RlvGoodsDetailAttributeAdapter mAttributeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);

        mId = getIntent().getIntExtra("id", 0);
        initRecAttribute();
        initData();

    }

    private void initRecAttribute() {
        mRecAttribute.setLayoutManager(new LinearLayoutManager(this));
        mRecAttribute.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAttribute = new ArrayList<>();
        mAttributeAdapter = new RlvGoodsDetailAttributeAdapter(this, mAttribute);
        mRecAttribute.setAdapter(mAttributeAdapter);
    }

    /**
     * 加载数据
     */
    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.sBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Flowable<GoodsDetailBean> flowable = apiService.getGoodsDetail(mId);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<GoodsDetailBean>() {
                    @Override
                    public void onNext(GoodsDetailBean goodsDetailBean) {
                        fillData(goodsDetailBean);//把所有的详情数据填充到界面组件中
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void fillData(GoodsDetailBean goodsDetailBean) {
        //填充banner数据  得到banner数据
        List<GoodsDetailBean.DataBeanX.GalleryBean> banners = goodsDetailBean.getData().getGallery();
        initBanner(banners);
        //填充名字，brief，价钱
        mTxtName.setText(goodsDetailBean.getData().getInfo().getName());
        mTxtBrief.setText(goodsDetailBean.getData().getInfo().getGoods_brief());
        mTxtPrice.setText("￥" + goodsDetailBean.getData().getInfo().getRetail_price());
        //配置评价数据
        mTxtCount.setText("评价(" + goodsDetailBean.getData().getComment().getCount() + ")");

        String avatarUrl = goodsDetailBean.getData().getComment().getData().getAvatar();//图片
        RequestOptions requestOptions = new RequestOptions().circleCrop();//设置圆形图片
        Glide.with(this).load(avatarUrl).apply(requestOptions).into(mImgAvatar);

        mTxtNickname.setText(goodsDetailBean.getData().getComment().getData().getNickname());
        mTxtAddtime.setText(goodsDetailBean.getData().getComment().getData().getAdd_time());
        mTxtContent.setText(goodsDetailBean.getData().getComment().getData().getContent());

        String pic_url = goodsDetailBean.getData().getComment().getData().getPic_list().get(0).getPic_url();//图片
        Glide.with(this).load(pic_url).into(mImgPicUrl);
        //填充 参数数据
        List<GoodsDetailBean.DataBeanX.AttributeBean> attribute = goodsDetailBean.getData().getAttribute();
        mAttribute.addAll(attribute);
        mAttributeAdapter.notifyDataSetChanged();
        //给webview配置数据
        String desc = goodsDetailBean.getData().getInfo().getGoods_desc();
        desc = "<html><head><style type='text/css'>img{max-width:100%;height:auto}</style></head><body>"+desc+"</body></html>";
        mWebDesc.loadData(desc,"text/html","utf-8");

    }

    private void initBanner(List<GoodsDetailBean.DataBeanX.GalleryBean> banners) {
        mBannerGoodsdetail.setImages(banners).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //取出banner对象中的图片url
                GoodsDetailBean.DataBeanX.GalleryBean galleryBean = (GoodsDetailBean.DataBeanX.GalleryBean) path;
                String imgUrl = galleryBean.getImg_url();
                Glide.with(context).load(imgUrl).into(imageView);//加载
            }
        }).start();
    }

    @OnClick(R.id.banner_goodsdetail)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.banner_goodsdetail:
                break;
        }
    }
}
