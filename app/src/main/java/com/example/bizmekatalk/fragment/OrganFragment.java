package com.example.bizmekatalk.fragment;

import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrganFragment extends Fragment {
    private OrganAdapter adapter;
    private OrganFragmentBinding binding;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adapter = new OrganAdapter(context);
        Log.i("jay.OrganFragment","userMap : "+BizmekaApp.userMap.keySet().toString());
        Log.i("jay.OrganFragment","navi : "+BizmekaApp.navi.getLast());

        adapter.clearData();
        List<Item> items = BizmekaApp.userMap.get(BizmekaApp.navi.getLast());
        if(items != null){
            for (Item item : items) {
                ((UserItem) item).setDeptName(BizmekaApp.COMPNAME);
            }
        }
        adapter.addData(items);
        adapter.addData(BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()));
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.organ_fragment, container, false);
        //첫번째 네비게이션 클릭시
        binding.llDeptNaviFirst.setOnClickListener(v -> {
            Log.i("jay.OrganFragment","onClick");
            clearNavi();
            updateData(BizmekaApp.userMap.get(BizmekaApp.navi.getLast()), BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()));
        });

        //부서원 수 표시
        updateTotalMember( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );

        //부서 백키 눌렀을 때
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
                    Log.i("jay.OrganFragment","textChange");
                    /*
                    clearNavi();
                    updateData(BizmekaApp.userMap.get(BizmekaApp.navi.getLast()), BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()));
                    updateTotalMember( BizmekaApp.userMap.get(BizmekaApp.navi.getLast()) );

                     */
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //clearNavi();
    }
}
