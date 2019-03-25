package com.example.wayup.fragment;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wayup.MainActivity;
import com.example.wayup.R;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Job;
import com.example.wayup.model.Profile;
import com.example.wayup.model.VolleyMultipartRequest;
import com.example.wayup.model.User;
import com.example.wayup.util.DateTime;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.ProcessDialog;
import com.example.wayup.util.VolleySingleton;
import com.example.wayup.view.CusBoldTextView;
import com.example.wayup.view.CusRegularTextView;
import com.github.mikephil.charting.utils.FileUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_PICK_IMAGE = 1997;
    private MainActivity mainActivity;
    private CircleImageView imAvatar;
    private CusBoldTextView tvName;
    private CusRegularTextView tvEmail, tvPass, tvPhone, tvGender, tvBirth, tvSkype
            , tvAddress, tvEducation, tvExp, tvHardSkill, tvSoftSkill, tvInfo, tvTarget;
    private ImageView imEditEmail, imEditPass, imEditPhone, imEditGender, imEditBirth
            , imEditSkype, imEditAddress, imEditEducation, imEditExp, imEditHardSkill
            , imEditSoftSkill, imEditInfo, imEditTarget, imUploadCV;
    private User currentUser;
    private Dialog dialog;
    private RequestQueue requestQueue;
    private ProcessDialog processDialog;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        findId(rootView);
        initViews();
        return rootView;
    }

    private void findId(View view){
        imAvatar = view.findViewById(R.id.imAvatarProfile);
        tvName = view.findViewById(R.id.tvNameProfile);
        tvEmail = view.findViewById(R.id.tvMailProfile);
        tvPass = view.findViewById(R.id.tvPassProfile);
        tvPhone = view.findViewById(R.id.tvPhoneProfile);
        tvGender = view.findViewById(R.id.tvGenderProfile);
        tvBirth = view.findViewById(R.id.tvBirthdayProfile);
        tvSkype = view.findViewById(R.id.tvSkypeProfile);
        tvAddress = view.findViewById(R.id.tvAddressProfile);
        tvEducation = view.findViewById(R.id.tvEducationProfile);
        tvExp = view.findViewById(R.id.tvExpProfile);
        tvHardSkill = view.findViewById(R.id.tvHardSkillProfile);
        tvSoftSkill = view.findViewById(R.id.tvSoftSkillProfile);
        tvInfo = view.findViewById(R.id.tvInfoProfile);
        tvTarget = view.findViewById(R.id.tvTargetProfile);

        imEditEmail = view.findViewById(R.id.imEditEmail);
        imEditPass = view.findViewById(R.id.imEditPass);
        imEditPhone = view.findViewById(R.id.imEditPhone);
        imEditGender = view.findViewById(R.id.imEditGender);
        imEditBirth = view.findViewById(R.id.imEditBirthDay);
        imEditSkype = view.findViewById(R.id.imEditSkype);
        imEditAddress = view.findViewById(R.id.imEditAddress);
        imEditEducation = view.findViewById(R.id.imEditEducation);
        imEditExp = view.findViewById(R.id.imEditExp);
        imEditHardSkill = view.findViewById(R.id.imEditHardSkill);
        imEditSoftSkill = view.findViewById(R.id.imEditSoftSkill);
        imEditInfo = view.findViewById(R.id.imEditInfoProfile);
        imEditTarget = view.findViewById(R.id.imEditTargetProfile);
        imUploadCV = view.findViewById(R.id.imUploadCv);

    }

    private void initViews(){
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        mainActivity = (MainActivity) getActivity();
        imEditEmail.setOnClickListener(this);
        imEditPass.setOnClickListener(this);
        imEditPhone.setOnClickListener(this);
        imEditGender.setOnClickListener(this);
        imEditBirth.setOnClickListener(this);
        imEditSkype.setOnClickListener(this);
        imEditAddress.setOnClickListener(this);
        imEditEducation.setOnClickListener(this);
        imEditExp.setOnClickListener(this);
        imEditHardSkill.setOnClickListener(this);
        imEditSoftSkill.setOnClickListener(this);
        imEditInfo.setOnClickListener(this);
        imEditTarget.setOnClickListener(this);
        tvName.setOnClickListener(this);
        imAvatar.setOnClickListener(this);
        processDialog = new ProcessDialog(getContext());
        imUploadCV.setOnClickListener(this);

        loadData();
    }

    private void loadData() {
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, getContext()), User.class);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        String img = currentUser.getImage().contains("http")?currentUser.getImage():Preferences.getData(Key.DOMAIN, mainActivity) + currentUser.getImage();
        Glide.with(getContext()).load(img).apply(requestOptions).into(imAvatar);

        tvName.setText(currentUser.getName());
        tvEmail.setText(currentUser.getEmail());
        tvPass.setText(currentUser.getPassword());
        tvPhone.setText(currentUser.getPhone());
        tvGender.setText(currentUser.getGender());
        tvTarget.setText(currentUser.getTarget());
        tvBirth.setText(currentUser.getBirthday());
        tvSkype.setText(currentUser.getSkype());
        tvAddress.setText(currentUser.getAddress());
        tvEducation.setText(currentUser.getEducation());
        tvExp.setText(currentUser.getExperience());
        tvHardSkill.setText(currentUser.getHard_skill());
        tvSoftSkill.setText(currentUser.getSoft_skill());
        tvInfo.setText(currentUser.getInfo());
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                if (enter) {
                    return AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
                } else {
                    return AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
                }
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                if (enter) {
                    return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_close_enter);
                } else {
                    return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_close_exit);
                }
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
            default:
                if (enter) {
                    return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_open_enter);
                } else {
                    return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_open_exit);
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNameProfile:
                initDialogChangeString("tên mới...", R.id.tvNameProfile);
                break;
            case R.id.imEditPass:
                initDialogChangeString("mật khẩu mới...", R.id.imEditPass);
                break;
            case R.id.imEditPhone:
                initDialogChangeString("số điện thoại mới...", R.id.imEditPhone);
                break;
            case R.id.imEditSkype:
                initDialogChangeString("địa chỉ Skype mới...", R.id.imEditSkype);
                break;
            case R.id.imEditAddress:
                initDialogChangeString("địa chỉ mới...", R.id.imEditAddress);
                break;
            case R.id.imEditEducation:
                initDialogChangeString("học vấn...", R.id.imEditEducation);
                break;
            case R.id.imEditExp:
                initDialogChangeString("kinh nghiệm...", R.id.imEditExp);
                break;
            case R.id.imEditHardSkill:
                initDialogChangeString("kỹ năng...", R.id.imEditHardSkill);
                break;
            case R.id.imEditSoftSkill:
                initDialogChangeString("kỹ năng mềm...", R.id.imEditSoftSkill);
                break;
            case R.id.imEditInfoProfile:
                initDialogChangeString("thông tin...", R.id.imEditInfoProfile);
                break;
            case R.id.imEditTargetProfile:
                initDialogChangeString("mục tiêu nghề nghiệp...", R.id.imEditTargetProfile);
                break;
            case R.id.imEditGender:
                initDialogChangeGender();
                break;
            case R.id.imEditBirthDay:
                initDialogChangeBirth();
                break;
            case R.id.imAvatarProfile:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
                break;
            case R.id.imUploadCv:
                countProfile(currentUser);
                break;
        }
    }

    private void countProfile(final User user){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainActivity)
                + API.GET_COUNT_PROFILE + user.getId_user(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("count").toString();
                        if (Integer.parseInt(data) >=5){
                            Toast.makeText(mainActivity, mainActivity.getString(R.string.max_cv), Toast.LENGTH_SHORT).show();
                        }else {
                            showFileChooser();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private static final int FILE_SELECT_CODE = 0;
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, getString(R.string.select_cv)),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(mainActivity, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null){
            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), imageUri);

                //displaying selected image to imageview
                imAvatar.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap, ".png");

                processDialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream iStream =   mainActivity.getContentResolver().openInputStream(uri);
                uploadCV(iStream, ".docx");

                processDialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void uploadBitmap(final Bitmap bitmap, final String type) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,  Preferences.getData(Key.IP, mainActivity) + API.UPLOAD_FILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            currentUser.setImage(obj.getString("fileName"));
                            updateRequest();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mainActivity, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(currentUser.getId_user()+ "_" + imagename + type, getFileDataFromDrawable(bitmap)));
                Log.e("========", currentUser.getId_user()+ "_" + imagename + type);
                return params;
            }
        };
        requestQueue.add(volleyMultipartRequest);
    }

    public void uploadCV(final InputStream stream, final String type) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,  Preferences.getData(Key.IP, mainActivity) + API.UPLOAD_FILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            insertProfile(obj.getString("fileName"));
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mainActivity, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    params.put("file", new DataPart("cv_" + DateTime.currentDateTime() + type, getBytes(stream)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("========", currentUser.getId_user()+ "_" + DateTime.currentDateTime() + type);
                return params;
            }
        };
        requestQueue.add(volleyMultipartRequest);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void initDialogChangeString(String action, final int id){
        dialog = new Dialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_change_string);
        dialog.setCancelable(true);

        final EditText edtChangeData = dialog.findViewById(R.id.edtChangeData);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmChangeData);

        edtChangeData.setHint(getString(R.string.input) + " " + action);

        if (action.equals("số điện thoại mới...")){
            edtChangeData.setInputType(InputType.TYPE_CLASS_PHONE);
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edtChangeData.getText().toString();
                if (data.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtChangeData, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                switch (id){
                    case R.id.tvNameProfile:
                        currentUser.setName(data);
                        break;
                    case R.id.imEditPass:
                        currentUser.setPassword(data);
                        break;
                    case R.id.imEditPhone:
                        currentUser.setPhone(data);
                        break;
                    case R.id.imEditSkype:
                        currentUser.setSkype(data);
                        break;
                    case R.id.imEditAddress:
                        currentUser.setAddress(data);
                        break;
                    case R.id.imEditEducation:
                        currentUser.setEducation(data);
                        break;
                    case R.id.imEditExp:
                        currentUser.setExperience(data);
                        break;
                    case R.id.imEditHardSkill:
                        currentUser.setHard_skill(data);
                        break;
                    case R.id.imEditSoftSkill:
                        currentUser.setSoft_skill(data);
                        break;
                    case R.id.imEditInfoProfile:
                        currentUser.setInfo(data);
                        break;
                    case R.id.imEditTargetProfile:
                        currentUser.setTarget(data);
                        break;
                }
                dialog.dismiss();
                updateRequest();
            }
        });
        dialog.show();
    }

    private int mYear, mMonth, mDay;
    private void initDialogChangeBirth(){
        dialog = new Dialog(rootView.getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_change_birth);
        dialog.setCancelable(true);

        final EditText edtChangeData = dialog.findViewById(R.id.edtChangeData);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmChangeData);


        edtChangeData.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                dialog.hide();
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

