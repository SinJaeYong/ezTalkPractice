package com.example.bizmekatalk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bizmekatalk.R;

public class ChatFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i("jay.ChatFragment","onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("jay.ChatFragment","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("jay.ChatFragment","onCreateView");
        return inflater.inflate(R.layout.chat_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("jay.ChatFragment","onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("jay.ChatFragment","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("jay.ChatFragment","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("jay.ChatFragment","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("jay.ChatFragment","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("jay.ChatFragment","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("jay.ChatFragment","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("jay.ChatFragment","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("jay.ChatFragment","onDetach");
    }


}
