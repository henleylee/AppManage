package com.henley.appmanage.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

/**
 * 权限请求辅助类
 *
 * @author Henley
 * @date 2018/10/8 18:36
 */
public class PermissionHelper {

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkUsagePermission(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return true;
        }
        boolean granted = false;
        AppOpsManager opsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
        if (opsManager != null) {
            int mode = opsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), activity.getPackageName());
            if (mode == AppOpsManager.MODE_DEFAULT) {
                granted = activity.checkSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED;
            } else {
                granted = mode == AppOpsManager.MODE_ALLOWED;
            }
        }
        return granted;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void openUsagePermissionSetting(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
    }

}
