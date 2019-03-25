package com.example.wayup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Company;
import com.example.wayup.model.Job;
import com.example.wayup.util.Direction;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.ProcessDialog;
import com.example.wayup.util.VolleySingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker myMarker;
    private Geocoder geocoder;
    private LatLng currentLocation;
    private int currentRadius = 1000;
    private ImageButton btnSearch, btnRadar, btnShowAll, btnLine, btnMapCanMove;
    private EditText edtSearch;
    private Dialog dialog;
    private RequestQueue requestQueue;
    private ProcessDialog processDialog;
    private boolean isMove = true;
    private List<Job> arrJobs = new ArrayList<>();
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        findId();
        initViews();
    }

    private void findId() {
        edtSearch = findViewById(R.id.edtSearchMap);
        btnSearch = findViewById(R.id.btnMapSearch);
        btnRadar = findViewById(R.id.btnMapRadar);
        btnShowAll = findViewById(R.id.btnMapShowAll);
        btnLine = findViewById(R.id.btnMapLine);
        btnMapCanMove = findViewById(R.id.btnMapCanMode);
    }

    private void initViews() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        processDialog = new ProcessDialog(this);
        edtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    new GetLatLng().execute(v.getText().toString());
                    return true;
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(this);
        btnRadar.setOnClickListener(this);
        btnShowAll.setOnClickListener(this);
        btnLine.setOnClickListener(this);
        btnMapCanMove.setOnClickListener(this);
    }

    private void initMap() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        LatLng begin;
        try {
            begin = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }catch (Exception e){
            begin = new LatLng(21.0544494, 105.735142);
        }
        currentLocation = begin;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(begin));
        CameraPosition position = new CameraPosition(begin, 14, 0, 0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        geocoder = new Geocoder(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (isMove){
                    currentLocation = mMap.getCameraPosition().target;
                    drawCircle(currentLocation, currentRadius);
                }
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

            }
        });


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String title = marker.getTitle();
        Job job = null;
        for (Job j : arrJobs){
            if (title.equalsIgnoreCase(j.getTitle())){
                job = j;
                break;
            }
        }

        if (job != null){
            Intent intent = new Intent(this, JobInfoActivity.class);
            intent.putExtra(MainActivity.KEY_PASS_DATA, job);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String[] permissionList = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : permissionList) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissionList, 0);
                    return;
                }
            }
        }
        initMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                finish();
                return;
            }
        }
        initMap();
    }

    private Marker drawMarker(LatLng position, String title, String snippet) {
        Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_job);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        MarkerOptions options = new MarkerOptions();
        options.position(position);
        options.title(title);
        options.snippet(snippet);
        options.icon(markerIcon);

        return mMap.addMarker(options);
    }

    @SuppressLint("NewApi")
    private void drawCircle(LatLng point, int radius){
        mMap.clear();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(point);
        circleOptions.radius(radius);
        circleOptions.strokeColor(this.getColor(R.color.stroke));
        circleOptions.fillColor(this.getColor(R.color.fill));
        circleOptions.strokeWidth(2);
        mMap.addCircle(circleOptions);

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // name to latlong
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMapSearch:
                initDialogSearch();
                break;
            case R.id.btnMapRadar:
                initDialogSetRadius();
                break;
            case R.id.btnMapShowAll:
                processDialog.show();
                getAll();
                break;
            case R.id.btnMapLine:
                if (myMarker == null){
                    Toast.makeText(this, getString(R.string.no_marker), Toast.LENGTH_SHORT).show();
                    return;
                }
//                processDialog.show();
//                new DrawLine().execute(currentLocation, myMarker.getPosition());
                double distance = distanceBetweenTwoPoint(currentLocation.latitude
                        , currentLocation.longitude, myMarker.getPosition().latitude, myMarker.getPosition().longitude);
                Toast.makeText(this, getString(R.string.distance) + distance + " m", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnMapCanMode:
                if (isMove){
                    isMove = false;
                    btnMapCanMove.setImageResource(R.drawable.ic_disable_move);
                }else {
                    isMove = true;
                    btnMapCanMove.setImageResource(R.drawable.ic_enable_move);
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        myMarker = marker;
        return false;
    }

    private class LatLongUtil extends AsyncTask<List<Job>, Void, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(List<Job>... lists) {
            arrJobs.clear();
            List<Job> jobs = lists[0];
            List<LatLng> latLngs = new ArrayList<>();
            arrJobs.addAll(jobs);
            for (Job job : jobs){
                latLngs.add(getLocationFromAddress(MapActivity.this, job.getAddress()));
            }
            return latLngs;
        }

        @Override
        protected void onPostExecute(List<LatLng> latLngs) {
            super.onPostExecute(latLngs);
            if (latLngs.size() == 0){
                Toast.makeText(MapActivity.this, getString(R.string.no_result), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
                return;
            }
            boolean check = false;
            for (int i=0; i< latLngs.size(); i++){
                double distance = distanceBetweenTwoPoint(latLngs.get(i).latitude,latLngs.get(i).longitude
                        , currentLocation.latitude, currentLocation.longitude);
                if (distance <= currentRadius){
                    check = true;
                    drawMarker(latLngs.get(i), arrJobs.get(i).getTitle(), arrJobs.get(i).getFast_info());
                }
            }
            if (!check){
                Toast.makeText(MapActivity.this, getString(R.string.no_result), Toast.LENGTH_SHORT).show();
            }
            if (processDialog.isShow()){
                processDialog.dismiss();
            }
        }
    }

    private class GetLatLng extends AsyncTask<String, Void, LatLng>{

        @Override
        protected LatLng doInBackground(String... strings) {
            return getLocationFromAddress(MapActivity.this, strings[0]);
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            currentLocation = latLng;
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraPosition position = new CameraPosition(latLng, 14, 0, 0);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        }
    }

    private class DrawLine extends AsyncTask<LatLng, Void, List<LatLng>>{

        @Override
        protected List<LatLng> doInBackground(LatLng... latLngs) {
            try{
                LatLng begin = latLngs[0];
                LatLng end = latLngs[1];
                Direction direction = new Direction();
                Document document = direction.getDocument(begin,end);
                ArrayList<LatLng> arr = direction.getDirection(document);
                return arr;
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<LatLng> latLngs) {
            super.onPostExecute(latLngs);
            if (latLngs == null){
                Toast.makeText(MapActivity.this, getString(R.string.errors), Toast.LENGTH_SHORT).show();
            }else {
                if (polyline != null){
                    polyline.remove();
                }
                drawDirection(latLngs);
            }
            if (processDialog.isShow()){
                processDialog.dismiss();
            }
        }
    }

    @SuppressLint("NewApi")
    private void initDialogSetRadius(){
        dialog = new Dialog(MapActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_set_radius);
        dialog.setCancelable(true);

        final EditText edtRadius = dialog.findViewById(R.id.edtRadiusMap);
        final Button btnConfirm = dialog.findViewById(R.id.btnSetRadiusMap);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radius = Integer.parseInt(edtRadius.getText().toString());
                if (edtRadius.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(btnConfirm, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                drawCircle(currentLocation, radius);
                currentRadius = radius;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("NewApi")
    private void initDialogSearch(){
        dialog = new Dialog(MapActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_map_search);
        dialog.setCancelable(true);

        final RadioButton radSkill = dialog.findViewById(R.id.radMapSkill);
        final RadioButton radLevel = dialog.findViewById(R.id.radMapLevel);
        RadioButton radTag = dialog.findViewById(R.id.radMapTag);
        final Spinner spinner = dialog.findViewById(R.id.spinnerMapSearch);
        Button btnSearch = dialog.findViewById(R.id.btnConfirmMapSearch);

        radSkill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_list_item_1, SearchActivity.skills);
                    spinner.setAdapter(adapter);
                }
            }
        });
        radLevel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_list_item_1, SearchActivity.levels);
                    spinner.setAdapter(adapter);
                }
            }
        });
        radTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_list_item_1, SearchActivity.titles);
                    spinner.setAdapter(adapter);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radSkill.isChecked()){
                    searchJob("skill", spinner.getSelectedItem().toString());
                }else{
                    searchJob("title", spinner.getSelectedItem().toString());
                }
                processDialog.show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public double distanceBetweenTwoPoint(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return distance * meterConversion;
    }

    private void searchJob(String condition, String param){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this)
                + API.SEARCH_WITH_CONDITION  + condition + "?" + condition + "=" + param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("jobs");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        List<Job> jobs = new ArrayList<>();
                        for (int i=0; i< jsonArray.length(); i++){
                            Job job = (Job) gson.fromJson(jsonArray.getJSONObject(i).toString(), Job.class);
                            jobs.add(job);
                        }

                        new LatLongUtil().execute(jobs);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getAll() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, this) + API.GET_ALL_JOB_WITHOUT_PAGE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        List<Job> jobs = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("jobs");
                        Gson gson = new Gson();
                        for (int i=0; i< jsonArray.length(); i++){
                            Job job = (Job) gson.fromJson(jsonArray.getJSONObject(i).toString(), Job.class);
                            jobs.add(job);
                        }
                        new LatLongUtil().execute(jobs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void drawDirection(List<LatLng> arr){
        PolylineOptions options = new PolylineOptions();
        options.width(8);
        options.color(Color.BLUE);
        options.addAll(arr);
        polyline = mMap.addPolyline(options);
    }

}
