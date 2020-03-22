package com.xts.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xts.shop.adapter.CarRlvAdapter;
import com.xts.shop.adapter.RlvGoodsDetailAttributeAdapter;
import com.xts.shop.bean.AddCarBean;
import com.xts.shop.bean.GoodsDetailBean;
import com.xts.shop.net.ApiService;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.w3c.dom.Text;

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

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {

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
    @BindView(R.id.relat_choose_count)
    RelativeLayout relatChooseCount;
    @BindView(R.id.relat_to_shoppingcar)
    RelativeLayout relatToShoppingcar;
    @BindView(R.id.relat_add_car)
    RelativeLayout relatAddCar;
    @BindView(R.id.ll_bottom_lead)
    LinearLayout ll_bottom_lead;

    private int mId;
    private List<GoodsDetailBean.DataBeanX.AttributeBean> mAttribute;
    private RlvGoodsDetailAttributeAdapter mAttributeAdapter;
    private PopupWindow popupWindow;

    private ImageView pupIv;
    private TextView pupTvPrice;
    private TextView pupTvClose;
    private TextView pupTvReduceCount;
    private TextView pupTvShowCount;
    private TextView pupTvAddCount;
    private GoodsDetailBean.DataBeanX.InfoBean info;
    private GoodsDetailBean alldata;
    private int number;
    private LinearLayout ll_bottom_lead1;
    private PopupWindow popupWindow1;
    private TextView pup_tv_show_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);

        initView();

        mId = getIntent().getIntExtra("id", 0);
        initRecAttribute();
        initData();

    }

    private void initView() {
        RelativeLayout relat_add_car = findViewById(R.id.relat_add_car);
        RelativeLayout relat_choose_count = findViewById(R.id.relat_choose_count);
        ll_bottom_lead1 = findViewById(R.id.ll_bottom_lead);

        relat_add_car.setOnClickListener(this);

        relat_choose_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPup();
            }
        });

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
                        Log.i("tag","===> "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void fillData(GoodsDetailBean goodsDetailBean) {

        info = goodsDetailBean.getData().getInfo();
        alldata = goodsDetailBean;

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
        desc = "<html><head><style type='text/css'>img{max-width:100%;height:auto}</style></head><body>" + desc + "</body></html>";
        mWebDesc.loadData(desc, "text/html", "utf-8");

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

    @OnClick({R.id.banner_goodsdetail, R.id.relat_add_car,
            R.id.relat_choose_count, R.id.relat_to_shoppingcar,
    })
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.banner_goodsdetail:

                break;

            case R.id.relat_add_car:

                addShoppingCar();// 添加购物车

                break;

            case R.id.relat_choose_count:
                showPup();
                break;
            case R.id.pup_tv_add_count:// 添加商品数量

                String s = pup_tv_show_count.getText().toString();
                int number = Integer.parseInt(s);
                if(number <= info.getGoods_number()){
                    number++;
                    pup_tv_show_count.setText(number+"");
                }

                break;

            case R.id.pup_tv_reduce_count:// 减少商品数量
                String ss = pup_tv_show_count.getText().toString();
                int number2 = Integer.parseInt(ss);
                if(number2 >1 ){
                    number2--;
                    pup_tv_show_count.setText(number2+"");
                }
                break;

            case R.id.pup_tv_close:
                if(popupWindow1!=null)
                    popupWindow1.dismiss();// 关闭弹窗
                break;
        }
    }

    private void addShoppingCar() {
        if(popupWindow1!=null) {
            if (alldata != null) {
                List<GoodsDetailBean.DataBeanX.ProductListBean> productList = alldata.getData().getProductList();

                String s = pup_tv_show_count.getText().toString();
                int number = Integer.parseInt(s);
                for (int i = 0; i < productList.size(); i++) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(ApiService.sBaseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();

                    ApiService apiService = retrofit.create(ApiService.class);
                    Flowable<AddCarBean> flowable = apiService.addCarInfo(productList.get(i).getGoods_id(), number
                            , productList.get(i).getId());
                    flowable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ResourceSubscriber<AddCarBean>() {
                                @Override
                                public void onNext(AddCarBean addCarBean) {
                                    if (addCarBean.getErrno() == 0) {
                                        Toast.makeText(GoodsDetailActivity.this,"购物车添加成功",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable t) {
                                    Log.i("tag", "===> " + t.getMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }
        }else{
            showPup();
        }
    }

    private void showPup() {
        // 1  创建弹窗   （控制弹窗）   2 显示弹窗
        View  view =  LayoutInflater.from(this).inflate(R.layout.detail_pup,null);

        // 弹窗的控件
       ImageView  pup_iv = view.findViewById(R.id.pup_iv);
        TextView pup_tv_add_count = view.findViewById(R.id.pup_tv_add_count);
        pup_tv_show_count = view.findViewById(R.id.pup_tv_show_count);
        TextView  pup_tv_reduce_count = view.findViewById(R.id.pup_tv_reduce_count);
        TextView  pup_tv_price = view.findViewById(R.id.pup_tv_price);
        TextView  pup_tv_close = view.findViewById(R.id.pup_tv_close);

        // 设置数据
        if(alldata!=null){
            Glide.with(this).load(alldata.getData().getInfo().getPrimary_pic_url()).into(pup_iv);
            pup_tv_price.setText(alldata.getData().getInfo().getRetail_price()+"");
        }

        // 监听
        pup_tv_close.setOnClickListener(this);
        pup_tv_reduce_count.setOnClickListener(this);
        pup_tv_add_count.setOnClickListener(this);

        // 创建 PopuWindow   三要素  1 布局  2 宽  3 高
        popupWindow1 = new PopupWindow(view, GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.WRAP_CONTENT);

        // 2 显示弹窗  1 显示在某个控件的下方  (可以调整 偏移量)  2 显示在某个容器控件相对位置
        popupWindow1.showAtLocation(ll_bottom_lead1,Gravity.BOTTOM,0,0);


    }
}
