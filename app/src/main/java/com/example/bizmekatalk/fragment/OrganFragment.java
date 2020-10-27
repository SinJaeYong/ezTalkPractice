package com.example.bizmekatalk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Map;
import java.util.stream.Collectors;

public class OrganFragment extends Fragment {
    private OrganAdapter adapter;
    private OrganFragmentBinding binding;

    private LinkedList<NaviItem> navi;
    private Map<String, List<Item>> deptMap;
    private Map<String, List<Item>> userMap;
    private List<Item> deptList;
    private List<Item> userList;

    private boolean searchFlag;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        navi = BizmekaApp.navi;
        deptMap = BizmekaApp.deptMap;
        userMap = BizmekaApp.userMap;
        deptList = BizmekaApp.deptMap.get( navi.getLast().getDeptId() );
        userList = BizmekaApp.userMap.get( navi.getLast().getDeptId() );

        adapter = new OrganAdapter(context);



        List<Item> items = userMap.get( navi.getLast().getDeptId() );
        if(items != null){
            for (Item item : items) {
                ((UserItem) item).setDeptName( navi.getLast().getDeptName() );
            }
        }

        adapter.clearData();
        adapter.addData( items );
        adapter.addData( deptMap.get( navi.getLast().getDeptId() ) );
        adapter.notifyDataSetChanged();

        searchFlag = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.organ_fragment, container, false);


        //네비게이션 표시
        for( int i = 0 ; i < navi.size() ; i++){
            SubNaviLayout childLayout = new SubNaviLayout( this.getContext() );
            int naviNum = i + 1;
            childLayout.setOnClickListener(v1 -> {
                clearNavi( naviNum );
                updateTotalMember( userMap.get( navi.getLast().getDeptId() ) );
                updateData( userMap.get( navi.getLast().getDeptId() ), deptMap.get( navi.getLast().getDeptId() ) );
            });

            TextView tvSubDeptName = childLayout.findViewById(R.id.tvSubDeptName);
            tvSubDeptName.setText( navi.get(i).getDeptName() );
            binding.llDeptNavi.addView( childLayout );
        }



        //부서원 수 표시
        updateTotalMember( userMap.get( navi.getLast().getDeptId() ) );


        //부서 백키 눌렀을 때
        binding.btnDeptBack.setOnClickListener(v -> {
            if( navi.size() > 1 ){
                navi.removeLast();
                binding.llDeptNavi.removeViewAt( navi.size() );
            }

            updateTotalMember( userMap.get( navi.getLast().getDeptId() ) );

            updateData( userMap.get( navi.getLast().getDeptId() ), deptMap.get( navi.getLast().getDeptId() ) );
        });

        binding.svOrganSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Item> userList = BizmekaApp.userList.stream().filter(item -> ((UserItem)item).getName().contains(newText)).collect(Collectors.toList());
                List<Item> deptList = BizmekaApp.deptList.stream().filter(item -> ((DeptItem)item).getDeptName().contains(newText)).collect(Collectors.toList());
                if("".equals(newText)){
                    if(searchFlag){
                        clearNavi(1);
                        updateData(userMap.get(navi.getLast().getDeptId()), deptMap.get(navi.getLast().getDeptId()));
                        updateTotalMember( userMap.get(navi.getLast().getDeptId()) );
                    }
                } else{
                    searchFlag = true;
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
            binding.tvOrganTotalMember.setText(String.valueOf( members.size() ));
        } else {
            binding.tvOrganTotalMember.setText("0");
        }
    }


    private void updateData(List<Item>...items) {
        adapter.clearData();
        for(List<Item> itemList : items){
            adapter.addData( itemList );
        }
        adapter.notifyDataSetChanged();
    }


    private void clearNavi( int startNum ) {
        int endNum = navi.size();
        binding.llDeptNavi.removeViews(startNum,  endNum - startNum);
        for(int i = 0 ; i <  endNum - startNum ; i++){
            navi.removeLast();
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
