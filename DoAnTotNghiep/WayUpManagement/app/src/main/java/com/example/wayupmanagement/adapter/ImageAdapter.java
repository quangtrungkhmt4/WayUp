package com.example.wayupmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.BitmapImage;
import com.example.wayupmanagement.model.ImageSliding;
import com.example.wayupmanagement.model.Job;
import com.example.wayupmanagement.util.Preferences;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<BitmapImage> arrItem;

    public ImageAdapter(Context context, int layout, List<BitmapImage> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        ImageView img, imgCancel;
    }

    @Override
    public int getCount() {
        return arrItem.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewRow = view;
        if(viewRow == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                viewRow = inflater.inflate(layout,viewGroup,false);
            }

            ViewHolder holder = new ViewHolder();
            if (viewRow != null) {
                holder.imgCancel = viewRow.findViewById(R.id.imgCancel);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imgItem);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final BitmapImage item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
//            RequestOptions requestOptions = new RequestOptions();
//            requestOptions.placeholder(R.drawable.loading);
//            String img = item.getUrl().contains("http")?item.getUrl(): Preferences.getData(Key.DOMAIN, context) + item.getUrl();
//            Glide.with(context).load(img).into(holder.img);
            holder.img.setImageBitmap(item.getBitmap());
        }
        if (holder != null) {
            holder.imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrItem.remove(item);
                    notifyDataSetChanged();
                }
            });
        }

        return viewRow;
    }

}