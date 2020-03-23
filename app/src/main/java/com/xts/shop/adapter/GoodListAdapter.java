package com.xts.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xts.shop.R;
import com.xts.shop.bean.GoodListBean;

import java.util.ArrayList;

public class GoodListAdapter extends RecyclerView.Adapter<GoodListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<GoodListBean.DataBeanX.DataBean> list;

    public GoodListAdapter(Context context, ArrayList<GoodListBean.DataBeanX.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoodListBean.DataBeanX.DataBean dataBean = list.get(position);
        Glide.with(context).load(dataBean.getList_pic_url()).into(holder.iv_item_search);
        holder.tv_search_name.setText(dataBean.getName());
        holder.tv_search_price.setText("¥ " + dataBean.getRetail_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_item_search;
        public TextView tv_search_name;
        public TextView tv_search_price;

        public ViewHolder(@NonNull View rootView) {
            super(rootView);
            this.iv_item_search = (ImageView) rootView.findViewById(R.id.iv_item_search);
            this.tv_search_name = (TextView) rootView.findViewById(R.id.tv_search_name);
            this.tv_search_price = (TextView) rootView.findViewById(R.id.tv_search_price);
        }
    }

}
