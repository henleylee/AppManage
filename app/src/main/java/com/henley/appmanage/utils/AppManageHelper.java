package com.henley.appmanage.utils;

import android.annotation.TargetApi;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.RemoteException;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;

import com.henley.appmanage.data.AppInfo;
import com.henley.appmanage.data.AppInfoManage;
import com.henley.appmanage.listener.OnAppInfoReadyListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * AppManage辅助类
 *
 * @author Henley
 * @date 2017/4/17 16:17
 */
public class AppManageHelper {

    private static final String METHOD_NAME_GETPACKAGESIZEINFO = "getPackageSizeInfo";
    private int totalCount = 0;
    private int readyCount = 0;
    private Context context;
    private PackageManager packageManager;
    private StorageManager storageManager;
    private StorageStatsManager storageStatsManager;
    private OnAppInfoReadyListener mListener;

    public AppManageHelper(Context context) {
        this.context = context;
        this.packageManager = context.getPackageManager();
    }

    public void setOnReadyListener(OnAppInfoReadyListener listener) {
        this.mListener = listener;
    }

    public AppInfoManage getAppManageInfo() {
        try {
            totalCount = 0;
            readyCount = 0;
            List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
            if (packageInfoList == null || packageInfoList.isEmpty()) {
                return null;
            }
            AppInfoManage appInfoManage = new AppInfoManage();
            for (PackageInfo packageInfo : packageInfoList) {
                AppInfo appInfo = getAppInfo(packageInfo);
                if (appInfo.isSystemApp()) {
                    appInfo.setIsSystemApp(true);
                    appInfoManage.addSystemAppInfo(appInfo);
                } else {
                    appInfo.setIsSystemApp(false);
                    appInfoManage.addUserAppInfo(appInfo);
                }
                appInfoManage.addAllAppInfo(appInfo);
                appInfoManage.putMapAppInfo(appInfo);
            }
            return appInfoManage;
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
        AppInfo appInfo = new AppInfo();
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        appInfo.setPackageName(applicationInfo.packageName);
        appInfo.setLocation(applicationInfo.sourceDir);
        appInfo.setAppIcon(applicationInfo.loadIcon(packageManager));
        appInfo.setAppLabel((String) applicationInfo.loadLabel(packageManager));
        appInfo.setVersionCode(packageInfo.versionCode);
        appInfo.setVersionName(packageInfo.versionName);
        appInfo.setIsSystemApp(isSystemApp(applicationInfo));
        if (packageInfo.signatures.length > 0) {
            byte[] signBytes = packageInfo.signatures[0].toByteArray();
            appInfo.setSignatureMD5(DigestHelper.encodeMD5Hex(signBytes));
            appInfo.setSignatureSHA1(DigestHelper.encodeSHAHex(signBytes));
            appInfo.setSignatureSHA256(DigestHelper.encodeSHA256Hex(signBytes));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAppTotalsizeO(packageInfo, appInfo);
        } else {
            queryPacakgeSize(appInfo.getPackageName(), new PackageStatsObserver(appInfo));
        }
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
                    String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, packageName, observer);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取App的缓存大小、数据大小、应用程序大小
     *
     * @param packageInfo
     * @param appInfo
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void getAppTotalsizeO(PackageInfo packageInfo, AppInfo appInfo) {
        try {
            if (storageManager == null) {
                storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            }
            if (storageStatsManager == null) {
                storageStatsManager = (StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
            }
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            UUID uuid = applicationInfo.storageUuid;
            int uid = applicationInfo.uid;
            StorageStats storageStats = storageStatsManager.queryStatsForUid(uuid, uid);
            appInfo.setCodeSize(storageStats.getAppBytes()); // 应用程序大小
            appInfo.setDataSize(storageStats.getDataBytes()); // 数据大小
            appInfo.setCacheSize(storageStats.getCacheBytes()); // 缓存大小
            long totalSizeBytes = storageStats.getAppBytes() + storageStats.getDataBytes() + storageStats.getCacheBytes();
            appInfo.setTotalSize(totalSizeBytes); // 总大小
            if (readyCount == totalCount && mListener != null) {
                mListener.onAppInfoReady();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isSystemApp(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
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
                appInfo.setCodeSize(packageStats.codeSize + packageStats.externalCodeSize); // 应用程序大小
                appInfo.setDataSize(packageStats.dataSize + packageStats.externalDataSize); // 数据大小
                appInfo.setCacheSize(packageStats.cacheSize + packageStats.externalCacheSize); // 缓存大小
                long totalSizeBytes = packageStats.codeSize + packageStats.externalCodeSize
                        + packageStats.dataSize + packageStats.externalDataSize
                        + packageStats.cacheSize + packageStats.externalCacheSize;
                appInfo.setTotalSize(totalSizeBytes); // 总大小
                if (readyCount == totalCount && mListener != null) {
                    mListener.onAppInfoReady();
                }
            }
        }
    }
}
