package com.liyunlong.appmanage.utils;

import com.liyunlong.appmanage.data.AppInfo;

import java.util.Comparator;

/**
 * 按APP大小排序
 *
 * @author liyunlong
 * @date 2017/6/7 14:56
 */
public class ComparatorBySize implements Comparator<AppInfo> {

    private boolean descOrder;

    public ComparatorBySize(boolean descOrder) {
        this.descOrder = descOrder;
    }

    @Override
    public int compare(AppInfo o1, AppInfo o2) {
        long totalSizeBytes1 = o1.getTotalSizeBytes();
        long totalSizeBytes2 = o2.getTotalSizeBytes();
        int result;
        if (totalSizeBytes1 < totalSizeBytes2) {
            result = -1;
        } else if (totalSizeBytes1 > totalSizeBytes2) {
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
