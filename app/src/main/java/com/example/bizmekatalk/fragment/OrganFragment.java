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
import com.example.bizmekatalk.crypto.AES256Cipher;
import com.example.bizmekatalk.databinding.OrganFragmentBinding;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.utils.PreferenceManager;
import com.google.gson.internal.LinkedTreeMap;

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

        adapter.updateAdapter(setRequestParams(""),"add");

        binding.btnDeptBack.setOnClickListener(v -> {
            Log.i("jay.OrganFragment","back");
            adapter.updateAdapter(setRequestParams(adapter.preDeptId),"reset");
        });

        binding.lvDeptList.setAdapter(adapter);
        return binding.getRoot();
    }


    private void callAPI(@javax.annotation.Nullable RequestParams params, CustomAdapter adapter, String mode) {
        new RequestAPI().<String>getCall(params).ifPresent(call->{
            setRequestCallBack(call, adapter, mode);
        });
    }


    private static void setRequestCallBack(Call<String> call, CustomAdapter adapter, String mode) {
        Log.i("jay.OrganFragment","mode : "+mode);
        new WebApiController<String>().request(call,(result)->{
            if(result.isSuccess()){
                String resultData = result.getData();
                Log.i("jay.OrganFragment","Result : "+resultData);
                try {
                    JSONArray jsonArr = new JSONArray(resultData);
                    List<JSONObject> list = new ArrayList<JSONObject>();
                    for(int i = 0 ; i < jsonArr.length() ; i++){
                        DeptItem item = new DeptItem();
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
                    ((DeptListAdapter)adapter).setMap(map);
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

    private RequestParams setRequestParams(String parentDeptId) {
        return new RequestParamBuilder().
                setPath(new ApiPath("OrgDeptInfo","GetAllDeptInfo")).
                setBodyJson("compid", PreferenceManager.getString(PreferenceManager.getCompId())).
                setBodyJson("lan",1).
                build();
    }

    private void cyperCheck(){
        String encryptJAVA = AES256Cipher.stringToEncryptStringWithJava("s907000");
        String decryptJAVA = AES256Cipher.stringToDecryptionStringWithJava(encryptJAVA);
        String encryptBiz = AES256Cipher.stringToEncryptStringWithBizmeka("s907000");
        String decryptBiz = AES256Cipher.stringToDecryptionStringWithBizmeka(encryptBiz);
        Log.i("jay.OrganFragment","preference compid : " + PreferenceManager.getString(PreferenceManager.getCompId()));
        Log.i("jay.OrganFragment","encrypt with JAVA: " + encryptJAVA);
        Log.i("jay.OrganFragment","decrypt with JAVA: " + decryptJAVA);
        Log.i("jay.OrganFragment","encrypt with Bizmeka: " + encryptBiz);
        Log.i("jay.OrganFragment","decrypt with Bizmeka: " + decryptBiz);
    }
}
