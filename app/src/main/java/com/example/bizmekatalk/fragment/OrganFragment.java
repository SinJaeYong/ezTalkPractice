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
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.UserItem;

import java.util.List;

public class OrganFragment extends Fragment {
    private OrganAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        OrganFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.organ_fragment, container, false);

        binding.setOrganFragment(this);
        adapter = new OrganAdapter(getActivity());

        if(BizmekaApp.userMap.get(BizmekaApp.navi.getLast())!=null){
            binding.tvOrganTotalMember.setText(String.valueOf(BizmekaApp.userMap.get(BizmekaApp.navi.getLast()).size()));
        } else {
            binding.tvOrganTotalMember.setText("0");
        }

        if (adapter != null ){
            List<Item> items = BizmekaApp.userMap.get(BizmekaApp.navi.getLast());
            for(Item item : items){
                ((UserItem)item).setDeptName(BizmekaApp.COMPNAME);
            }
            adapter.setData( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );
            adapter.addData( BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()) );
            adapter.notifyDataSetChanged();
        }

        binding.btnDeptBack.setOnClickListener(v -> {
            if(BizmekaApp.navi.size() > 1)
                BizmekaApp.navi.removeLast();
            if(BizmekaApp.userMap.get(BizmekaApp.navi.getLast())!=null){
                binding.tvOrganTotalMember.setText(String.valueOf(BizmekaApp.userMap.get(BizmekaApp.navi.getLast()).size()));
            } else {
                binding.tvOrganTotalMember.setText("0");
            }
            adapter.clearData();
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
