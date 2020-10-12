package com.example.bizmekatalk.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.adapter.CustomAdapter;
import com.example.bizmekatalk.adapter.DeptListAdapter;
import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.common.RequestParamBuilder;
import com.example.bizmekatalk.api.common.RequestParams;

import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.databinding.OrganFragmentBinding;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;

public class OrganFragment extends Fragment {
    private OrganFragmentBinding binding;
    private DeptListAdapter adapter = new DeptListAdapter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.organ_fragment,container,false);
        binding.setOrganFragment(this);

        //cyperCheck();
        //adapter.updateAdapter(setRequestParams(""),"add");
        callAPI(setDeptRequestParams(),adapter);

        binding.btnDeptBack.setOnClickListener(v -> {
            if(adapter.navi.size() > 1)
                adapter.navi.removeLast();
            Log.i("jay.navi","navi.gitList : "+adapter.navi.getLast());
            adapter.updateItems(adapter.getMap().get(adapter.navi.getLast()));
            adapter.notifyDataSetChanged();
            //adapter.updateAdapter(setRequestParams(adapter.preDeptId),"reset");
        });

        binding.lvDeptList.setAdapter(adapter);
        return binding.getRoot();
    }


    private void callAPI(@javax.annotation.Nullable RequestParams params, CustomAdapter adapter) {
        new RequestAPI().<String>getCall(params).ifPresent(call->{
            setRequestCallBack(call, adapter);
        });
    }


    private static void setRequestCallBack(Call<String> call, CustomAdapter adapter) {
        new WebApiController<String>().request(call,(result)->{
            if(result.isSuccess()){
                String resultData = result.getData();
                Log.i("jay.OrganFragment","Result : "+resultData);
                try {
                    JSONArray jsonArr = new JSONArray(resultData);
                    List<JSONObject> list = new ArrayList<JSONObject>();
                    for(int i = 0 ; i < jsonArr.length() ; i++){
                        JSONObject json = new JSONObject(jsonArr.get(i).toString());
                        list.add(json);
                    }
                    Map<String,List<JSONObject>> map = list.stream().collect(Collectors.groupingBy(json-> {
                        try {
                            return json.getString("parentdeptid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return "";
                        }
                    }));
                    Log.i("jay.OrganFragment","list : "+map.get("").toString());
                    ((DeptListAdapter)adapter).setMap(map);
                    ((DeptListAdapter)adapter).initNavi("");
                    Log.i("jay.navi","navi.gitList : "+((DeptListAdapter)adapter).navi.getLast());
                    adapter.updateItems(map.get(""));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i("jay.OrganFragment","에러. response : "+result.getError());
            }
        });
    }

    private RequestParams setDeptRequestParams() {
        return new RequestParamBuilder().
                setPath(new ApiPath("OrgDeptInfo","GetAllDeptInfo")).
                setBodyJson("compid", PreferenceManager.getString(PreferenceManager.getCompId())).
                setBodyJson("lan",1).
                build();
    }

    private RequestParams setUserRequestParams(String ... params){
        return new RequestParamBuilder().
                setPath(new ApiPath("OrgDeptInfo","GetAllDeptInfo")).
                setBodyJson("compid", PreferenceManager.getString(PreferenceManager.getCompId())).
                setBodyJson("lan",1).
                build();
    }


}
