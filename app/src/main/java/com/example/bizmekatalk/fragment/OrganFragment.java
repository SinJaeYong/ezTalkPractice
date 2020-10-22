package com.example.bizmekatalk.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.activity.BizmekaApp;
import com.example.bizmekatalk.adapter.OrganAdapter;

import com.example.bizmekatalk.databinding.OrganFragmentBinding;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.UserItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrganFragment extends Fragment {
    private OrganAdapter adapter;
    private OrganFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.organ_fragment, container, false);

        adapter = new OrganAdapter(getActivity());

        //첫번째 네비게이션 클릭시
        binding.llDeptNaviFirst.setOnClickListener(v -> {
            clearNavi();
            updateData(BizmekaApp.userMap.get(BizmekaApp.navi.getLast()), BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()));
        });

        //부서원 수 표
        updateTotalMember( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );

        //첫 화면 표시
        if (adapter != null ){
            List<Item> items = BizmekaApp.userMap.get(BizmekaApp.navi.getLast());
            for(Item item : items){
                ((UserItem)item).setDeptName(BizmekaApp.COMPNAME);
            }
            adapter.setData( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );
            adapter.addData( BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()) );
            adapter.notifyDataSetChanged();
        }

        //부서트리 백키 눌렀을
        binding.btnDeptBack.setOnClickListener(v -> {
            if(BizmekaApp.navi.size() > 1){
                BizmekaApp.navi.removeLast();
                //binding.scDeptNavi.removeViewAt(BizmekaApp.navi.size());
                binding.llDeptNavi.removeViewAt(BizmekaApp.navi.size());
            }

            updateTotalMember( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );

            updateData( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()), BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()) );
        });

        binding.svOrganSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("jay.OrganFragment","query : "+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Item> userList = BizmekaApp.userList.stream().filter(item -> ((UserItem)item).getName().contains(newText)).collect(Collectors.toList());
                List<Item> deptList = BizmekaApp.deptList.stream().filter(item -> ((DeptItem)item).getDeptName().contains(newText)).collect(Collectors.toList());
                if("".equals(newText)){
                    clearNavi();
                    updateData(BizmekaApp.userMap.get(BizmekaApp.navi.getLast()), BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()));
                    updateTotalMember( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );
                } else{
                    updateData( userList, deptList );
                    updateTotalMember( userList );
                }
                return true;
            }
        });

        binding.lvDeptList.setAdapter(adapter);

        return binding.getRoot();
    }



    private void updateTotalMember(List<Item> members) {
        if (members != null) {
            binding.tvOrganTotalMember.setText(String.valueOf(members.size()));
        } else {
            binding.tvOrganTotalMember.setText("0");
        }
    }


    private void updateData(List<Item>...items) {
        adapter.clearData();
        for(List<Item> itemList : items){
            adapter.addData(itemList);
        }
        adapter.notifyDataSetChanged();
    }


    private void clearNavi() {
        int naviSize = BizmekaApp.navi.size();
        binding.llDeptNavi.removeViews(1,  naviSize - 1);
        for(int i = 0 ; i <  naviSize - 1 ; i++){
            BizmekaApp.navi.removeLast();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean chosungSearch(List<Item> list, String input) {

        return false;
    }

}
