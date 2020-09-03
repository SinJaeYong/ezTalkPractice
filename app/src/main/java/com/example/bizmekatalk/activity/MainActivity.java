package com.example.bizmekatalk.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.adapter.ProfileListAdapter;
import com.example.bizmekatalk.items.ProfileItem;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    List<ProfileItem> profileItems = new Vector<ProfileItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        String url = "http://10.0.102.59:31033/api/OrgUserInfo/GetAllUserInfo";
        Map<String,String> headerMap = new HashMap<String,String>();
        JSONObject keyJson = new JSONObject();
        JSONObject bodyJson = new JSONObject();
        try {
            Log.i("jay.MainActivity","compid "+PreferenceManager.getString(this,PreferenceManager.COMP_ID));
            keyJson.put("userid",PreferenceManager.getString(this,PreferenceManager.USER_ID));
            keyJson.put("compid",PreferenceManager.getString(this,PreferenceManager.COMP_ID));
            keyJson.put("ltoken",PreferenceManager.getString(this,PreferenceManager.L_TOKEN));
            bodyJson.put("compid",PreferenceManager.getString(this,PreferenceManager.COMP_ID));
            bodyJson.put("lan",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        headerMap.put("Content-Type","application/json;odata.metadata=full");
        headerMap.put("Expect","100-continue");
        headerMap.put("key",keyJson.toString());
        int method = PreferenceManager.HTTP_METHOD_POST;

        final ListView profile_list = findViewById(R.id.profile_list);
        final ProfileListAdapter adapter = new ProfileListAdapter(this);
        adapter.updateItems(url,headerMap,bodyJson,method);
        profile_list.setAdapter(adapter);


    }


}