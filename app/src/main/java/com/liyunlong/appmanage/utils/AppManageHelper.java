package com.liyunlong.appmanage.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.format.Formatter;

import com.liyunlong.appmanage.data.AppInfo;
import com.liyunlong.appmanage.data.AppManageInfo;
import com.liyunlong.appmanage.listener.OnAppInfoReadyListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * AppManage辅助类
 *
 * @author liyunlong
 * @date 2017/4/17 16:17
 */
public class AppManageHelper {

    private static final String METHOD_NAME_GETPACKAGESIZEINFO = "getPackageSizeInfo";
    private int totalCount = 0;
    private int readyCount = 0;
    private Context context;
    private PackageManager packageManager;
    private OnAppInfoReadyListener mListener;

    public AppManageHelper(Context context) {
        this.context = context;
        this.packageManager = context.getPackageManager();
    }

    public void setOnReadyListener(OnAppInfoReadyListener listener) {
        this.mListener = listener;
    }

    public AppManageInfo getAppManageInfo() {
        try {
            totalCount = 0;
            readyCount = 0;
            List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
            if (packageInfoList == null || packageInfoList.isEmpty()) {
                return null;
            }
            AppManageInfo appManageInfo = new AppManageInfo();
            for (PackageInfo packageInfo : packageInfoList) {
                AppInfo appInfo = getAppInfo(packageInfo);
                if (appInfo.isSystemApp()) {
                    appInfo.setIsSystemApp(true);
                    appManageInfo.addSystemAppInfo(appInfo);
                } else {
                    appInfo.setIsSystemApp(false);
                    appManageInfo.addUserAppInfo(appInfo);
                }
                appManageInfo.addAllAppInfo(appInfo);
                appManageInfo.putMapAppInfo(appInfo);
            }
            return appManageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AppInfo getAppInfo(String packageName) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return getAppInfo(packageInfo);
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private AppInfo getAppInfo(PackageInfo packageInfo) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        totalCount++;
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        AppInfo appInfo = new AppInfo();
        appInfo.setPackageName(applicationInfo.packageName);
        appInfo.setLocation(applicationInfo.sourceDir);
        appInfo.setAppIcon(applicationInfo.loadIcon(packageManager));
        appInfo.setAppLabel((String) applicationInfo.loadLabel(packageManager));
        appInfo.setVersionCode(packageInfo.versionCode);
        appInfo.setVersionName(packageInfo.versionName);
        appInfo.setIsSystemApp(isSystemApp(applicationInfo));
        if (packageInfo.signatures.length > 0) {
            appInfo.setSigmd5(MD5Helper.getMessageDigest(packageInfo.signatures[0].toByteArray()));
        }
        queryPacakgeSize(appInfo.getPackageName(), new PackageStatsObserver(appInfo));
        return appInfo;
    }

    /**
     * 获取App的缓存大小、数据大小、应用程序大小
     *
     * @param packageName
     * @param observer
     */
    private void queryPacakgeSize(String packageName, IPackageStatsObserver observer) {
        try {
            Method method = packageManager.getClass().getMethod(METHOD_NAME_GETPACKAGESIZEINFO,
                    String.class, int.class, IPackageStatsObserver.class);
            method.invoke(packageManager, packageName, Process.myUid() / 100000, observer);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private boolean isSystemApp(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private String formateFileSize(long sizeBytes) {
        return Formatter.formatFileSize(context, sizeBytes);
    }

    private class PackageStatsObserver extends IPackageStatsObserver.Stub {

        private AppInfo appInfo;

        PackageStatsObserver(AppInfo appInfo) {
            this.appInfo = appInfo;
        }

        @Override
        public void onGetStatsCompleted(PackageStats packageStats, boolean succeeded) throws RemoteException {
            if (appInfo != null) {
                readyCount++;
                appInfo.setCodeSize(formateFileSize(packageStats.codeSize + packageStats.externalCodeSize)); // 应用程序大小
                appInfo.setDataSize(formateFileSize(packageStats.dataSize + packageStats.externalDataSize)); // 数据大小
                appInfo.setCacheSize(formateFileSize(packageStats.cacheSize + packageStats.externalCacheSize)); // 缓存大小
                long totalSizeBytes = packageStats.codeSize + packageStats.externalCodeSize
                        + packageStats.dataSize + packageStats.externalDataSize
                        + packageStats.cacheSize + packageStats.externalCacheSize;
                appInfo.setTotalSizeBytes(totalSizeBytes);
                appInfo.setTotalSize(formateFileSize(totalSizeBytes)); // 总大小
                if (readyCount == totalCount && mListener != null) {
                    mListener.onAppInfoReady();
                }
            }
        }
    }
}
