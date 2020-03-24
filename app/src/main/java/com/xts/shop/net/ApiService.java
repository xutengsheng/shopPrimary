package com.xts.shop.net;

import com.xts.shop.bean.AddCarBean;
import com.xts.shop.bean.CarInfo;
import com.xts.shop.bean.DelCarInfo;
import com.xts.shop.bean.GoodListBean;
import com.xts.shop.bean.GoodsDetailBean;
import com.xts.shop.bean.HomeBean;
import com.xts.shop.bean.TopicBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    String sBaseUrl = "https://cdwan.cn/api/";

    //专题数据请求接口
    @GET("topic/list")
    Flowable<TopicBean> getTopic(@Query("page") int page, @Query("size") int size);

    @GET("index")
    Flowable<HomeBean> getHomeData();

    @GET("goods/detail")
    Flowable<GoodsDetailBean> getGoodsDetail(@Query("id") int id);

    // 添加购物车
    // https://cdwan.cn/api/cart/add
    @POST("cart/add")
    @FormUrlEncoded
    Flowable<AddCarBean> addCarInfo(@Field("goodsId") int goodsId,
                                    @Field("number") int number,
                                    @Field("productId") int productId);
    // 获取购物车数据
    //https://cdwan.cn/api/cart/index
    @GET("cart/index")
    Flowable<CarInfo> getCarDtate();

    //删除购物车请求
    // https://cdwan.cn/api/cart/delete
    @POST("cart/delete")
    @FormUrlEncoded
    Flowable<DelCarInfo> delCarInfo(@Field("productIds") int productIds);

    @GET("goods/list")
    Flowable<GoodListBean> getGoodsList(@Query("keyword") String keyword, @Query("page") int page
            , @Query("size") int size, @Query("sort") String sort, @Query("order") String order, @Query("categoryId") int categoryId);

}
