package com.litl.www.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

public class TestConfig {

    public static String loginUrl;
    public static String addUserUrl;
    public static String updateUserInfoUrl;
    public static String getUserListUrl;
    public static String getUserInfoUrl;
    //储存cookies信息的变量
    public static CookieStore store;
    //http客户端
    public static DefaultHttpClient defaultHttpClient;
}
