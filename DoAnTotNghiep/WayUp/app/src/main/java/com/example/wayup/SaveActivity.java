package com.example.wayup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wayup.adapter.LVJobAdapter;
import com.example.wayup.model.Job;
import com.example.wayup.util.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SaveActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private Toolbar toolbar;
    private ListView listView;
    private Database database;
    private List<Job> jobs = new ArrayList<>();
    private LVJobAdapter lvJobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        findId();
        initViews();
        loadData();
    }

    private void loadData() {
        jobs.addAll(database.getJobs());
        lvJobAdapter.notifyDataSetChanged();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarSave);
        listView = findViewById(R.id.lvSave);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }
        toolbar.setTitleTextColor(0xFFFFFFFF);
        database = new Database(this);

        lvJobAdapter = new LVJobAdapter(this, R.layout.item_job, jobs);
        listView.setAdapter(lvJobAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, JobInfoActivity.class);
        intent.putExtra(MainActivity.KEY_PASS_DATA, jobs.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn xóa tin này khỏi danh sách?")
                .setCancelable(false)
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        database.deleteJob(jobs.get(position).getId_job());
                        jobs.clear();
                        jobs.addAll(database.getJobs());
                        lvJobAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
}
