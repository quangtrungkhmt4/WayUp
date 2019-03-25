package com.example.wayup.constant;

import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import lombok.NoArgsConstructor;

import static android.content.Context.WIFI_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

@NoArgsConstructor
public class API {

    public static final String IP = "http://192.168.15.108:9999/v1/";
    public static final String DOMAIN = "http://192.168.15.108/storage_wayup/";

    // user
    public static final String LOGIN_USER =  "/v1/users";
    public static final String REGISTER_USER = "/v1/users";
    public static final String UPDATE_USER = "/v1/users";
    public static final String IS_EXISTS_EMAIL = "/v1/users/";
    public static final String SEARCH_USER_WITH_EMAIL = "/v1/users/email";

    //company
    public static final String GET_COUNT_COMPANY = "/v1/companies/size";
    public static final String GET_ALL_COMPANY_WITH_PAGE = "/v1/companies/all";
    public static final String GET_ALL_COMPANY = "/v1/companies";
    public static final String SEARCH_COMPANY = "/v1/companies/name";

    //job
    public static final String GET_ALL_JOB_WITH_PAGE = "/v1/jobs";
    public static final String GET_ALL_JOB = "/v1/jobs/size";
    public static final String GET_ALL_JOB_WITH_COMPANY = "/v1/jobs/";
    public static final String SEARCH_WITH_CONDITION = "/v1/jobs/";
    public static final String GET_QUANTUM_LANGUAGE = "/v1/jobs/skills";
    public static final String GET_QUANTUM_LEVEL = "/v1/jobs/level";
    public static final String GET_QUANTUM_CITY = "/v1/jobs/city";
    public static final String GET_ALL_JOB_WITHOUT_PAGE = "/v1/jobs/getAll";

    //file
    public static final String UPLOAD_FILE = "/v1/uploadFile";

    //image
    public static final String GET_ALL_IMAGES_WITH_JOB = "/v1/images";

    //comment
    public static final String POST_COMPANY_COMMENT = "/v1/comments";
    public static final String GET_COMPANY_COMMENT = "/v1/comments/";

    //profile
    public static final String POST_PROFILE = "/v1/profiles";
    public static final String GET_COUNT_PROFILE = "/v1/profiles/";
    public static final String GET_ALL_PROFILE = "/v1/profiles/all/";
    public static final String DELETE_PROFILE = "/v1/profiles";

    //apply
    public static final String POST_APPLY = "/v1/apply";
    public static final String SEND_MAIL = "/v1/mail";
    public static final String GET_APPLY_WITH_USER = "/v1/apply/";
    public static final String SEARCH_APPLY = "/v1/apply/";

    //notification
    public static final String POST_NOTI = "/v1/noties";
    public static final String DELETE_NOTI = "/v1/noties";

    //hr
    public static final String POST_HR = "/v1/hrs";
    public static final String SEND_CODE = "/v1/mail/sendCode";

    //mail
    public static final String SEND_PASS = "/v1/mail/sendPass";

}


