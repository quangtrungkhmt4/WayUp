package com.example.wayup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.adapter.JobAdapter;
import com.example.wayup.adapter.LVJobAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Job;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    public static String[] skills = new String[]{"Skill", "Agile","Android","Angular","AngularJS","ASP.NET","AWS"
            ,"Blockchain","Bridge Engineer","Business Analyst","C#","C++","C language","Cloud","CSS"
            ,"Database","Designer","DevOps","Django","Drupal","Embedded","English","ERP","Games"
            ,"Golang","HTML5","Hybrid","iOS","IT Support","J2EE","Japanese","Java","JavaScript"
            ,"JQuery","JSON","Laravel","Linux","Magento","Manager","Mobile Apps","MVC","MySQL"
            ,".NET","Networking","NodeJS","NoSQL","Objective C","OOP","Oracle","PHP","PostgreSql"
            ,"Product Manager","Project Manager","Python","QA QC","ReactJS","React Native","Ruby"
            ,"Ruby on Rails","SAP","Scala","Sharepoint","Software Architect","Spring","SQL","Swift"
            ,"System Admin","System Engineer","Team Leader","Tester","UI-UX","Unity","VueJS"
            ,"Wordpress","Xamarin","Full Stack","Front End","Back End","QA","QC"};
    public static String[] titles = new String[]{"Tag", ".NET Developer",".NET Web Developer"
            ,"Android App Developer","Android Developer","AngularJS Developer"
            ,"AngularJS Web Developer","ASP.NET Developer","ASP.NET Web Developer"
            ,"Back End Developer","Back End Web Developer","Bridge Engineer","Business Analyst"
            ,"C Language Developer","C# Developer","C# Web Developer","C++ Developer","CSS Developer"
            ,"CSS Web Developer","Database Administrator","Django Developer","Django Web Developer"
            ,"Drupal Developer","Drupal Web Developer","Embedded Developer","Embedded Engineer"
            ,"ERP Developer","ERP Specialist","Front End Developer","Front End Web Developer"
            ,"Full Stack Developer","Full Stack Web Developer","Games Developer","Graphic Designer"
            ,"HTML5 Developer","iOS App Developer","iOS Developer","IT Administrator","J2EE Developer"
            ,"Java Developer","Java Web Developer","Javascript Developer","Javascript Web Developer"
            ,"jQuery Developer","JSON Developer","Linux Developer","Linux System Administrator"
            ,"Magento Developer","Magento Web Developer","Mobile Apps Developer","MVC Developer"
            ,"MySQL Developer","NodeJS Developer","Objective C App Developer","Objective C Developer"
            ,"OOP Developer","Oracle Database Administrator","Oracle DBA","Oracle Developer"
            ,"PHP Developer","PHP Web Developer","Postgresql Database Administrator","Postgresql Developer"
            ,"Product Manager","Product Owner","Project Manager","Python Developer","Python Web Developer"
            ,"QA QC Manager","Quality Control Manager","Quality Control Tester","ReactJS Developer"
            ,"Ruby Developer","Ruby On Rails Developer","Ruby On Rails Web Developer","Sales Engineer"
            ,"SAP Consultant","Senior .NET Developer","Senior Android App Developer","Senior Android Developer"
            ,"Senior AngularJS Developer","Senior ASP.NET Developer","Senior Back End Developer"
            ,"Senior Bridge Engineer","Senior Business Analyst","Senior C Language Developer"
            ,"Senior C# Developer","Senior C++ Developer","Senior CSS Developer","Senior Database Administrator"
            ,"Senior Designer","Senior Django Developer","Senior Drupal Developer","Senior Embedded Developer"
            ,"Senior ERP Developer","Senior Front End Developer","Senior Full Stack Developer"
            ,"Senior Full Stack Web Developer","Senior Games Developer","Senior HTML5 Developer"
            ,"Senior iOS App Developer","Senior iOS Developer","Senior J2EE Developer","Senior Java Developer"
            ,"Senior Javascript Developer","Senior jQuery Developer","Senior JSON Developer"
            ,"Senior Linux Developer","Senior Magento Developer","Senior Mobile Apps Developer"
            ,"Senior MVC Developer","Senior MySQL Developer","Senior Nodejs Developer"
            ,"Senior Objective C App Developer","Senior Objective C Developer","Senior OOP Developer"
            ,"Senior Oracle Developer","Senior PHP Developer","Senior Postgresql Database Administrator"
            ,"Senior Postgresql Developer","Senior Product Manager","Senior Product Owner"
            ,"Senior Project Manager","Senior Python Developer","Senior QA QC","Senior Quality Control Tester"
            ,"Senior ReactJS Developer","Senior Ruby Developer","Senior Ruby On Rails Developer"
            ,"Senior Sales Engineer","Senior Sharepoint Developer","Senior Software Architect"
            ,"Senior SQL Administrator","Senior SQL Developer","Senior System Admin","Senior System Engineer"
            ,"Senior Tester","Senior Unity Developer","Senior UX UI Designer","Senior Windows Phone Developer"
            ,"Senior Wordpress Developer","Senior XML Developer","Sharepoint Developer","Software Architect"
            ,"Software Developer","SQL Database Administrator","SQL Developer","System Admin"
            ,"System Administrator","System Engineer","Team Leader","Tester","Unity Developer"
            ,"Unity Game Developer","UX UI Designer","Web Designer","Web Developer"
            ,"Windows Phone Developer","Wordpress Developer","Wordpress Web Developer","XML Developer"};
    String[] cities = new String[]{"City", "Ho Chi Minh", "Ha Noi", "Da Nang", "Others"};
    public static String[] levels = new String[]{"Level", "Senior", "Junior", "Fresher", "Intent"};

    private Spinner spinner;
    private ListView lvJob;
    private List<Job> jobs = new ArrayList<>();
    private Toolbar toolbar;
    private LinearLayout lnCity;
    private int option;
    private ArrayAdapter<String> itemSpinnerAdapter;
    private RequestQueue requestQueue;
    private LVJobAdapter jobAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private boolean mSpinnerInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findId();
        initViews();
        getData();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarSearch);
        spinner = findViewById(R.id.spinner);
        lnCity = findViewById(R.id.lnSearchProvince);
        lvJob = findViewById(R.id.lvJobSearch);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayoutSearch);
    }

    @SuppressLint("NewApi")
    private void initViews() {
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }
        toolbar.setTitleTextColor(0xFFFFFFFF);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();

        jobAdapter = new LVJobAdapter(this, R.layout.item_job, jobs);
        lvJob.setAdapter(jobAdapter);
        TextView tvEmpty = findViewById(R.id.tvEmpty);
        lvJob.setEmptyView(tvEmpty);
        lvJob.setOnItemClickListener(this);

        spinner.setOnItemSelectedListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        option = intent.getIntExtra(Key.PASS_OPTION, -1);
        if (option == Key.OPTION_CITY){
            getSupportActionBar().setTitle(getString(R.string.search_province));
            itemSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
            spinner.setAdapter(itemSpinnerAdapter);
        }else if (option == Key.OPTION_LANGUAGE){
            getSupportActionBar().setTitle(getString(R.string.search_language));
            itemSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, skills);
            spinner.setAdapter(itemSpinnerAdapter);
        }else if (option == Key.OPTION_LEVEL){
            getSupportActionBar().setTitle(getString(R.string.search_level));
            itemSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels);
            spinner.setAdapter(itemSpinnerAdapter);
        }else{
            getSupportActionBar().setTitle(getString(R.string.search_tag));
            itemSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
            spinner.setAdapter(itemSpinnerAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMoreData(String condition, String param){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this)
                + API.SEARCH_WITH_CONDITION  + condition + "?" + condition + "=" + param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("jobs");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        for (int i=0; i< jsonArray.length(); i++){
                            Job job = (Job) gson.fromJson(jsonArray.getJSONObject(i).toString(), Job.class);
                            jobs.add(job);
                        }
                        if (shimmerFrameLayout.isShimmerStarted()){
                            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                }
                            }.start();

                        }
                        jobAdapter.notifyDataSetChanged();
                        spinner.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!mSpinnerInitialized) {
            mSpinnerInitialized = true;
            return;
        }
        if (position != 0){
            jobs.clear();
            String condition = "";
            if (itemSpinnerAdapter.getItem(0).equalsIgnoreCase(skills[0])){
                condition = "skill";
            }else if (itemSpinnerAdapter.getItem(0).equalsIgnoreCase(titles[0])){
                condition = "title";
            }else if (itemSpinnerAdapter.getItem(0).equalsIgnoreCase(levels[0])){
                condition = "title";
            }else {
                condition = "address";
            }
            loadMoreData(condition, itemSpinnerAdapter.getItem(position));
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
//            spinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, JobInfoActivity.class);
        intent.putExtra(MainActivity.KEY_PASS_DATA, jobs.get(position));
        startActivity(intent);
    }
}
