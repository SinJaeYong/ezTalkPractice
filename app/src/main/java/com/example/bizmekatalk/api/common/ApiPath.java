package com.example.bizmekatalk.api.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.Getter;

@Getter
public class ApiPath {

    private LinkedList<String> pathList = new LinkedList<String>();

    public ApiPath(String ... pathNames) {
        Collections.addAll(pathList, pathNames);
    }

    public ApiPath replace(String ... pathNames){
        Collections.addAll(pathList, pathNames);
        return this;
    }

    public ApiPath push(String path){
        pathList.push(path);
        return this;
    }

    public ApiPath pop(){
        pathList.pop();
        return this;
    }

    public ApiPath clear(){
        pathList.clear();
        return this;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer("");
        pathList.stream().forEach(path->buf.append(path.trim()+"/"));
        buf.deleteCharAt(buf.length());
        return ( buf.length() == 0 ) ? "" : buf.deleteCharAt(buf.length()-1).toString();
    }
}