//                                edtChangeData.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dialog.show();
                        String d = String.valueOf(dayOfMonth);
                        String m = String.valueOf(month+1);
                        String y = String.valueOf(year);
                        if (dayOfMonth < 10){
                            d = "0" + d;
                        }
                        if ((month + 1) < 10){
                            m = "0" + m;
                        }
                        String date = String.format("%s/%s/%s",d,m,y);
                        edtChangeData.setText(date);
                    }
                });
                datePickerDialog.show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edtChangeData.getText().toString();
                if (data.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtChangeData, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                currentUser.setBirthday(data);
                dialog.dismiss();
                updateRequest();
            }
        });
        dialog.show();
    }

    private void initDialogChangeGender(){
        dialog = new Dialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_change_gender);
        dialog.setCancelable(true);

        final RadioButton radMale = dialog.findViewById(R.id.radMale);
        RadioButton radFemale = dialog.findViewById(R.id.radFemale);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmChangeData);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = "";
                if (radMale.isChecked()){
                    gender = getString(R.string.male);
                }else {
                    gender = getString(R.string.female);
                }
                currentUser.setGender(gender);
                dialog.dismiss();
                updateRequest();
            }
        });
        dialog.show();
    }

    private void reLoadData(){
        tvName.setText(currentUser.getName());
        tvEmail.setText(currentUser.getEmail());
        tvPass.setText(currentUser.getPassword());
        tvPhone.setText(currentUser.getPhone());
        tvGender.setText(currentUser.getGender());
        tvTarget.setText(currentUser.getTarget());
        tvBirth.setText(currentUser.getBirthday());
        tvSkype.setText(currentUser.getSkype());
        tvAddress.setText(currentUser.getAddress());
        tvEducation.setText(currentUser.getEducation());
        tvExp.setText(currentUser.getExperience());
        tvHardSkill.setText(currentUser.getHard_skill());
        tvSoftSkill.setText(currentUser.getSoft_skill());
        tvInfo.setText(currentUser.getInfo());
    }

    private void updateRequest(){
        String json = new Gson().toJson(currentUser);
        JSONObject requestBody = null;
        try {
            requestBody = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Preferences.getData(Key.IP, mainActivity) + API.UPDATE_USER, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("user").toString();
                        if (data.equals("null")){

                        }else {
                            reLoadData();
                            Preferences.saveData(Key.USER, data.toString(), getContext());

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void insertProfile(String fileName){
        Profile profile = new Profile(fileName, currentUser);
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        JSONObject requestBody = null;
        try {
            requestBody = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Preferences.getData(Key.IP, mainActivity) + API.POST_PROFILE, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("response").toString();
                        if (data.equals("false")){
                            Toast.makeText(mainActivity, getString(R.string.insert_cv_fail), Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(mainActivity, getString(R.string.insert_cv_success), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
