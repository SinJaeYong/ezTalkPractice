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

    private DeptListAdapter adapter = new DeptListAdapter(getActivity());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setDataBinding();

        requestCall();

        binding.deptList.setAdapter(adapter);

        return inflater.inflate(R.layout.organ_fragment, container,false);
    }



    private void requestCall() {
        //Request 동작
        new RequestAPI().<String>getCall(setRequestParams()).ifPresent(call->{
            new WebApiController<String>().request(call,(result)->{
                if(result.isSuccess()){
                    String deptResult = result.getData();
                    updateItems(deptResult);
                } else {
                    Log.i("jay.OrganFragment","에러. request : "+result.getError());
                }
            });
        });
    }


    private void updateItems(String deptResult) {
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
    }


    private void setDataBinding() {
        //dataBinding
        binding = DataBindingUtil.setContentView(getActivity(),R.layout.organ_fragment);
        binding.setOrganFragment(this);
    }

    private RequestParams setRequestParams() {
        return new RequestParamBuilder(getActivity()).
                setPath(new ApiPath("OrgDeptInfo","GetAllDeptInfo")).
                setBodyJson("compid", PreferenceManager.getString(PreferenceManager.getCompId())).
                setBodyJson("lan",1).
                build();
    }

}
