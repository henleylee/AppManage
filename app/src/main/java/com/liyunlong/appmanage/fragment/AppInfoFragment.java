package com.liyunlong.appmanage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.liyunlong.appmanage.R;
import com.liyunlong.appmanage.adapter.AppInfoAdapter;
import com.liyunlong.appmanage.data.AppInfo;
import com.liyunlong.appmanage.utils.ComparatorByName;
import com.liyunlong.appmanage.utils.ComparatorBySize;

import java.util.Collections;
import java.util.List;

/**
 * @author liyunlong
 * @date 2017/4/17 16:55
 */
public class AppInfoFragment extends Fragment {

    private AppInfoAdapter mAdapter;
    private ListView mListView;
    private List<AppInfo> mList;
    private boolean hasInit;
    private boolean needRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appinfo, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview);
        hasInit = true;
        if (needRefresh) {
            updateAppInfo(mList);
            needRefresh = false;
        }
        return rootView;
    }

    public void updateAppInfo(List<AppInfo> list) {
        if (hasInit) {
            if (mAdapter == null) {
                mAdapter = new AppInfoAdapter(list);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.refresh(list);
            }
        } else {
            this.mList = list;
            needRefresh = true;
        }
    }

    /**
     * 按名称排序
     *
     * @param descOrder 是否降序
     */
    public void sortByName(final boolean descOrder) {
        if (mAdapter != null) {
            List<AppInfo> appInfoList = mAdapter.getData();
            if (appInfoList != null && !appInfoList.isEmpty()) {
                Collections.sort(appInfoList, new ComparatorByName(descOrder));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 按大小排序
     *
     * @param descOrder 是否降序
     */
    public void sortBySize(final boolean descOrder) {
        if (mAdapter != null) {
            List<AppInfo> appInfoList = mAdapter.getData();
            if (appInfoList != null && !appInfoList.isEmpty()) {
                Collections.sort(appInfoList, new ComparatorBySize(descOrder));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
