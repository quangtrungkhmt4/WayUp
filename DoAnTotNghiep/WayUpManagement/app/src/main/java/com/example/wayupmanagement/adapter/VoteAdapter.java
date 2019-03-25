package com.example.wayupmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.model.Vote;

import java.util.List;

public class VoteAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Vote> arrItem;

    public VoteAdapter(Context context, int layout, List<Vote> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView tvCompany, tvPoint;
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
                holder.tvCompany = viewRow.findViewById(R.id.tvCompanyVote);
            }
            if (viewRow != null) {
                holder.tvPoint = viewRow.findViewById(R.id.tvPoint);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final Vote item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvCompany.setText(item.getCompany().getName());
        }
        if (holder != null) {
            holder.tvPoint.setText(String.valueOf(item.getPoint()));
        }

        return viewRow;
    }

}