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
import com.example.wayup.constant.Key;
import com.example.wayup.model.CompanyComment;
import com.example.wayup.model.Job;
import com.example.wayup.util.DateTime;
import com.example.wayup.util.Preferences;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<CompanyComment> arrItem;

    public CommentAdapter(Context context, int layout, List<CompanyComment> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        ImageView img;
        TextView tvName, tvComment, tvTime;
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
                holder.tvName = viewRow.findViewById(R.id.tvNameComment);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imgComment);
            }
            if (viewRow != null) {
                holder.tvComment = viewRow.findViewById(R.id.tvComment);
            }
            if (viewRow != null) {
                holder.tvTime = viewRow.findViewById(R.id.tvTimeComment);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        CompanyComment item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvName.setText(item.getUser().getName());
        }
        if (holder != null) {
            holder.tvComment.setText(item.getComment());
        }
        if (holder != null) {
            String img = item.getUser().getImage().contains("http")?item.getUser().getImage(): Preferences.getData(Key.DOMAIN, context) + item.getUser().getImage();
            Glide.with(context).load(img).into(holder.img);
        }
        if (holder != null) {
            holder.tvTime.setText(DateTime.convertDate(item.getCreated_at()));
        }
        return viewRow;
    }

}