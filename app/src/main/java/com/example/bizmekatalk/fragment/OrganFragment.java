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
import com.example.bizmekatalk.activity.BizmekaApp;
import com.example.bizmekatalk.adapter.OrganAdapter;

import com.example.bizmekatalk.databinding.OrganFragmentBinding;

public class OrganFragment extends Fragment {
    private OrganAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        OrganFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.organ_fragment, container, false);
        binding.setOrganFragment(this);
        adapter = new OrganAdapter();
        //Log.i("jay.OrganFragment", "deptMap : "+BizmekaApp.deptMap.toString());
        BizmekaApp.navi.add("s907000");
        Log.i("jay.OrganFragment","navi.userMap : "+BizmekaApp.userMap.toString());
        if (adapter != null ){
            adapter.setData( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );
            adapter.addData( BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()) );
            adapter.notifyDataSetChanged();
        }

        binding.btnDeptBack.setOnClickListener(v -> {
            if(BizmekaApp.navi.size() > 1)
                BizmekaApp.navi.removeLast();
            Log.i("jay.OrganFragment","navi.getLast : "+BizmekaApp.navi.getLast());
            adapter.clear();
            adapter.addData( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );
            adapter.addData( BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()) );
            adapter.notifyDataSetChanged();
        });

        binding.lvDeptList.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }









}
