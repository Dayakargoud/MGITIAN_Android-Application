package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.dayakar.mgitian.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

   private List<String> list=new ArrayList<>();
   private Context mContext;
   private LayoutInflater layoutInflater;

    public PagerAdapter(List<String> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    public PagerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.view_pager_item,container,false);
        ImageView image=view.findViewById(R.id.viewPager_image);
          image.setImageResource(R.drawable.website);
        Picasso.get().load(list.get(position)).into(image);

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
