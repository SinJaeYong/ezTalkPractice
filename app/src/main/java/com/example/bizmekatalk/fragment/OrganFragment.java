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
import com.example.bizmekatalk.adapter.DeptListAdapter;
import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.common.RequestParamBuilder;
import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;

import com.example.bizmekatalk.databinding.OrganFragmentBinding;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrganFragment extends Fragment {

    private OrganFragmentBinding binding;

    private DeptListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.setContentView(getActivity(),R.layout.organ_fragment);
        binding.setOrganFragment(this);


        RequestParams params = new RequestParamBuilder(getActivity()).
                setPath(new ApiPath("OrgDeptInfo","GetAllDeptInfo")).
                setBodyJson("compid",PreferenceManager.getString(PreferenceManager.COMP_ID)).
                setBodyJson("lan",1).
                build();

        new RequestAPI().<String>getCall(params).ifPresent(call->{
            new WebApiController<String>().request(call,(result)->{
                if(result.isSuccess()){
                    String deptResult = result.getData();
                    try {
                        JSONArray jsonArr = new JSONArray(deptResult);
                        for(int i = 0 ; i < jsonArr.length() ; i++){
                            JSONObject json = new JSONObject(jsonArr.get(i).toString());
                            String temp = json.get("deptname").toString();
                            adapter.updateItems(temp);
                            //Log.i("jay.OrganFragment","json : "+temp);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("jay.OrganFragment","에러. request : "+result.getError());
                }
            });
        });

        adapter = new DeptListAdapter(getActivity());

        binding.deptList.setAdapter(adapter);

        return inflater.inflate(R.layout.organ_fragment,container,false);
    }

}
