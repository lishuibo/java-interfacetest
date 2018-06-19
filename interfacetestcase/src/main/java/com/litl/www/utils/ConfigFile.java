package com.litl.www.utils;

import com.litl.www.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);

    public static String getUrl(InterfaceName name) {
        String address = bundle.getString("test.url");
        String uri = "";
        String testUrl;
        if (name == InterfaceName.LOGIN) {
            uri = bundle.getString("login.uri");
        }

        if (name == InterfaceName.ADDUSERINFO) {
            uri = bundle.getString("addUser.uri");
        }

        if (name == InterfaceName.GETUSERINFO) {
            uri = bundle.getString("getUserInfo.uri");
        }

        if (name == InterfaceName.GETUSERLIST) {
            uri = bundle.getString("getUserList.uri");
        }

        if (name == InterfaceName.UPDATEUSERINFO) {
            uri = bundle.getString("updateUser.uri");
        }

        testUrl = address+uri;
        return testUrl;
    }
}
