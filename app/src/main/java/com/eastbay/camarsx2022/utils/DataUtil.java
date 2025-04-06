package com.eastbay.camarsx2022.utils;

import android.text.TextUtils;

public class DataUtil {
    public static String filterText(String text) {
        if (text == null) {
            return "";
        }
        return TextUtils.isEmpty(text) ? "" : text;
    }

}
