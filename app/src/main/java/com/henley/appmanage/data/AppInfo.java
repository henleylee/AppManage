package com.henley.appmanage.data;

import android.graphics.drawable.Drawable;

/**
 * App信息
 *
 * @author Henley
 * @date 2017/4/17 15:01
 */
public class AppInfo {

    private Drawable appIcon;
    private String appName;
    private long cacheSize;
    private long codeSize;
    private long dataSize;
    private long totalSize;
    private boolean isSystemApp;
    private String location;
    private int versionCode;
    private String versionName;
    private String packageName;
    private String signatureMD5;
    private String signatureSHA1;
    private String signatureSHA256;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getCodeSize() {
        return codeSize;
    }

    public void setCodeSize(long codeSize) {
        this.codeSize = codeSize;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void setSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    public Boolean isSystemApp() {
        return isSystemApp;
    }

    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSignatureMD5() {
        return signatureMD5;
    }

    public void setSignatureMD5(String signatureMD5) {
        this.signatureMD5 = signatureMD5;
    }

    public String getSignatureSHA1() {
        return signatureSHA1;
    }

    public void setSignatureSHA1(String signatureSHA1) {
        this.signatureSHA1 = signatureSHA1;
    }

    public String getSignatureSHA256() {
        return signatureSHA256;
    }

    public void setSignatureSHA256(String signatureSHA256) {
        this.signatureSHA256 = signatureSHA256;
    }

}
