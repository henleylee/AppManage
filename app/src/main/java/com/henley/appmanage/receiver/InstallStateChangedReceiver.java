package com.henley.appmanage.receiver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.henley.appmanage.listener.OnInstallStateChangedListener;

/**
 * 安装包状态广播接收器
 * <ul>
 * <strong>注意：</strong>
 * <li>需要权限{@link Manifest.permission#RESTART_PACKAGES}
 * </ul>
 *
 * @author Henley
 * @date 2016/7/11 12:59
 */
public class InstallStateChangedReceiver extends BroadcastReceiver {

    private static final String TAG = "InstallStateChangedReceiver";
    private OnInstallStateChangedListener mListener;

    public InstallStateChangedReceiver(OnInstallStateChangedListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        String packageName = intent.getData().getSchemeSpecificPart();
        if (Intent.ACTION_PACKAGE_ADDED.equals(action)) { // 一个新应用包已经安装在设备上，数据包括包名
            logger("安装包状态发生改变，" + packageName + " 安装成功!");
            if (mListener != null) {
                mListener.onPackageAdded(action, packageName);
            }
        } else if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) { // 一个新版本的应用安装到设备，替换之前已经存在的版本
            logger("安装包状态发生改变，" + packageName + " 替换成功!");
            if (mListener != null) {
                mListener.onPackageReplaced(action, packageName);
            }
        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) { // 一个已存在的应用程序包已经从设备上移除，包括包名
            logger("安装包状态发生改变，" + packageName + " 卸载成功!");
            if (mListener != null) {
                mListener.onPackageRemoved(action, packageName);
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void logger(String msg) {
        Log.i(TAG, msg);
    }
}
