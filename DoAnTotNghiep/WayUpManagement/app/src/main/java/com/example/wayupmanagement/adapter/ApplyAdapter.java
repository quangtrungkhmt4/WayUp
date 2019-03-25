package com.example.wayupmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.ApplyProfile;
import com.example.wayupmanagement.model.Job;
import com.example.wayupmanagement.util.DateTime;
import com.example.wayupmanagement.util.Preferences;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplyAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<ApplyProfile> arrItem;

    public ApplyAdapter(Context context, int layout, List<ApplyProfile> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        CircleImageView img;
        TextView tvName, tvEmail, tvStatus, tvTime;
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
                holder.tvName = viewRow.findViewById(R.id.tvNameApply);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imgAvatarApply);
            }
            if (viewRow != null) {
                holder.tvEmail = viewRow.findViewById(R.id.tvEmailApply);
            }
            if (viewRow != null) {
                holder.tvStatus = viewRow.findViewById(R.id.tvStatusApply);
            }
            if (viewRow != null) {
                holder.tvTime = viewRow.findViewById(R.id.tvTimeApply);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final ApplyProfile item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvName.setText(item.getUser().getName());
        }
        if (holder != null) {
            holder.tvEmail.setText(item.getEmail());
        }
        if (holder != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.loading);
            String img = item.getUser().getImage().contains("http")?item.getUser().getImage(): Preferences.getData(Key.DOMAIN, context) + item.getUser().getImage();
            Glide.with(context).load(img).into(holder.img);
        }
        if (holder != null) {
            holder.tvTime.setText(DateTime.convertDate(item.getCreated_at()));
        }
        if (holder != null) {
            if (item.getStatus() == 0){
                holder.tvStatus.setText(context.getString(R.string.status) + ": "  + "Đang chờ");
            }else if (item.getStatus() == 1){
                holder.tvStatus.setText(context.getString(R.string.status) + ": "  + "Đã hủy");
            }else {
                holder.tvStatus.setText(context.getString(R.string.status) + ": "  + "Đã đóng");
            }
        }

        return viewRow;
    }

}