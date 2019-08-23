package com.henley.appmanage.utils;

import com.henley.appmanage.data.AppInfo;

import java.util.Comparator;

/**
 * 按APP名称排序
 *
 * @author Henley
 * @date 2017/6/7 14:56
 */
public class ComparatorByName implements Comparator<AppInfo> {

    private boolean descOrder;

    public ComparatorByName(boolean descOrder) {
        this.descOrder = descOrder;
    }

    @Override
    public int compare(AppInfo o1, AppInfo o2) {
        int result = o1.getAppName().compareTo(o2.getAppName());
        if (descOrder) {
            result = 0 - result;
        }
        return result;
    }

}
