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
import android.content.pm.Signature;
import android.os.Build;
import android.os.storage.StorageManager;

import androidx.annotation.NonNull;

import com.henley.appmanage.data.AppInfo;
import com.henley.appmanage.data.AppInfoManage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private Context context;
    private PackageManager packageManager;
    private StorageManager storageManager;
    private StorageStatsManager storageStatsManager;

    public AppManageHelper(Context context) {
        this.context = context;
        this.packageManager = context.getPackageManager();
    }

    public AppInfoManage getAppManageInfo() {
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
        if (packageInfos == null || packageInfos.isEmpty()) {
            return null;
        }
        AppInfoManage appInfoManage = new AppInfoManage();
        for (PackageInfo packageInfo : packageInfos) {
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
    }

    public AppInfo getAppInfo(String packageName) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return getAppInfo(packageInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private AppInfo getAppInfo(PackageInfo packageInfo) {
        AppInfo appInfo = new AppInfo();
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        appInfo.setPackageName(applicationInfo.packageName);
        appInfo.setLocation(applicationInfo.sourceDir);
        appInfo.setAppIcon(applicationInfo.loadIcon(packageManager));
        appInfo.setAppName((String) applicationInfo.loadLabel(packageManager));
        appInfo.setVersionCode(packageInfo.versionCode);
        appInfo.setVersionName(packageInfo.versionName);
        appInfo.setIsSystemApp(isSystemApp(applicationInfo));
        Signature[] signatures = packageInfo.signatures;
        if (signatures.length > 0) {
            byte[] signBytes = signatures[0].toByteArray();
            appInfo.setSignatureMD5(DigestHelper.encodeMD5Hex(signBytes));
            appInfo.setSignatureSHA1(DigestHelper.encodeSHAHex(signBytes));
            appInfo.setSignatureSHA256(DigestHelper.encodeSHA256Hex(signBytes));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAppTotalSizeO(packageInfo, appInfo);
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
    private void getAppTotalSizeO(PackageInfo packageInfo, AppInfo appInfo) {
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
        public void onGetStatsCompleted(PackageStats packageStats, boolean succeeded) {
            if (appInfo != null) {
                appInfo.setCodeSize(packageStats.codeSize + packageStats.externalCodeSize); // 应用程序大小
                appInfo.setDataSize(packageStats.dataSize + packageStats.externalDataSize); // 数据大小
                appInfo.setCacheSize(packageStats.cacheSize + packageStats.externalCacheSize); // 缓存大小
                long totalSizeBytes = packageStats.codeSize + packageStats.externalCodeSize
                        + packageStats.dataSize + packageStats.externalDataSize
                        + packageStats.cacheSize + packageStats.externalCacheSize;
                appInfo.setTotalSize(totalSizeBytes); // 总大小
            }
        }
    }
}
