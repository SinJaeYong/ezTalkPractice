package com.example.bizmekatalk.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.items.ProfileItem;
import com.example.bizmekatalk.utils.HttpRequest;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import okhttp3.Response;

public class ProfileListAdapter extends BaseAdapter {

    private Context context;
    private List<ProfileItem> items = new Vector<ProfileItem>();
    private int layoutResId;

    public ProfileListAdapter(Context context) {
        this.context = context;
    }

    public ProfileListAdapter(Context context, int layoutResId) {
        this(context);
        this.layoutResId = layoutResId;
    }

    public void updateItems(String url, Map<String, String> headerMap, JSONObject bodyJson, int method){
        //request 설정
        HttpRequest httpRequest = new HttpRequest(url,headerMap,bodyJson,method);
        //request 보내기
        new ProfileListAsyncTask().execute(httpRequest);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View itemView, ViewGroup viewGroup) {
        if(itemView==null){

            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView=inflater.inflate(R.layout.profile_item,null);
            //itemView=inflater.inflate(layoutResId,null);
        }
        ImageView itemProfileImage = itemView.findViewById(R.id.itemProfileImage);
        TextView itemName = itemView.findViewById(R.id.itemName);
        TextView itemPosition = itemView.findViewById(R.id.itemPosition);
        TextView itemJob = itemView.findViewById(R.id.itemJob);
        Glide.with(context).load(items.get(position).getProfileImageUrl()).transform(new CenterCrop()).into(itemProfileImage);
        itemName.setText(items.get(position).getName());
        itemPosition.setText(items.get(position).getPosition());
        itemJob.setText(items.get(position).getJob());

        return itemView;
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
                Log.i("jay.ProfileListAdapter","arr_length : "+jsonArr.length());
                for(int i=0;i<jsonArr.length();i++){
                    ProfileItem item = new ProfileItem();
                    String profileImage = jsonArr.getJSONObject(i).getString("profileimage");
                    String profileImgUrl = PreferenceManager.UPLOAD_URL + jsonArr.getJSONObject(i).getString("profileimage");
                    item.setProfileImageUrl(profileImgUrl);
                    item.setName(jsonArr.getJSONObject(i).getString("name"));
                    item.setPosition(jsonArr.getJSONObject(i).getString("position"));
                    item.setJob(jsonArr.getJSONObject(i).getString("job"));
                    items.add(item);

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            notifyDataSetChanged();
        }
    }
}
