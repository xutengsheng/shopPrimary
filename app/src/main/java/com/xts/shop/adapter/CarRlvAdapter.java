package com.xts.shop.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xts.shop.R;
import com.xts.shop.bean.CarInfo;
import java.util.ArrayList;

public class CarRlvAdapter extends RecyclerView.Adapter<CarRlvAdapter.CarHolder> {
    private ArrayList<CarInfo.DataBean.CartListBean> list = new ArrayList<>();
    private Context context;

    public CarRlvAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item,parent,false);
        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarHolder holder, int position) {
        CarInfo.DataBean.CartListBean cartListBean = list.get(position);
        Glide.with(context).load(cartListBean.getList_pic_url()).into(holder.car_item_iv);
        holder.car_tv_name.setText(cartListBean.getGoods_name());
        holder.car_tv_price.setText("￥"+cartListBean.getRetail_price());
        holder.car_item_count.setText("X"+cartListBean.getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CarHolder extends RecyclerView.ViewHolder{
        public CheckBox car_item_cb;
        public ImageView  car_item_iv;
        public TextView  car_tv_name;
        public TextView  car_tv_price;
        public TextView  car_item_count;
        public TextView  car_item_chose;
        public TextView  car_item_reduce_count;
        public TextView  car_item_show_count;
        public TextView  car_item_add_count;
        public LinearLayout ll_control_add;

        public CarHolder(@NonNull View itemView) {
            super(itemView);
            car_item_cb = itemView.findViewById(R.id.car_item_cb);
            car_item_iv = itemView.findViewById(R.id.car_item_iv);
            car_tv_name = itemView.findViewById(R.id.car_tv_name);
            car_tv_price = itemView.findViewById(R.id.car_tv_price);
            car_item_count = itemView.findViewById(R.id.car_item_count);
            car_item_chose = itemView.findViewById(R.id.car_item_chose);
            car_item_reduce_count = itemView.findViewById(R.id.car_item_reduce_count);

            car_item_show_count = itemView.findViewById(R.id.car_item_show_count);
            car_item_add_count = itemView.findViewById(R.id.car_item_add_count);

            ll_control_add = itemView.findViewById(R.id.ll_control_add);
        }
    }

    public void refrshAdapter(ArrayList<CarInfo.DataBean.CartListBean> cartList){
        list.addAll(cartList);
        notifyDataSetChanged();
    }
}
