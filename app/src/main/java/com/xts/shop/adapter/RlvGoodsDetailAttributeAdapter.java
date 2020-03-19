package com.xts.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xts.shop.R;
import com.xts.shop.bean.GoodsDetailBean;

import java.util.List;

public class RlvGoodsDetailAttributeAdapter extends RecyclerView.Adapter<RlvGoodsDetailAttributeAdapter.ViewHolder> {
    private Context mContext;
    private List<GoodsDetailBean.DataBeanX.AttributeBean> mAttribute;

    public RlvGoodsDetailAttributeAdapter(Context context, List<GoodsDetailBean.DataBeanX.AttributeBean> attribute) {
        mContext = context;
        mAttribute = attribute;
    }

    @NonNull
    @Override
    public RlvGoodsDetailAttributeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.rec_item_goodsdetail_attribute, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RlvGoodsDetailAttributeAdapter.ViewHolder holder, int position) {
        holder.name.setText(mAttribute.get(position).getName());
        holder.value.setText(mAttribute.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return mAttribute.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
            value = itemView.findViewById(R.id.txt_value);
        }
    }
}
