package com.example.wayup.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.wayup.model.Company;
import com.example.wayup.model.Job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String PATH = Environment.getDataDirectory().getPath()
            + "/data/com.example.wayup/databases/";

    private static final String DB_NAME = "wayup.sqlite";
    private static final String TABLE = "job";

    private static final String ID_JOB = "id_job";
    private static final String TITLE = "title";
    private static final String THUMBNAIL = "thumbnail";
    private static final String INFORMATION = "information";
    private static final String ADDRESS = "address";
    private static final String JOIN_DATE = "join_date";
    private static final String ESTIMATETIME = "estimatetime";
    private static final String LOCK = "lock";
    private static final String SALARY = "salary";
    private static final String SKILLS = "skills";
    private static final String FAST_INFO = "fast_info";
    private static final String ID_COMPANY = "id_company";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String MEMBER = "member";
    private static final String COUNTRY = "country";
    private static final String THUMBNAIL_COMPANY = "thumbnail_company";
    private static final String IMAGE = "image";
    private static final String TIME_WORK = "time_for_work";
    private static final String ADDRESS_COMPANY = "address_company";
    private static final String CONTACT = "contact";
    private static final String DESC = "desc";
    private static final String OT = "ot";
    private static final String TITLE_COMPANY = "title_company";

    private Context context;
    private SQLiteDatabase database;

    public Database(Context context) {
        this.context = context;
        copyFileToDevice();
    }

    private void copyFileToDevice() {
        File file = new File(PATH + DB_NAME);
        if (!file.exists()) {
            File parent = file.getParentFile();
            parent.mkdirs();
            try {
                InputStream inputStream = context.getAssets().open(DB_NAME);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int count = inputStream.read(b);
                while (count != -1) {
                    outputStream.write(b, 0, count);
                    count = inputStream.read(b);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openDatabase() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }

    private void closeDatabase() {
        database.close();
    }

    public List<Job> getJobs() {
        List<Job> arr = new ArrayList<>();
        openDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();

        int indexID_JOB = cursor.getColumnIndex(ID_JOB);
        int indexTITLE = cursor.getColumnIndex(TITLE);
        int indexTHUMBNAIL = cursor.getColumnIndex(THUMBNAIL);
        int indexINFO = cursor.getColumnIndex(INFORMATION);
        int indexADDRESS = cursor.getColumnIndex(ADDRESS);
        int indexJOIN_DATE = cursor.getColumnIndex(JOIN_DATE);
        int indexESTIMATETIME = cursor.getColumnIndex(ESTIMATETIME);
        int indexLOCK = cursor.getColumnIndex(LOCK);
        int indexSALARY = cursor.getColumnIndex(SALARY);
        int indexSKILLS = cursor.getColumnIndex(SKILLS);
        int indexFAST_INFO = cursor.getColumnIndex(FAST_INFO);
        int indexID_COMPANY = cursor.getColumnIndex(ID_COMPANY);
        int indexNAME = cursor.getColumnIndex(NAME);
        int indexTYPE = cursor.getColumnIndex(TYPE);
        int indexMEMBER = cursor.getColumnIndex(MEMBER);
        int indexCOUNTRY = cursor.getColumnIndex(COUNTRY);
        int indexTHUMB_COMPANY = cursor.getColumnIndex(THUMBNAIL_COMPANY);
        int indexIMAGE = cursor.getColumnIndex(IMAGE);
        int indexTIME_WORK = cursor.getColumnIndex(TIME_WORK);
        int indexADDRESS_COMPANY = cursor.getColumnIndex(ADDRESS_COMPANY);
        int indexCONTACT = cursor.getColumnIndex(CONTACT);
        int indexDESC = cursor.getColumnIndex(DESC);
        int indexOT = cursor.getColumnIndex(OT);
        int indexTITLE_COMPANY = cursor.getColumnIndex(TITLE_COMPANY);

        while (!cursor.isAfterLast()) {
            int id_job = cursor.getInt(indexID_JOB);
            String title = cursor.getString(indexTITLE);
            String thumbnail = cursor.getString(indexTHUMBNAIL);
            String info = cursor.getString(indexINFO);
            String address = cursor.getString(indexADDRESS);
            String join_date = cursor.getString(indexJOIN_DATE);
            int estimatetime = cursor.getInt(indexESTIMATETIME);
            int lock = cursor.getInt(indexLOCK);
            int salary = cursor.getInt(indexSALARY);
            String skills = cursor.getString(indexSKILLS);
            String fast_info = cursor.getString(indexFAST_INFO);
            int id_company = cursor.getInt(indexID_COMPANY);
            String name = cursor.getString(indexNAME);
            String type = cursor.getString(indexTYPE);
            String member = cursor.getString(indexMEMBER);
            String country = cursor.getString(indexCOUNTRY);
            String thumb_company = cursor.getString(indexTHUMB_COMPANY);
            String image = cursor.getString(indexIMAGE);
            String time_work = cursor.getString(indexTIME_WORK);
            String address_company = cursor.getString(indexADDRESS_COMPANY);
            String contact = cursor.getString(indexCONTACT);
            String desc = cursor.getString(indexDESC);
            String ot = cursor.getString(indexOT);
            String title_company = cursor.getString(indexTITLE_COMPANY);

            Company company = new Company(id_company,name, type, member, country, thumb_company, image, time_work, address_company, contact, desc, ot, title_company);
            Job job = new Job(id_job, title, thumbnail, info, address, join_date, estimatetime, lock, salary, skills, fast_info, company);
            arr.add(job);
            cursor.moveToNext();
        }
        closeDatabase();
        return arr;
    }


    public boolean deleteJob(int id) {
        openDatabase();
        boolean isDone = database.delete(TABLE, "id_job" + "=" + id, null) > 0;
        closeDatabase();
        return isDone;
    }

    public void insertJob(Job job) {
        openDatabase();
        ContentValues values = new ContentValues();
        values.put("id_job", job.getId_job());
        values.put("title", job.getTitle());
        values.put("thumbnail", job.getThumbnail());
        values.put("information", job.getInformation());
        values.put("address", job.getAddress());
        values.put("estimatetime", job.getEstimatetime());
        values.put("lock", job.getLock());
        values.put("salary", job.getSalary());
        values.put("skills", job.getSkills());
        values.put("fast_info", job.getFast_info());
        values.put("id_company", job.getCompany().getId_company());
        values.put("name", job.getCompany().getName());
        values.put("type", job.getCompany().getType());
        values.put("member", job.getCompany().getMember());
        values.put("country", job.getCompany().getCountry());
        values.put("thumbnail_company", job.getCompany().getThumbnail());
        values.put("image", job.getCompany().getImage());
        values.put("time_for_work", job.getCompany().getTime_for_work());
        values.put("address_company", job.getCompany().getAddress());
        values.put("contact", job.getCompany().getContact());
        values.put("desc", job.getCompany().getDescription());
        values.put("ot", job.getCompany().getOver_time());
        values.put("title_company", job.getCompany().getTitle());
        database.insert(TABLE, null, values);
        closeDatabase();
    }
}
