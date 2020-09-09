package com.example.bizmekatalk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.bizmekatalk.API.RequestAPI;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.adapter.ProfileListAdapter;
import com.example.bizmekatalk.items.ProfileItem;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private ListView profile_list;
    private List<ProfileItem> allItems = new Vector<ProfileItem>();
    private ProfileListAdapter adapter;
    private boolean lastitemVisibleFlag = false;
    private int currentItemCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //ListView 및 Adapter 인스턴스
        profile_list = findViewById(R.id.profile_list);
        adapter = new ProfileListAdapter(this);

        //GetAllUserInfo에 요청하기 위한 Url, RequestHeader 및 RequestBody 설정
        String path = "OrgUserInfo/GetAllUserInfo";
        Map<String,String> headerMap = new HashMap<String,String>();
        JSONObject keyJson = new JSONObject();
        JSONObject bodyJson = new JSONObject();
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


        //GetAllUserInfo 요청처리
        getAllItems(path,headerMap,bodyJson,PreferenceManager.HTTP_METHOD_POST);

        //ListView 스크롤 반응 처리
        profile_list.setOnScrollListener(new ListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    Log.i(PreferenceManager.TAG,"스크롤의 끝 "+currentItemCount);
                    int i =0;
                    while(adapter.getCount()<allItems.size()&&i<PreferenceManager.PROFILE_LIST_STEP){
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
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });
        profile_list.setAdapter(adapter);
    }

    //GetAllUserInfo 요청
    public void getAllItems(String path, Map<String, String> headerMap, JSONObject bodyJson, int method){
        //request 설정
        Call<String> call = new RequestAPI().getCall(path,headerMap,bodyJson,method);
        //request 비동기적으로 보내기
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONArray jsonArr = new JSONArray(response.body());
                    for(int i=0;i<jsonArr.length();i++){
                        ProfileItem item = new ProfileItem();
                        String profileImage = jsonArr.getJSONObject(i).getString("profileimage");
                        String profileImgUrl = PreferenceManager.UPLOAD_URL + profileImage;
                        item.setProfileImageUrl(profileImgUrl);
                        item.setName(jsonArr.getJSONObject(i).getString("name"));
                        item.setPosition(jsonArr.getJSONObject(i).getString("position"));
                        item.setJob(jsonArr.getJSONObject(i).getString("job"));
                        allItems.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //ListView 첫 화면에 표시할 정보 Adapter에 전달
                int i=0;
                while(adapter.getCount()<allItems.size()&&i<PreferenceManager.PROFILE_LIST_STEP){
                    adapter.updateItems(allItems.get(i));
                    i++;
                }
                //현재까지 전달한 아이템 카운트
                currentItemCount=i;

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(PreferenceManager.TAG,"Response Fail");
                t.printStackTrace();
            }
        });
    }

}