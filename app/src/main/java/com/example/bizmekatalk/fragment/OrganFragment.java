package com.example.bizmekatalk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.activity.Bizmeka;
import com.example.bizmekatalk.adapter.OrganAdapter;

import com.example.bizmekatalk.databinding.OrganFragmentBinding;
import com.example.bizmekatalk.fragment.sublayout.SubNaviLayout;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.NaviItem;
import com.example.bizmekatalk.items.UserItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrganFragment extends Fragment {
    private OrganAdapter adapter;
    private OrganFragmentBinding binding;
    private List<Item> deptLeafList;
    private List<Item> userLeafList;

    private boolean searchFlag;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initData();
        adapter = new OrganAdapter(context);
        adapter.updateItems(userLeafList, deptLeafList);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.organ_fragment, container, false);
        for(int i = 0; i < Bizmeka.navi.size() ; i++){
            setDeptNavigationView( i, Bizmeka.navi.get(i).getDeptName() );
        }
        setNumberOfUsers(userLeafList);
        setOnDeptBackBotton();
        setSearchTextListener();
        binding.lvDeptList.setAdapter(adapter);
        return binding.getRoot();
    }


    private void initData() {
        allItemListToMap();
        resetItemLeafList();
        setUserDeptName( Bizmeka.navi.getLast().getDeptName() );
    }


    private void allItemListToMap() {
        Bizmeka.deptMap = Bizmeka.deptList.stream().collect(Collectors.groupingBy(item -> ((DeptItem)item).getParentDeptId()));
        Bizmeka.userMap = Bizmeka.userList.stream().collect(Collectors.groupingBy(item -> ((UserItem)item).getDeptId()));
    }


    private void resetItemLeafList(){
        deptLeafList = Bizmeka.deptMap.get( Bizmeka.navi.getLast().getDeptId() );
        userLeafList = Bizmeka.userMap.get( Bizmeka.navi.getLast().getDeptId() );
    }


    private void setUserDeptName(String userDeptName) {
        if(userLeafList != null){
            userLeafList.stream().forEach(item->((UserItem)item).setDeptName( userDeptName ));
        }
    }


    private void setNumberOfUsers(List<Item> userList) {
        if (userList != null) {
            binding.tvOrganTotalMember.setText(String.valueOf( userList.size() ));
        } else {
            binding.tvOrganTotalMember.setText("0");
        }
    }


    private void setOnDeptBackBotton() {
        binding.btnDeptBack.setOnClickListener(v -> {
            int naviPosition = Bizmeka.navi.size() - 1;
            clearNavi( naviPosition, binding.llDeptNavi );
            setNumberOfUsers(userLeafList);
            adapter.updateItems(userLeafList, deptLeafList);
        });
    }


    private void setSearchTextListener() {
        searchFlag = false;

        binding.svOrganSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Item> searchedUserList = Bizmeka.userList.stream().filter(item -> ((UserItem)item).getName().contains(newText)).collect(Collectors.toList());
                List<Item> searchedDeptList = Bizmeka.deptList.stream().filter(item -> ((DeptItem)item).getDeptName().contains(newText)).collect(Collectors.toList());
                if("".equals(newText)){
                    if(searchFlag){
                        clearNavi(1, binding.llDeptNavi );
                        adapter.updateItems(userLeafList, deptLeafList);
                        setNumberOfUsers(userLeafList);
                    }
                } else{
                    searchFlag = true;
                    adapter.updateItems( searchedUserList, searchedDeptList );
                    setNumberOfUsers( searchedUserList );
                }
                return true;
            }
        });
    }


    private void setDeptNavigationView(int position, String deptName) {
        SubNaviLayout childLayout = new SubNaviLayout( this.getContext() );
        childLayout.setOnClickListener(v1 -> {
            clearNavi( position + 1, binding.llDeptNavi );
            setNumberOfUsers(userLeafList);
            adapter.updateItems(userLeafList, deptLeafList);
        });

        TextView tvSubDeptName = childLayout.findViewById(R.id.tvSubDeptName);
        if(tvSubDeptName != null){
            tvSubDeptName.setText( deptName );
        }
        binding.llDeptNavi.addView( childLayout );
    }


    private void clearNavi(int startNum ,LinearLayout ll) {
        if(Bizmeka.navi.size()>1){
            int endNum = Bizmeka.navi.size();
            if( ll != null ){
                ll.removeViews(startNum,  endNum - startNum);
            }
            for(int i = 0 ; i <  endNum - startNum ; i++){
                Bizmeka.navi.removeLast();
            }
            resetItemLeafList();
        }
    }



}
