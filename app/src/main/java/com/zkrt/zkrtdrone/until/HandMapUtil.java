package com.zkrt.zkrtdrone.until;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jack_xie on 16-12-23.
 */

public class HandMapUtil {
    /**
     * 根据map获取map包含的key,返回set
     * @param map
     * @return
     */
    public static Set<String> getKeySetByMap(Map<String, String> map) {
        Set<String> keySet = new HashSet<String>();
        keySet.addAll(map.keySet());
        return keySet;
    }

    /**
     * 根据key的set返回key的list
     *
     * @param set
     * @return
     */
    public static List<String> getKeyListBySet(Set<String> set) {
        List<String> keyList = new ArrayList<String>();
        keyList.addAll(set);
        return keyList;
    }

    /**
     * 根据map返回key和value的list
     *
     * @param map
     * @param isKey
     *            true为key,false为value
     * @return
     */
    public static List<String> getListByMap(Map<String, String> map,
                                            boolean isKey) {
        List<String> listadd = new LinkedList<>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            if (isKey) {
                listadd.add(key);
            } else {
                listadd.add(map.get(key));
            }
        }
        return listadd;
    }

}
