package com.henley.appmanage.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * App信息管理
 *
 * @author Henley
 * @date 2017/4/17 16:10
 */
public class AppInfoManage {

    private List<AppInfo> allAppList = new ArrayList<>();
    private List<AppInfo> userAppList = new ArrayList<>();
    private List<AppInfo> systemAppList = new ArrayList<>();
    private HashMap<String, AppInfo> packageMap = new HashMap<>();

    public List<AppInfo> getAllAppList() {
        return allAppList;
    }

    public boolean removeAllAppInfo(AppInfo appInfo) {
        return allAppList.remove(appInfo);
    }

    public boolean addAllAppInfo(AppInfo appInfo) {
        return allAppList.add(appInfo);
    }

    public List<AppInfo> getUserAppList() {
        return userAppList;
    }

    public boolean removeUserAppInfo(AppInfo appInfo) {
        return userAppList.remove(appInfo);
    }

    public boolean addUserAppInfo(AppInfo appInfo) {
        return userAppList.add(appInfo);
    }

    public List<AppInfo> getSystemAppList() {
        return systemAppList;
    }

    public boolean removeSystemAppInfo(AppInfo appInfo) {
        return systemAppList.remove(appInfo);
    }

    public boolean addSystemAppInfo(AppInfo appInfo) {
        return systemAppList.add(appInfo);
    }

    public HashMap<String, AppInfo> getPackageMap() {
        return packageMap;
    }

    public AppInfo getMapAppInfo(String packageName) {
        return packageMap.get(packageName);
    }

    public AppInfo putMapAppInfo(AppInfo appInfo) {
        return packageMap.put(appInfo.getPackageName(), appInfo);
    }

    public AppInfo removeMapAppInfo(String packageName) {
        return packageMap.remove(packageName);
    }

}
