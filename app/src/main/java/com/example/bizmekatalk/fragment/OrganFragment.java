package com.example.bizmekatalk.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.activity.BizmekaApp;
import com.example.bizmekatalk.adapter.DeptListAdapter;
import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.common.RequestParamBuilder;
import com.example.bizmekatalk.api.common.RequestParams;

import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.databinding.OrganFragmentBinding;
import com.example.bizmekatalk.items.DataRepository;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrganFragment extends Fragment {
    private DeptListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("jay.DataRepository","MainThread");
        OrganFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.organ_fragment, container, false);
        binding.setOrganFragment(this);
        adapter = new DeptListAdapter();

        DataRepository.getInstance().getNavi().push("s907000");

        setData();

        binding.btnDeptBack.setOnClickListener(v -> {
            if(DataRepository.getInstance().getNavi().size() > 1)
                DataRepository.getInstance().getNavi().removeLast();
            Log.i("jay.navi","navi.gitList : "+DataRepository.getInstance().getNavi().getLast());
            setData();
        });

        binding.lvDeptList.setAdapter(adapter);
        return binding.getRoot();
    }

    private void setData(){
        if (adapter != null ){
            adapter.updateItems(
                    DataRepository.getInstance().getDeptMap().
                            get(DataRepository.getInstance().getNavi().getLast())
            );
            adapter.notifyDataSetChanged();
        }
    }




}
