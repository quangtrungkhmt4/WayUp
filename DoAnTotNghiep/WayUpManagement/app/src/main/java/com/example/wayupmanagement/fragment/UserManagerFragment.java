package com.example.wayupmanagement.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayupmanagement.InfoUserActivity;
import com.example.wayupmanagement.LoginActivity;
import com.example.wayupmanagement.MainAdminActivity;
import com.example.wayupmanagement.MainHrActivity;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.adapter.UserAdapter;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.Hr;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.VolleySingleton;
import com.example.wayupmanagement.view.CustomListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserManagerFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private LinearLayout lnHr, lnUser, lnLock;
    private ImageView imShowHr, imShowUser, imShowLock;
    private CustomListView lvHr, lvUser, lvLock;
    private MainAdminActivity mainAdminActivity;
    private RequestQueue requestQueue;
    private UserAdapter hrAdapter;
    private UserAdapter userAdapter;
    private UserAdapter lockAdapter;

    private List<User> hrs = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<User> locks = new ArrayList<>();
    private boolean isShowHr = true;
    private boolean isShowUser = true;
    private boolean isShowLock = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_manager, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        lnHr = view.findViewById(R.id.lnHr);
        lnUser = view.findViewById(R.id.lnUser);
        imShowHr = view.findViewById(R.id.imShowHr);
        imShowUser = view.findViewById(R.id.imShowUser);
        lvHr = view.findViewById(R.id.lvHr);
        lvUser = view.findViewById(R.id.lvUser);
        lnLock = view.findViewById(R.id.lnLock);
        imShowLock = view.findViewById(R.id.imShowLock);
        lvLock = view.findViewById(R.id.lvLock);

    }

    private void initViews() {
        mainAdminActivity = (MainAdminActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        hrAdapter = new UserAdapter(mainAdminActivity, R.layout.item_apply, hrs);
        userAdapter = new UserAdapter(mainAdminActivity, R.layout.item_apply, users);
        lvUser.setAdapter(userAdapter);
        lvHr.setAdapter(hrAdapter);
        lockAdapter = new UserAdapter(mainAdminActivity, R.layout.item_apply, locks);
        lvLock.setAdapter(lockAdapter);

        getHr();
        getUser();
        getLock();

        lnUser.setOnClickListener(this);
        lnHr.setOnClickListener(this);
        lnLock.setOnClickListener(this);

        lvLock.setOnItemClickListener(this);
        lvUser.setOnItemClickListener(this);
        lvHr.setOnItemClickListener(this);

        lvUser.setOnItemLongClickListener(this);
        lvHr.setOnItemLongClickListener(this);
    }

    private void getUser() {
        users.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, mainAdminActivity) + API.SEARCH_USER + "?permission=0"
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray data= response.getJSONObject("data").getJSONArray("users");
                        for (int i=0; i< data.length(); i++){
                            users.add(new Gson().fromJson(data.getJSONObject(i).toString(), User.class));
                        }
                        if (userAdapter != null)
                            userAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainAdminActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getHr() {
        hrs.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, mainAdminActivity) + API.SEARCH_USER + "?permission=1"
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray data= response.getJSONObject("data").getJSONArray("users");
                        for (int i=0; i< data.length(); i++){
                            hrs.add(new Gson().fromJson(data.getJSONObject(i).toString(), User.class));
                        }
                        if (hrAdapter != null)
                            hrAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainAdminActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getLock() {
        locks.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, mainAdminActivity) + API.GET_LOCK
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray data= response.getJSONObject("data").getJSONArray("users");
                        for (int i=0; i< data.length(); i++){
                            locks.add(new Gson().fromJson(data.getJSONObject(i).toString(), User.class));
                        }
                        Toast.makeText(mainAdminActivity, locks.size() + "", Toast.LENGTH_SHORT).show();
                        if (lockAdapter != null){
                            lockAdapter.notifyDataSetChanged();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainAdminActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lnHr:
                if (isShowHr){
                    isShowHr = false;
                    imShowHr.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                    lvHr.setVisibility(View.GONE);
                }else {
                    isShowHr = true;
                    imShowHr.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    lvHr.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.lnUser:
                if (isShowUser){
                    isShowUser = false;
                    imShowUser.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                    lvUser.setVisibility(View.GONE);
                }else {
                    isShowUser = true;
                    imShowUser.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    lvUser.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.lnLock:
                if (isShowLock){
                    isShowLock = false;
                    imShowLock.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                    lvLock.setVisibility(View.GONE);
                }else {
                    isShowLock = true;
                    imShowLock.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    lvLock.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lvHr:
                mainAdminActivity.switchActivity(InfoUserActivity.class, hrs.get(position));
                break;
            case R.id.lvUser:
                mainAdminActivity.switchActivity(InfoUserActivity.class, users.get(position));
                break;
            case R.id.lvLock:
                mainAdminActivity.switchActivity(InfoUserActivity.class, locks.get(position));
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()){
            case R.id.lvHr:
                final AlertDialog.Builder builder = new AlertDialog.Builder(mainAdminActivity);
                builder.setMessage("Bạn muốn khóa tài khoản này?")
                        .setCancelable(false)
                        .setPositiveButton("Khóa", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                User u = hrs.get(position);
                                u.setLock(1);
                                updateRequest(u);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.lvUser:
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(mainAdminActivity);
                builder1.setMessage("Bạn muốn khóa tài khoản này?")
                        .setCancelable(false)
                        .setPositiveButton("Khóa", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                User u1 = users.get(position);
                                u1.setLock(1);
                                updateRequest(u1);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert1 = builder1.create();
                alert1.show();

                break;
        }
        return true;
    }

    private void updateRequest(User currentUser){
        String json = new Gson().toJson(currentUser);
        JSONObject requestBody = null;
        try {
            requestBody = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Preferences.getData(Key.IP, mainAdminActivity) + API.UPDATE_USER, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("user").toString();
                        if (data.equals("null")){

                        }else {
                            Toast.makeText(mainAdminActivity, getString(R.string.lock_success), Toast.LENGTH_SHORT).show();
                            getHr();
                            getUser();
                            getLock();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainAdminActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
