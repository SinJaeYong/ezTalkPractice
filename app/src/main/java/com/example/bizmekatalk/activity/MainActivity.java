package com.example.bizmekatalk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.adapter.ProfileListAdapter;
import com.example.bizmekatalk.items.ProfileItem;
import com.example.bizmekatalk.utils.HttpRequest;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import okhttp3.Response;

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
        String url = "http://10.0.102.59:31033/api/OrgUserInfo/GetAllUserInfo";
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
        int method = PreferenceManager.HTTP_METHOD_POST;

        //GetAllUserInfo 요청처리
        getAllItems(url,headerMap,bodyJson,method);

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


        //adapter.updateItems(allItems);
        //adapter.notifyDataSetChanged();
        profile_list.setAdapter(adapter);
    }

    public class ProfileListAsyncTask extends AsyncTask<HttpRequest,Void, Response> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Response doInBackground(HttpRequest... httpRequests) {
            Response response=null;
            try {
                response=httpRequests[0].post();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            //onPostExecute에서 item 업데이트
            try {
                JSONArray jsonArr = new JSONArray(response.body().string());
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
            } catch (IOException | JSONException e) {
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
    }

    //GetAllUserInfo 요청
    public void getAllItems(String url, Map<String, String> headerMap, JSONObject bodyJson, int method){
        //request 설정
        HttpRequest httpRequest = new HttpRequest(url,headerMap,bodyJson,method);
        //request 비동기적으로 보내기
        new ProfileListAsyncTask().execute(httpRequest);
    }

}