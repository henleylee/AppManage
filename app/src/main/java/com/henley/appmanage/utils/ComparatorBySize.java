package com.henley.appmanage.utils;

import com.henley.appmanage.data.AppInfo;

import java.util.Comparator;

/**
 * 按APP大小排序
 *
 * @author Henley
 * @date 2017/6/7 14:56
 */
public class ComparatorBySize implements Comparator<AppInfo> {

    private boolean descOrder;

    public ComparatorBySize(boolean descOrder) {
        this.descOrder = descOrder;
    }

    @Override
    public int compare(AppInfo o1, AppInfo o2) {
        long totalSize1 = o1.getTotalSize();
        long totalSize2 = o2.getTotalSize();
        int result;
        if (totalSize1 < totalSize2) {
            result = -1;
        } else if (totalSize1 > totalSize2) {
            result = 1;
        } else {
            result = 0;
        }
        if (descOrder) {
            result = 0 - result;
        }
        return result;
    }

}
