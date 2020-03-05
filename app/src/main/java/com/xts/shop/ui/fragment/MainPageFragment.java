package com.xts.shop.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.xts.shop.R;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

public class MainPageFragment extends Fragment {

    private Banner mBanner;

    public static MainPageFragment newInstance(){
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
    }

    private void initView(View view) {
        mBanner = view.findViewById(R.id.banner);

        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_menu_shoping_pressed);
        images.add(R.drawable.ic_menu_sort_nor);
        images.add(R.drawable.ic_menu_sort_pressed);
        images.add(R.drawable.ic_menu_choice_pressed);
        mBanner.setImages(images)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        //path的类型指的是setImages里面集合的泛型
                        Glide.with(context).load(path).into(imageView);
                    }
                })
                .start();
    }
}
