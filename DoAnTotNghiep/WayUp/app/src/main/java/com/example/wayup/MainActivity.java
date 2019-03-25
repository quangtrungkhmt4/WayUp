package com.example.wayup;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wayup.adapter.ItemDrawerAdapter;
import com.example.wayup.adapter.LVAppliedAdapter;
import com.example.wayup.adapter.LVProfileAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.fragment.CompanyFragment;
import com.example.wayup.fragment.ForumFragment;
import com.example.wayup.fragment.JobFragment;
import com.example.wayup.fragment.ProfileFragment;
import com.example.wayup.model.AbstractModel;
import com.example.wayup.model.ApplyProfile;
import com.example.wayup.model.ItemDrawer;
import com.example.wayup.model.Notification;
import com.example.wayup.model.Profile;
import com.example.wayup.model.User;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;
import com.example.wayup.view.BottomNavigationBehavior;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private User user;
    private List<ItemDrawer> itemDrawers = new ArrayList<>();
    private ItemDrawerAdapter itemDrawerAdapter;
    private CircleImageView imAvatarDrawer;
    private TextView tvNameDrawer;
    private ListView lvItemDrawer;
    private Dialog dialog;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findID();
        initViews();
    }

    private void findID() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbarMain);
        imAvatarDrawer = findViewById(R.id.imAvatarDrawerLayout);
        tvNameDrawer = findViewById(R.id.tvNameDrawerLayout);
        lvItemDrawer = findViewById(R.id.lvItemDrawerlayout);
    }

    @SuppressLint("CheckResult")
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

        toggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                loadDataDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        itemDrawerAdapter = new ItemDrawerAdapter(this, R.layout.item_drawer, itemDrawers);
        lvItemDrawer.setAdapter(itemDrawerAdapter);
        lvItemDrawer.setOnItemClickListener(this);
        loadItemDrawer();

        bottomNavigationView.inflateMenu(R.menu.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        user = (User) new Gson().fromJson(Preferences.getData(Key.USER, this), User.class);
        if (user.getGender().equals("null")){
            loadFragment(new ProfileFragment());
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            toolbar.setTitle(getString(R.string.information));
        }else {
            loadFragment(new CompanyFragment());
            toolbar.setTitle(getString(R.string.list_company));
        }
        loadDataDrawer();
    }

    private void loadDataDrawer(){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        user = (User) new Gson().fromJson(Preferences.getData(Key.USER, this), User.class);
        String img = user.getImage().contains("http")?user.getImage(): Preferences.getData(Key.DOMAIN, this) + user.getImage();
        Glide.with(this).load(img).apply(requestOptions).into(imAvatarDrawer);
        tvNameDrawer.setText(user.getName());
    }

    @SuppressLint("NewApi")
    private void loadItemDrawer() {
        itemDrawers.add(new ItemDrawer(0, getString(R.string.search), R.drawable.ic_search));
        itemDrawers.add(new ItemDrawer(1, getString(R.string.map_drawer), R.drawable.ic_map));
        itemDrawers.add(new ItemDrawer(2, getString(R.string.chart), R.drawable.ic_chart_v2));
        itemDrawers.add(new ItemDrawer(3, getString(R.string.favorite), R.drawable.ic_favorite));
        itemDrawers.add(new ItemDrawer(4, getString(R.string.notification), R.drawable.ic_notifications));
        itemDrawers.add(new ItemDrawer(5, getString(R.string.list_profile), R.drawable.ic_history));
        itemDrawers.add(new ItemDrawer(6, getString(R.string.applied), R.drawable.ic_list_apply));
        itemDrawers.add(new ItemDrawer(7, getString(R.string.register_post), R.drawable.ic_register_post));
        itemDrawers.add(new ItemDrawer(8, getString(R.string.about), R.drawable.ic_info_v2));
        itemDrawers.add(new ItemDrawer(9, getString(R.string.logout), R.drawable.ic_logout));
        itemDrawerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void addBadgeView() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);
        itemView.addView(notificationBadge);
    }

    private void refeshBadgeView() {
        boolean badgeIsVisiable = notificationBadge.getVisibility() != View.VISIBLE;
        notificationBadge.setVisibility(badgeIsVisiable ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.activity_open_enter,
                R.anim.activity_open_exit,
                R.anim.activity_close_enter,
                R.anim.activity_close_exit);
        transaction.replace(R.id.frame_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.navigation_company:
                toolbar.setTitle(getString(R.string.list_company));
                getSupportFragmentManager().popBackStack();
                fragment = new CompanyFragment();
                loadFragment(fragment);
                break;
            case R.id.navigation_job:
                toolbar.setTitle(getString(R.string.list_job));
                getSupportFragmentManager().popBackStack();
                fragment = new JobFragment();
                loadFragment(fragment);
                break;
            case R.id.navigation_forum:
                toolbar.setTitle(getString(R.string.forums));
                getSupportFragmentManager().popBackStack();
                fragment = new ForumFragment();
                loadFragment(fragment);
                break;
            case R.id.navigation_profile:
                toolbar.setTitle(getString(R.string.information));
                getSupportFragmentManager().popBackStack();
                fragment = new ProfileFragment();
                loadFragment(fragment);
                break;
        }
        return true;
    }

    public static final String KEY_PASS_DATA = "KEY_PASS_DATA";
    public<E extends AbstractModel> void switchActivity(Class<?> target, E object){
        Intent intent = new Intent(this, target);
        intent.putExtra(KEY_PASS_DATA, object);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0){
            initDialogOptionSearch();
        }else if (position == 1){
            startActivity(new Intent(MainActivity.this, MapActivity.class));
        }else if (position == 2){
            startActivity(new Intent(MainActivity.this, ChartActivity.class));
        }else if (position == 3){
            startActivity(new Intent(MainActivity.this, SaveActivity.class));
        }else if (position == 4){
            initDialogNotification();
        }else if (position == 5){
            initDialogProfile();
        }else if (position == 6){
            initDialogApplied();
        }else if (position == 7){
            startActivity(new Intent(this, RegisterCompanyActivity.class));
        }else if (position == 8){
            initDialogAbout();
        }else if (position == 9){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn muốn đăng xuất khỏi hệ thống?")
                    .setCancelable(false)
                    .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            Preferences.saveData(Key.USER, "", MainActivity.this);
                            startActivity(new Intent(MainActivity.this, LoginRegisterActivity.class));
                            finish();
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

        drawerLayout.closeDrawers();
    }

    @SuppressLint("NewApi")
    private void initDialogAbout(){
        dialog = new Dialog(MainActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);
        dialog.show();
    }

    @SuppressLint("NewApi")
    private void initDialogNotification(){
        dialog = new Dialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_notification);
        dialog.setCancelable(true);

        final Spinner spinner = dialog.findViewById(R.id.spinnerNoti);
        final EditText edtMail = dialog.findViewById(R.id.edtEmailNoti);
        final Button btnConfirm = dialog.findViewById(R.id.btnRegisterNoti);
        Button btnRemove = dialog.findViewById(R.id.btnDeleteNoti);

        String[] skills = new String[]{"Agile","Android","Angular","AngularJS","ASP.NET","AWS"
                ,"Blockchain","Bridge Engineer","Business Analyst","C#","C++","C language","Cloud","CSS"
                ,"Database","Designer","DevOps","Django","Drupal","Embedded","English","ERP","Games"
                ,"Golang","HTML5","Hybrid","iOS","IT Support","J2EE","Japanese","Java","JavaScript"
                ,"JQuery","JSON","Laravel","Linux","Magento","Manager","Mobile Apps","MVC","MySQL"
                ,".NET","Networking","NodeJS","NoSQL","Objective C","OOP","Oracle","PHP","PostgreSql"
                ,"Product Manager","Project Manager","Python","QA QC","ReactJS","React Native","Ruby"
                ,"Ruby on Rails","SAP","Scala","Sharepoint","Software Architect","Spring","SQL","Swift"
                ,"System Admin","System Engineer","Team Leader","Tester","UI-UX","Unity","VueJS"
                ,"Wordpress","Xamarin","Full Stack","Front End","Back End","QA","QC"};

        final ArrayAdapter<String> itemSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, skills);
        spinner.setAdapter(itemSpinnerAdapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtMail.getText().toString();
                if (email.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(btnConfirm, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                String skill = spinner.getSelectedItem().toString();
                sendNoti(email, skill);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtMail.getText().toString();
                if (email.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(btnConfirm, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                deleteNoti(email);

            }
        });

        dialog.show();
    }

    List<ApplyProfile> applyProfiles = new ArrayList<>();
    LVAppliedAdapter adapterApplied = null;
    @SuppressLint("NewApi")
    private void initDialogApplied(){
        dialog = new Dialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_applied);
        dialog.setCancelable(true);

        ListView listView = dialog.findViewById(R.id.lvApplied);
        TextView tvEmpty = dialog.findViewById(R.id.tvEmpty);
        listView.setEmptyView(tvEmpty);

        adapterApplied = new LVAppliedAdapter(this, R.layout.item_applied, applyProfiles);
        listView.setAdapter(adapterApplied);
        getListApply();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (applyProfiles.get(position).getStatus() == 0){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Bạn muốn hủy tin ứng tuyển này?")
                            .setCancelable(false)
                            .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    ApplyProfile ap = applyProfiles.get(position);
                                    ap.setStatus(1);
                                    updateApply(ap);
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
        });

        dialog.show();
    }

    private void updateApply(ApplyProfile applyProfile){
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(applyProfile));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Preferences.getData(Key.IP, this)
                + API.POST_APPLY , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        Toast.makeText(MainActivity.this, getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();
                        getListApply();
                    }else {
                        Toast.makeText(MainActivity.this, getString(R.string.cancel_fail), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getListApply(){
        applyProfiles.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this)
                + API.GET_APPLY_WITH_USER + user.getId_user(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("applyProfiles");
                        for (int i=0; i<jsonArray.length(); i++){
                            applyProfiles.add(new Gson().fromJson(jsonArray.getJSONObject(i).toString(), ApplyProfile.class));
                        }
                        if (adapterApplied != null){
                            adapterApplied.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    List<Profile> profiles = new ArrayList<>();
    LVProfileAdapter adapterPro = null;
    @SuppressLint("NewApi")
    private void initDialogProfile(){
        dialog = new Dialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_list_profile);
        dialog.setCancelable(true);

        ListView listView = dialog.findViewById(R.id.lvProfiles);
        TextView tvEmpty = dialog.findViewById(R.id.tvEmpty);
        listView.setEmptyView(tvEmpty);

        adapterPro = new LVProfileAdapter(this, R.layout.item_profile, profiles);
        listView.setAdapter(adapterPro);
        getProfile(user);

        dialog.show();
    }

    private void getProfile(final User user){
        profiles.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this)
                + API.GET_ALL_PROFILE + user.getId_user(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray data= response.getJSONObject("data").getJSONArray("profiles");
                        if (data.length() == 0){
                        }else {
                            for (int i=0; i< data.length(); i++){
                                profiles.add(new Gson().fromJson(data.getJSONObject(i).toString(), Profile.class));
                            }
                            if (adapterPro != null){
                                adapterPro.notifyDataSetChanged();
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
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void deleteNoti(final String email){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE
                , Preferences.getData(Key.IP, this) + API.DELETE_NOTI + "?email=" + email, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("response").toString();
                        if (data.equalsIgnoreCase("true")){
                            Toast.makeText(MainActivity.this, getString(R.string.remove_notification_success), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this, getString(R.string.remove_notification_fail), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void sendNoti(final String email, final String skill){
        Notification notification = new Notification(skill, email);
        String json = new Gson().toJson(notification);
        JSONObject param = null;
        try {
            param = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Preferences.getData(Key.IP, this) + API.POST_NOTI, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("response").toString();
                        if (data.equalsIgnoreCase("true")){
                            Toast.makeText(MainActivity.this, getString(R.string.register_notification_success), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this, getString(R.string.register_notification_fail), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void initDialogOptionSearch(){
        dialog = new Dialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_option_search);
        dialog.setCancelable(true);

        Button btnProvince = dialog.findViewById(R.id.btnSearchWithProvince);
        Button btnLanguage = dialog.findViewById(R.id.btnSearchWithLanguage);
        Button btnLevel = dialog.findViewById(R.id.btnSearchWithLevel);
        Button btnTag = dialog.findViewById(R.id.btnSearchWithTag);

        final Intent intent = new Intent(MainActivity.this, SearchActivity.class);

        btnProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Key.PASS_OPTION, Key.OPTION_CITY);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Key.PASS_OPTION, Key.OPTION_LANGUAGE);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btnLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Key.PASS_OPTION, Key.OPTION_LEVEL);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Key.PASS_OPTION, Key.OPTION_TAG);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
