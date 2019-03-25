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
import com.bumptech.glide.request.RequestOptions;
import com.example.wayup.R;
import com.example.wayup.action.ApplyAction;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Job;
import com.example.wayup.util.Preferences;

import java.util.List;

public class LVJobAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Job> arrItem;

    public LVJobAdapter(Context context, int layout, List<Job> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        ImageView img;
        TextView tvTitle, tvFastInfo, tvApply;
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
                holder.tvTitle = viewRow.findViewById(R.id.tvTitleJob);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imLogoJob);
            }
            if (viewRow != null) {
                holder.tvFastInfo = viewRow.findViewById(R.id.tvFastInfo);
            }
            if (viewRow != null) {
                holder.tvApply = viewRow.findViewById(R.id.tvApply);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final Job item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvTitle.setText(item.getTitle());
        }
        if (holder != null) {
            holder.tvFastInfo.setText(item.getFast_info());
        }
        if (holder != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.loading);
            String img = item.getThumbnail().contains("http")?item.getThumbnail(): Preferences.getData(Key.DOMAIN, context) + item.getThumbnail();
            Glide.with(context).load(img).into(holder.img);
        }

        holder.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyAction applyAction = new ApplyAction(context, item);
                applyAction.showDialog();
            }
        });

        return viewRow;
    }

}