package com.example.bizmekatalk.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.adapter.ProfileListAdapter;
import com.example.bizmekatalk.fragment.ChatFragment;
import com.example.bizmekatalk.fragment.GroupFragment;
import com.example.bizmekatalk.fragment.OrganFragment;
import com.example.bizmekatalk.fragment.SettingFragment;
import com.example.bizmekatalk.items.ProfileItem;
import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.utils.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private ListView profile_list;
    private List<ProfileItem> allItems = new Vector<ProfileItem>();
    private ProfileListAdapter adapter;
    private boolean lastitemVisibleFlag = false;
    private int currentItemCount=0;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private GroupFragment groupFragment = new GroupFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private OrganFragment organFragment = new OrganFragment();
    private SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout,organFragment).commitAllowingStateLoss();
        bottomNavigationView.setSelectedItemId(R.id.navigation_organ);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            FragmentTransaction trans = fragmentManager.beginTransaction();
            switch (menuItem.getItemId()){
                case R.id.navigation_group:{
                    trans.replace(R.id.frame_layout,groupFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_chat:{
                    trans.replace(R.id.frame_layout,chatFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_organ:{
                    trans.replace(R.id.frame_layout,organFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_setting:{
                    trans.replace(R.id.frame_layout,settingFragment).commitAllowingStateLoss();
                    break;
                }
            }
            return true;
        });

        //ListView 및 Adapter 인스턴스
        //profile_list = findViewById(R.id.profile_list);
        //adapter = new ProfileListAdapter(this);

        //GetAllUserInfo 요청처리
        //getAllItems(setRequestParam());

        //ListView 스크롤 반응 처리
        /*
        if(profile_list != null) {
            scrolledDataUpdate();
        }
         */
    }

    private RequestParams setParams(){
        //GetAllUserInfo에 요청하기 위한 Url, RequestHeader 및 RequestBody 설정
        ApiPath apiPath = new ApiPath("OrgUserInfo","GetAllUserInfo");

        Map<String,String> headerMap = new HashMap<String,String>();
        JSONObject keyJson = new JSONObject();

        JSONObject bodyJson = new JSONObject();

        int method = PreferenceManager.HTTP_METHOD_POST;

        try {
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

        return new RequestParams(apiPath,headerMap,bodyJson,method);
    }


    private void scrolledDataUpdate(){
            profile_list.setOnScrollListener(new ListView.OnScrollListener(){
                @Override
                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                        Log.i(PreferenceManager.TAG,"스크롤의 끝 "+currentItemCount);
                        int i =0;
                        while((adapter.getCount() < allItems.size()) && (i < PreferenceManager.PROFILE_LIST_STEP)){
                            adapter.updateItems(allItems.get(currentItemCount));
                            currentItemCount++;
                            i++;
                        }
                        adapter.notifyDataSetChanged();
                        //데이터 로드
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    //Flag 변경
                    lastitemVisibleFlag = (totalItemCount > 0) && ((firstVisibleItem + visibleItemCount) >= totalItemCount);
                }
            });
            profile_list.setAdapter(adapter);
    }


    //GetAllUserInfo 요청
    private void getAllItems(RequestParams params){
        //request 설정
        RequestAPI requestAPI = new RequestAPI();
        requestAPI.<String>getCall(params).ifPresent(call->{
            new WebApiController<String>().request(call,(result)->{
                if(result.isSuccess()){
                    try {
                        JSONArray jsonArr = new JSONArray(result.getData());
                        String tempStr = null;
                        for(int i=0;i<jsonArr.length();i++){
                            ProfileItem item = new ProfileItem();
                            String profileImage = jsonArr.getJSONObject(i).getString("profileimage");
                            String profileImgUrl = PreferenceManager.UPLOAD_URL + profileImage;
                            item.setProfileImageUrl(profileImgUrl);
                            item.setName(jsonArr.getJSONObject(i).getString("name"));
                            tempStr = jsonArr.getJSONObject(i).getString("position");
                            tempStr = ("".equals(tempStr.trim())) ? tempStr : ("("+tempStr+")");
                            item.setPosition(tempStr);
                            item.setJob(jsonArr.getJSONObject(i).getString("job"));
                            allItems.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //ListView 첫 화면에 표시할 정보 Adapter에 전달
                    int i=0;
                    while((adapter.getCount() < allItems.size()) &&(i < PreferenceManager.PROFILE_LIST_STEP)){
                        adapter.updateItems(allItems.get(i));
                        i++;
                    }
                    //현재까지 전달한 아이템 카운트
                    currentItemCount=i;

                    adapter.notifyDataSetChanged();
                } else {
                    Log.i(PreferenceManager.TAG,"프로필 업데이트 실패. request : "+result.getError());
                }
            });
        });
    }

}