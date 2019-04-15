package com.henley.appmanage.listener;

/**
 * @author Henley
 * @date 2017/4/18 11:18
 */
public interface OnInstallStateChangedListener {

    /**
     * 当安装包安装成功时回调
     *
     * @param action
     * @param packageName 包名
     */
    void onPackageAdded(String action, String packageName);

    /**
     * 当安装包替换成功时回调
     *
     * @param action
     * @param packageName 包名
     */
    void onPackageReplaced(String action, String packageName);

    /**
     * 当安装包卸载成功时回调
     *
     * @param action
     * @param packageName 包名
     */
    void onPackageRemoved(String action, String packageName);
}
