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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.R;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.ApplyProfile;
import com.example.wayup.model.Profile;
import com.example.wayup.util.DateTime;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LVAppliedAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<ApplyProfile> arrItem;

    public LVAppliedAdapter(Context context, int layout, List<ApplyProfile> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView tvTitle, tvEmail, tvStstus, tvTime;
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
                holder.tvTitle = viewRow.findViewById(R.id.tvTitleJobApplied);
            }
            if (viewRow != null) {
                holder.tvEmail = viewRow.findViewById(R.id.tvEmailJobApplied);
            }
            if (viewRow != null) {
                holder.tvStstus = viewRow.findViewById(R.id.tvStatusJobApplied);
            }
            if (viewRow != null) {
                holder.tvTime = viewRow.findViewById(R.id.tvTimeJobApplied);
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
            holder.tvTitle.setText(item.getJob().getTitle());
        }
        if (holder != null) {
            holder.tvEmail.setText(context.getString(R.string.email) + ": " +item.getEmail());
        }
        if (holder != null) {
            if (item.getStatus() == 0){
                holder.tvStstus.setText(context.getString(R.string.status) + ": " +context.getString(R.string.waitting));
            }else if (item.getStatus() == 1){
                holder.tvStstus.setText(context.getString(R.string.status) + ": " +context.getString(R.string.cancel));
            }else if (item.getStatus() == 2){
                holder.tvStstus.setText(context.getString(R.string.status) + ": " +context.getString(R.string.close));
            }
        }
        if (holder != null) {
            holder.tvTime.setText(DateTime.convertDate(item.getCreated_at()));
        }
        return viewRow;
    }

}