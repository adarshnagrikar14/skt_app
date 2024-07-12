package com.skt.skillup.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.skt.skillup.R;


public class ImagePagerAdapter extends PagerAdapter {

    private final Context mContext;
    private final int[] mImageIds = {
            R.drawable.number1, R.drawable.number2, R.drawable.number3,
            R.drawable.number4, R.drawable.number5, R.drawable.number6,
            R.drawable.number7, R.drawable.number8, R.drawable.number9,
            R.drawable.number10, R.drawable.number11, R.drawable.number12,
            R.drawable.number13, R.drawable.number14, R.drawable.number15,
            R.drawable.number16, R.drawable.number17, R.drawable.number18,
            R.drawable.number19, R.drawable.number20, R.drawable.number21,
            R.drawable.number22, R.drawable.number23, R.drawable.number24,
            R.drawable.number25, R.drawable.number26, R.drawable.number27
    };

    public ImagePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_image, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mImageIds[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
