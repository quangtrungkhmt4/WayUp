package com.example.wayup.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.ChartActivity;
import com.example.wayup.R;
import com.example.wayup.adapter.ChartPagerAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LanguageChartFragment extends Fragment {
    private RequestQueue requestQueue;
    private PieChart pieChart;
    private float[] yData = new float[7];
    private String[] xData = {"Java", "C#", "PHP", "Python", "Swift", "C++", "JavaScript"};
    private ChartActivity chartActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language_chart, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        pieChart = view.findViewById(R.id.chart_language);
    }

    private void initViews() {
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        chartActivity = (ChartActivity) getActivity();
        Description desc = new Description();
        desc.setText("Ngôn ngữ lập trình");
        pieChart.setDescription(desc);
        pieChart.setHoleRadius(45f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleAlpha(50);
        pieChart.setCenterText("Ngôn ngữ lập trình");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);

        getQuantum();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String[] strings = e.toString().split("y: ");
                float job = Float.parseFloat(strings[1]);
                int jobs = (int) job;
                int index = 0;
                for (int i=0; i< yData.length; i++){
                    if (job == yData[i]){
                        index = i;
                        break;
                    }
                }
                Toast.makeText(getContext(), xData[index] + ": " + jobs +" "+ getContext().getString(R.string.posts), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @SuppressLint("NewApi")
    private void addData() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i=0; i<yData.length; i++){
            yEntrys.add(new PieEntry(yData[i]));
        }

        for (int i=1; i<xData.length; i++){
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Language");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        // create color
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getContext().getColor(R.color.chart_one));
        colors.add(getContext().getColor(R.color.chart_two));
        colors.add(getContext().getColor(R.color.chart_three));
        colors.add(getContext().getColor(R.color.chart_four));
        colors.add(getContext().getColor(R.color.chart_five));
        colors.add(getContext().getColor(R.color.chart_six));
        colors.add(getContext().getColor(R.color.chart_seven));

        pieDataSet.setColors(colors);

        //add legend
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setEnabled(true);

        List<LegendEntry> entries = new ArrayList<>();

        for (int i = 0; i < xData.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors.get(i);
            entry.label = xData[i];
            entries.add(entry);
        }

        legend.setCustom(entries);

        //create data
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void getQuantum(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, chartActivity) + API.GET_QUANTUM_LANGUAGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("quantum");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        for (int i=0; i< jsonArray.length(); i++){
                            yData[i] = Float.parseFloat(jsonArray.get(i).toString());
                        }
                        addData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
