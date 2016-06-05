package com.bwelco.bwelcosearch;

import android.util.ArrayMap;

import java.util.List;
import java.util.Map;

/**
 * Created by bwelco on 2016/6/5.
 */
public class SearchList {


    Map<String, List<String>> map;

    public SearchList() {
        this.map = new ArrayMap<String, List<String>>();
    }

    public Map<String, List<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<String>> map) {
        this.map = map;
    }
}
