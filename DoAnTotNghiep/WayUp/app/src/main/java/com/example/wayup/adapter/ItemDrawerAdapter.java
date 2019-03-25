package com.example.wayup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wayup.R;
import com.example.wayup.model.ItemDrawer;
import com.example.wayup.model.Job;

import java.util.List;

public class ItemDrawerAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<ItemDrawer> arrItem;

    public ItemDrawerAdapter(Context context, int layout, List<ItemDrawer> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView tvTitle;
        ImageView img;
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
                holder.tvTitle = viewRow.findViewById(R.id.tvItemDrawer);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imIconDrawerItem);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        ItemDrawer item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvTitle.setText(item.getName());
        }
        if (holder != null) {
            holder.img.setImageResource(item.getIcon());
        }

        return viewRow;
    }

}