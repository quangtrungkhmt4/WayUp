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
import com.example.wayup.model.Profile;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LVProfileAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Profile> arrItem;

    public LVProfileAdapter(Context context, int layout, List<Profile> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView tvTitle;
        ImageView imDelete;
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
                holder.tvTitle = viewRow.findViewById(R.id.tvNameProfile);
            }
            if (viewRow != null) {
                holder.imDelete = viewRow.findViewById(R.id.imDeleteProfile);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final Profile item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvTitle.setText(item.getUrl());
        }

        holder.imDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(item);
            }
        });
        return viewRow;
    }

    private void delete(final Profile profile){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE
                , Preferences.getData(Key.IP, context) + API.DELETE_PROFILE + "?id_profile=" + profile.getId_profile(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data = response.getJSONObject("data").get("response").toString();
                        if (data.equalsIgnoreCase("true")){
                            arrItem.remove(profile);
                            notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(context).getmRequestQueue().add(jsonObjectRequest);
    }

}