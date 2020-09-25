package com.example.bizmekatalk.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.common.RequestParamBuilder;
import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrganFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RequestAPI requestAPI = new RequestAPI();

        RequestParams params = new RequestParamBuilder(getActivity()).
                setPath(new ApiPath("OrgDeptInfo","GetAllDeptInfo")).
                setBodyJson("compid",PreferenceManager.getString(getActivity(),PreferenceManager.COMP_ID)).
                setBodyJson("lan",1).
                build();

        requestAPI.<String>getCall(params).ifPresent(call->{
            new WebApiController<String>().request(call,(result)->{
                if(result.isSuccess()){
                    String deptResult = result.getData();
                    Log.i("jay.OrganFragment","result : "+deptResult);
                } else {
                    Log.i("jay.OrganFragment","에러. request : "+result.getError());
                }
            });
        });


        return inflater.inflate(R.layout.fragment_organ,container,false);

    }

}
