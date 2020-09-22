package com.helper;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Naresh on 12-Jan-17.
 */

public class IntentDataObject implements Serializable {

    public static final String OBJ = "obj";
    private Object obj = null;
    private HashMap<String,String> map;
    private HashMap<String,String> filterStateMap;

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public HashMap<String, String> getFilterStateMap() {
        return filterStateMap;
    }

    public void setFilterStateMap(HashMap<String, String> filterStateMap) {
        this.filterStateMap = filterStateMap;
    }

    public IntentDataObject(){
        map = new HashMap<>();
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public HashMap<String, String> getData() {
        return map;
    }

    public void putData(String key,String value) {
        map.put(key,value);
    }
    public void setHashMapData(String key,HashMap<String,String> value) {
        map = value;
    }
}
