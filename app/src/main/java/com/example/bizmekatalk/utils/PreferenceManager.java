package com.example.bizmekatalk.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.bizmekatalk.activity.BizmekaApp;

public class PreferenceManager {

    public static final int PIN_MAX_COUNT = 4;

    public static final int PROFILE_LIST_STEP = 20;

    public static final String API_URL = "http://10.0.102.59:31033/api/";

    public static final String API_SERVER_DOMAIN = "10.0.102.59";

    public static final String API_SERVER_PORT = "31033";

    public static final String TAG = "jay";

    public static final String PIN_KEY = "pin";
    private final String addd = "pin";

    public static final String USER_ID = "userId";

    public static final String COMP_ID = "compId";

    public static final String L_TOKEN = "lToken";

    public static final String PREFERENCES_NAME = "rebuild_preference";

    public static final String UPLOAD_URL = "http://10.0.102.59";

    public static final int HTTP_METHOD_POST = 1;

    private static final String DEFAULT_VALUE_STRING = "";

    private static final boolean DEFAULT_VALUE_BOOLEAN = false;

    private static final int DEFAULT_VALUE_INT = -1;

    private static final long DEFAULT_VALUE_LONG = -1L;

    private static final float DEFAULT_VALUE_FLOAT = -1F;




    private static SharedPreferences getPreferences(Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    }

    /**
     * String 값 저장
     * @param key
     * @param value
     */

    public static void setString(String key, String value) {
        SharedPreferences prefs = getPreferences(BizmekaApp.getAppContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }



    public static String getApiUrl(){
        return API_URL;
    }
    /**
     * int 값 저장
     * @param context
     * @param key
     * @param value
     */

    public static void setInt(Context context, String key, int value) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();

    }





    /**
     * float 값 저장
     * @param context
     * @param key
     * @param value
     */

    public static void setFloat(Context context, String key, float value) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();

    }



    /**
     * String 값 로드
     * @param key
     * @return
     */

    public static String getString(String key) {
        return getPreferences(BizmekaApp.getAppContext()).getString(key, DEFAULT_VALUE_STRING);
    }






    /**
     * int 값 로드
     * @param key
     * @return
     */

    public static int getInt(String key) {
        SharedPreferences prefs = getPreferences(BizmekaApp.getAppContext());
        int value = prefs.getInt(key, DEFAULT_VALUE_INT);
        return value;
    }




    /**
     * float 값 로드
     * @param key
     * @return
     */

    public static float getFloat(String key) {

        SharedPreferences prefs = getPreferences(BizmekaApp.getAppContext());
        float value = prefs.getFloat(key, DEFAULT_VALUE_FLOAT);
        return value;

    }


    /**
     * 키 값 삭제
     * @param key
     */

    public static void removeKey(String key) {

        SharedPreferences prefs = getPreferences(BizmekaApp.getAppContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();

    }



    /**
     * 모든 저장 데이터 삭제
     */

    public static void clear() {

        SharedPreferences prefs = getPreferences(BizmekaApp.getAppContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();

    }

}
