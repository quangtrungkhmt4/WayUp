package com.example.wayupmanagement.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayupmanagement.InfoApplyActivity;
import com.example.wayupmanagement.MainHrActivity;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.adapter.ApplyAdapter;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.ApplyProfile;
import com.example.wayupmanagement.model.Hr;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.model.Vote;
import com.example.wayupmanagement.util.DateTime;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApplyFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    private ListView lvApply;
    private ApplyAdapter applyAdapter;
    private List<ApplyProfile> applyProfiles = new ArrayList<>();
    private MainHrActivity mainHrActivity;
    private RequestQueue requestQueue;
    private Hr currentHr;
    private User currentUser;
    private ProcessDialog processDialog;
    private Dialog dialog;
    private FloatingActionButton btnRefesh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        lvApply = view.findViewById(R.id.lvApply);
        TextView tvEmpty = view.findViewById(R.id.tvEmpty);
        lvApply.setEmptyView(tvEmpty);
        btnRefesh = view.findViewById(R.id.btnRefesh);
    }

    private void initViews() {
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        mainHrActivity = (MainHrActivity) getActivity();
        applyAdapter = new ApplyAdapter(mainHrActivity, R.layout.item_apply, applyProfiles);
        processDialog = new ProcessDialog(getContext());
        lvApply.setAdapter(applyAdapter);
        lvApply.setOnItemClickListener(this);
        lvApply.setOnItemLongClickListener(this);
        btnRefesh.setOnClickListener(this);
    }

    private void searchHr(int id_user) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, mainHrActivity) + API.SEARCH_HR + id_user
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("hr").toString();
                        if (data.equals("null")){
                            Toast.makeText(mainHrActivity, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                        }else {
                            currentHr = new Gson().fromJson(data, Hr.class);
                            loadApply();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainHrActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void loadApply() {
        applyProfiles.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainHrActivity)
                + API.SEARCH_APPLY_VER2 + mainHrActivity.getCurrentHr().getCompany().getId_company(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("applyProfiles");
                        Gson gson = new Gson();
                        for (int i=0; i< jsonArray.length(); i++){
                            ApplyProfile applyProfile = (ApplyProfile) gson.fromJson(jsonArray.getJSONObject(i).toString(), ApplyProfile.class);
                            applyProfiles.add(applyProfile);
                        }
                        if (applyAdapter != null){
                            applyAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainHrActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, mainHrActivity), User.class);
        searchHr(currentUser.getId_user());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mainHrActivity.switchActivity(InfoApplyActivity.class, applyProfiles.get(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (applyProfiles.get(position).getStatus() == 0){
            final AlertDialog.Builder builder = new AlertDialog.Builder(mainHrActivity);
            builder.setMessage("Bạn muốn đóng ứng viên này?")
                    .setCancelable(false)
                    .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            ApplyProfile ap = applyProfiles.get(position);
                            ap.setStatus(2);
                            updateApply(ap);
                            processDialog.show();
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
        }
        return true;
    }

    private void updateApply(final ApplyProfile applyProfile) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(applyProfile));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT
                , Preferences.getData(Key.IP, mainHrActivity) + API.POST_APPLY
                , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("applyProfile").toString();
                        if (data.equals("null")){
                            Toast.makeText(mainHrActivity, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            currentUser = new Gson().fromJson(Preferences.getData(Key.USER, mainHrActivity), User.class);
                            searchHr(currentUser.getId_user());
                            Toast.makeText(mainHrActivity, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                            initDialogVote(applyProfile);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainHrActivity, error.toString(), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void postVote(Vote vote) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(vote));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                , Preferences.getData(Key.IP, mainHrActivity) + API.POST_VOTE
                , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("vote").toString();
                        if (data.equals("null")){
                            Toast.makeText(mainHrActivity, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            Toast.makeText(mainHrActivity, getString(R.string.vote_success), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainHrActivity, error.toString(), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("NewApi")
    private void initDialogVote(final ApplyProfile applyProfile){
        dialog = new Dialog(mainHrActivity,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_rate_apply);
        dialog.setCancelable(false);

        final EditText edtVote = dialog.findViewById(R.id.edtVotePoint);
        final Button btnConfirm = dialog.findViewById(R.id.btnVote);
        final Button btnSkip = dialog.findViewById(R.id.btnSkip);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vote = Integer.parseInt(edtVote.getText().toString());
                if (edtVote.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(btnConfirm, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                Vote vot = new Vote(DateTime.random(), vote, applyProfile.getUser(), currentHr.getCompany());
                postVote(vot);

                dialog.dismiss();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        loadApply();
    }
}
