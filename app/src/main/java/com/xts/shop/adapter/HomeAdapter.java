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
import com.xts.shop.bean.HomeBean;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<HomeBean.DataBean.BannerBean> banners;
    private ArrayList<HomeBean.DataBean.CategoryListBean.GoodsListBean> list;
    private int VIEW_TYPE_ONE = 1;
    private int VIEW_TYPE_TWO = 2;
    private final LayoutInflater inflater;

    public HomeAdapter(Context context, ArrayList<HomeBean.DataBean.BannerBean> banners, ArrayList<HomeBean.DataBean.CategoryListBean.GoodsListBean> list) {
        this.context = context;
        this.banners = banners;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 创建条目布局并返回对应viewholder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ONE) {
            View inflate = inflater.inflate(R.layout.item_banner, null);
            return new ViewHolderOne(inflate);
        } else {
            View view = inflater.inflate(R.layout.item_list, null);
            return new ViewHolderTwo(view);
        }
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = holder.getItemViewType();

        if (itemViewType == VIEW_TYPE_ONE) {
            ArrayList<String> images = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            for (int i = 0; i < banners.size(); i++) {
                images.add(banners.get(i).getImage_url());
                titles.add(banners.get(i).getName());
            }
            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            viewHolderOne.banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE).setImages(images).setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(context).load(path).into(imageView);
                }
            }).setBannerTitles(titles).start();
        } else {
            HomeBean.DataBean.CategoryListBean.GoodsListBean goodsList = list.get(position - 1);
            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            viewHolderTwo.tv_home_name.setText(goodsList.getName());
            Glide.with(context).load(goodsList.getList_pic_url()).into(viewHolderTwo.iv_home_item);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLis.onItemClick(position-1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_ONE;
        } else {
            return VIEW_TYPE_TWO;
        }
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        public Banner banner;

        public ViewHolderOne(View rootView) {
            super(rootView);
            this.banner = (Banner) rootView.findViewById(R.id.banner);
        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder {
        public ImageView iv_home_item;
        public TextView tv_home_name;

        public ViewHolderTwo(View rootView) {
            super(rootView);
            this.iv_home_item = (ImageView) rootView.findViewById(R.id.iv_home_item);
            this.tv_home_name = (TextView) rootView.findViewById(R.id.tv_home_name);
        }

    }

    private OnItemClickLis mOnItemClickLis;

    public void setOnItemClickLis(OnItemClickLis onItemClickLis) {
        mOnItemClickLis = onItemClickLis;
    }
//条目点击的接口回调
    public interface OnItemClickLis{
        void onItemClick(int position);
    }
}
