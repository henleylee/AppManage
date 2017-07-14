package com.liyunlong.appmanage.utils;

import com.liyunlong.appmanage.data.AppInfo;

import java.util.Comparator;

/**
 * 按APP名称排序
 *
 * @author liyunlong
 * @date 2017/6/7 14:56
 */
public class ComparatorByName implements Comparator<AppInfo> {

    private boolean descOrder;

    public ComparatorByName(boolean descOrder) {
        this.descOrder = descOrder;
    }

    @Override
    public int compare(AppInfo o1, AppInfo o2) {
        int result = o1.getAppLabel().compareTo(o2.getAppLabel());
        if (descOrder) {
            result = 0 - result;
        }
        return result;
    }

}
