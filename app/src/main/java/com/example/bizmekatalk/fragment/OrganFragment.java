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
import com.example.bizmekatalk.activity.BizmekaApp;
import com.example.bizmekatalk.adapter.OrganAdapter;

import com.example.bizmekatalk.databinding.OrganFragmentBinding;
import com.example.bizmekatalk.fragment.sublayout.SubNaviLayout;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.NaviItem;
import com.example.bizmekatalk.items.UserItem;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class OrganFragment extends Fragment {
    private OrganAdapter adapter;
    private OrganFragmentBinding binding;

    private LinkedList<NaviItem> navi;
    private List<Item> deptList;
    private List<Item> userList;

    private boolean searchFlag;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initData(context);
        adapter.updateItems(userList, deptList);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.organ_fragment, container, false);
        for( int i = 0 ; i < navi.size() ; i++){
            setDeptNavigationView( i, navi.get(i).getDeptName() );
        }
        setNumberOfUsers(userList);
        setOnDeptBackBotton();
        setSearchTextListener();
        binding.lvDeptList.setAdapter(adapter);
        return binding.getRoot();
    }



    private void initData(@NonNull Context context) {
        adapter = new OrganAdapter(context);
        navi = BizmekaApp.navi;
        resetLists();
        setUserDeptName( navi.getLast().getDeptName() );
    }

    private void setUserDeptName(String userDeptName) {
        if(userList != null){
            userList.stream().forEach(item->((UserItem)item).setDeptName( userDeptName ));
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
            int naviPosition = navi.size() - 1;
            clearNavi( naviPosition, binding.llDeptNavi );
            setNumberOfUsers(userList);
            adapter.updateItems(userList, deptList);
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
                List<Item> searchedUserList = BizmekaApp.userList.stream().filter(item -> ((UserItem)item).getName().contains(newText)).collect(Collectors.toList());
                List<Item> searchedDeptList = BizmekaApp.deptList.stream().filter(item -> ((DeptItem)item).getDeptName().contains(newText)).collect(Collectors.toList());
                if("".equals(newText)){
                    if(searchFlag){
                        clearNavi(1, binding.llDeptNavi );
                        adapter.updateItems(userList, deptList);
                        setNumberOfUsers(userList);
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
        int naviNum = position + 1;
        childLayout.setOnClickListener(v1 -> {
            clearNavi( naviNum, binding.llDeptNavi );
            setNumberOfUsers(userList);
            adapter.updateItems(userList, deptList);
        });

        TextView tvSubDeptName = childLayout.findViewById(R.id.tvSubDeptName);
        tvSubDeptName.setText( deptName );
        binding.llDeptNavi.addView( childLayout );
    }


    private void clearNavi(int startNum ,LinearLayout ll) {
        if(navi.size()>1){
            int endNum = navi.size();
            ll.removeViews(startNum,  endNum - startNum);
            for(int i = 0 ; i <  endNum - startNum ; i++){
                navi.removeLast();
            }
            resetLists();
        }
    }


    private void resetLists(){
        deptList = BizmekaApp.deptMap.get( navi.getLast().getDeptId() );
        userList = BizmekaApp.userMap.get( navi.getLast().getDeptId() );
    }
}
