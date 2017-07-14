package com.liyunlong.appmanage.activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.common.design.MaterialDialog;
import com.liyunlong.appmanage.R;
import com.liyunlong.appmanage.adapter.AppInfoAdapter;
import com.liyunlong.appmanage.adapter.FragmentAdapter;
import com.liyunlong.appmanage.data.AppInfo;
import com.liyunlong.appmanage.data.AppManageInfo;
import com.liyunlong.appmanage.fragment.AppInfoFragment;
import com.liyunlong.appmanage.listener.OnAppInfoReadyListener;
import com.liyunlong.appmanage.listener.OnInstallStateChangedListener;
import com.liyunlong.appmanage.receiver.InstallStateChangedReceiver;
import com.liyunlong.appmanage.utils.AppManageHelper;
import com.liyunlong.appmanage.utils.ComparatorByName;
import com.liyunlong.appmanage.utils.ComparatorBySize;
import com.liyunlong.appmanage.utils.NavigatorHelper;
import com.liyunlong.appmanage.utils.SearchViewHelper;
import com.liyunlong.appmanage.utils.Utility;
import com.liyunlong.appmanage.widget.LoadingDialog;
import com.liyunlong.appmanage.widget.indicator.CommonNavigator;
import com.liyunlong.appmanage.widget.indicator.MagicIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends FragmentActivity implements OnInstallStateChangedListener, OnAppInfoReadyListener, View.OnClickListener {

    private static final String SP_NAME = "appmanage_prefs";
    private static final String CURRENT_WHICH = "current_Which";
    private static final String[] TITLES = {"所有", "系统", "用户"};
    private static final String[] SORT_MENU_ITEMS = new String[]{"按名称升序", "按名称降序", "按大小升序", "按大小降序"};
    private ExecutorService mExecutorService;
    private AppManageInfo appManageInfo;
    private List<AppInfoFragment> fragments;
    private Dialog mProgressDialog;
    private InstallStateChangedReceiver receiver;
    private SharedPreferences mPreferences;
    private int curWhich;
    private int tempWhich;
    private AppManageHelper appManageHelper;
    private ListView listSearch;
    private AppInfoAdapter mAdapter;
    private EditText edtSeatch;
    private View searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        int length = TITLES.length;
        final List<String> titles = Arrays.asList(TITLES);
        fragments = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            fragments.add(new AppInfoFragment());
        }
        mViewPager.setOffscreenPageLimit(length);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), titles, fragments));

        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magicindicator);
        CommonNavigator commonNavigator = NavigatorHelper.getCommonNavigator(this, mViewPager, titles);
        magicIndicator.setNavigator(commonNavigator);
        magicIndicator.setupWithViewPager(mViewPager);
        mPreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mProgressDialog = new LoadingDialog(this);
        initSearchView();
        registerReceiver();
        loadAppManageInfo();

    }

    private void initSearchView() {
        FrameLayout contentView = (FrameLayout) getWindow().getDecorView();
        searchView = getLayoutInflater().inflate(R.layout.layout_appinfo_search, null);
        contentView.addView(searchView);
        int statusBarHeight = Utility.getStatusBarHeight(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) searchView.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
        searchView.setLayoutParams(layoutParams);
        searchView.setVisibility(View.INVISIBLE);
        edtSeatch = (EditText) searchView.findViewById(R.id.search_edt);
        final View ivClean = searchView.findViewById(R.id.search_clean);
        ivClean.setVisibility(View.GONE);
        ivClean.setOnClickListener(this);
        searchView.findViewById(R.id.search_back).setOnClickListener(this);
        searchView.findViewById(R.id.search_cancle).setOnClickListener(this);
        edtSeatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    ivClean.setVisibility(View.VISIBLE);
                    startQuery(s.toString());
                } else {
                    ivClean.setVisibility(View.GONE);
                    updateSearchAppInfo(null);
                }
            }
        });
        listSearch = (ListView) searchView.findViewById(R.id.search_listview);
    }


    private void registerReceiver() {
        receiver = new InstallStateChangedReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(receiver, intentFilter);
    }

    private void loadAppManageInfo() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        if (appManageHelper == null) {
            appManageHelper = new AppManageHelper(this);
            appManageHelper.setOnReadyListener(this);
        }
        mProgressDialog.show();
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                appManageInfo = appManageHelper.getAppManageInfo();
            }
        });
    }

    private void updateAppManageInfo() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (appManageInfo != null) {
            fragments.get(0).updateAppInfo(appManageInfo.getAllAppList());
            fragments.get(1).updateAppInfo(appManageInfo.getSystemAppList());
            fragments.get(2).updateAppInfo(appManageInfo.getUserAppList());
            curWhich = mPreferences.getInt(CURRENT_WHICH, 0);
            startSortAppInfo();
        }
    }

    private void showSortMenuDialog() {
        tempWhich = curWhich;
        new MaterialDialog.Builder(this)
                .setTitle("排序")
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .setSingleChoiceItems(SORT_MENU_ITEMS, curWhich, new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        tempWhich = which;
                        return true; // 默认返回false(返回true则Dialog不消失，返回false则Dialog消失)
                    }
                })
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if (curWhich != tempWhich) {
                            curWhich = tempWhich;
                            mPreferences.edit()
                                    .putInt(CURRENT_WHICH, curWhich)
                                    .apply();
                            startSortAppInfo();
                        }
                        return false;
                    }
                })
                .create()
                .show();
    }

    private void startSortAppInfo() {
        if (fragments != null && !fragments.isEmpty()) {
            for (AppInfoFragment fragment : fragments) {
                switch (curWhich) {
                    case 0:
                        fragment.sortByName(false); // 按名称排序(升序)
                        break;
                    case 1:
                        fragment.sortByName(true); // 按名称排序(降序)
                        break;
                    case 2:
                        fragment.sortBySize(false); // 按大小排序(升序)
                        break;
                    case 3:
                        fragment.sortBySize(true); // 按大小排序(降序)
                        break;
                }
            }
        }
    }

    @Override
    public void onAppInfoReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAppManageInfo();
            }
        });
    }

    @Override
    public void onPackageAdded(String action, String packageName) {
        AppInfo appInfo = appManageHelper.getAppInfo(packageName);
        appManageInfo.putMapAppInfo(appInfo);
        appManageInfo.addAllAppInfo(appInfo);
        appManageInfo.addUserAppInfo(appInfo);
        updateAppManageInfo();
    }

    @Override
    public void onPackageReplaced(String action, String packageName) {

    }

    @Override
    public void onPackageRemoved(String action, String packageName) {
        AppInfo appInfo = appManageInfo.removeMapAppInfo(packageName);
        appManageInfo.removeAllAppInfo(appInfo);
        appManageInfo.removeUserAppInfo(appInfo);
        updateAppManageInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {
            handleSearchViewState();
            return true;
        } else if (itemId == R.id.action_sort) {
            showSortMenuDialog();
            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            loadAppManageInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void startQuery(String text) {
        if (appManageInfo != null && !appManageInfo.getAllAppList().isEmpty()) {
            List<AppInfo> resultList = new ArrayList<>();
            List<AppInfo> sourceList = appManageInfo.getAllAppList();
            for (AppInfo appInfo : sourceList) {
                if (appInfo.getAppLabel().contains(text) || appInfo.getPackageName().contains(text)) {
                    resultList.add(appInfo);
                }
            }
            switch (curWhich) {
                case 0:
                    Collections.sort(resultList, new ComparatorByName(false)); // 按名称排序(升序)
                    break;
                case 1:
                    Collections.sort(resultList, new ComparatorByName(true)); // 按名称排序(降序)
                    break;
                case 2:
                    Collections.sort(resultList, new ComparatorBySize(false)); // 按大小排序(升序)
                    break;
                case 3:
                    Collections.sort(resultList, new ComparatorBySize(true)); // 按大小排序(降序)
                    break;
            }
            updateSearchAppInfo(resultList);
        }
    }

    public void updateSearchAppInfo(List<AppInfo> list) {
        if (mAdapter == null) {
            mAdapter = new AppInfoAdapter(list);
            listSearch.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(list);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
            case R.id.search_cancle:
                handleSearchViewState();
                break;
            case R.id.search_clean:
                if (edtSeatch != null) {
                    edtSeatch.setText("");
                }
                updateSearchAppInfo(null);
                break;
        }
    }

    private void handleSearchViewState() {
        SearchViewHelper.handleSearchViewState(this, searchView, edtSeatch);
    }

    @Override
    public void onBackPressed() {
        if (searchView != null && searchView.getVisibility() == View.VISIBLE) {
            handleSearchViewState();
        } else {
            super.onBackPressed();
        }
    }
}