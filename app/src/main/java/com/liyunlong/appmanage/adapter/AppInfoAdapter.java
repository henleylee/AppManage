package com.liyunlong.appmanage.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liyunlong.appmanage.R;
import com.liyunlong.appmanage.data.AppInfo;

import java.util.List;
import java.util.Locale;

/**
 * @author liyunlong
 * @date 2017/4/17 17:12
 */
public class AppInfoAdapter extends BaseAdapter {

    private List<AppInfo> mList;
    private Context mContext;
    private boolean needRefresh;

    public AppInfoAdapter(List<AppInfo> list) {
        this.mList = list;
    }

    public List<AppInfo> getData() {
        return mList;
    }

    public void setmList(List<AppInfo> mList) {
        this.mList = mList;
    }

    public void refresh(List<AppInfo> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public AppInfo getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppInfoViewHolder holder;
        if (convertView == null) {
            if (mContext == null) {
                mContext = parent.getContext();
            }
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout_appinfo, parent, false);
            holder = new AppInfoViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AppInfoViewHolder) convertView.getTag();
        }
                final AppInfo appInfo = getItem(position);
        if (appInfo != null) {
            String versionName = appInfo.getVersionName();
            if (!TextUtils.isEmpty(versionName) && versionName.contains("-")) {
                versionName = versionName.substring(0, versionName.indexOf("-"));
            }
            final String appLable = appInfo.getAppLabel();
            final String appVersion = formatString("版本: %s(版本号: %d)", versionName, appInfo.getVersionCode());
            final String appSize = formatString("存储总计: %s\n应用: %s；数据: %s；缓存: %s", appInfo.getTotalSize(), appInfo.getCodeSize(), appInfo.getDataSize(), appInfo.getCacheSize());
            final String pkgName = formatString("包名: %s", appInfo.getPackageName());
            final String signatures = formatString("签名: %s", appInfo.getSigmd5());
            holder.appIcon.setImageDrawable(appInfo.getAppIcon());
            holder.appLable.setText(appLable);
            holder.appVersion.setText(appVersion);
            holder.appSize.setText(appSize);
            holder.pkgName.setText(pkgName);
            holder.signatures.setText(signatures);
            holder.actionCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE)).setText("应用程序：" + appLable + "\n" + appVersion + "\n" + pkgName + "\n" + signatures);
                    Toast.makeText(mContext, "应用信息复制成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }

    private String formatString(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    static class AppInfoViewHolder {

        private final ImageView appIcon;
        private final TextView actionCopy;
        private final TextView appLable;
        private final TextView appVersion;
        private final TextView appSize;
        private final TextView pkgName;
        private final TextView signatures;

        AppInfoViewHolder(View itemView) {
            appIcon = (ImageView) itemView.findViewById(R.id.appinfo_applogo);
            actionCopy = (TextView) itemView.findViewById(R.id.appinfo_action_copy);
            appLable = (TextView) itemView.findViewById(R.id.appinfo_appname);
            appVersion = (TextView) itemView.findViewById(R.id.appinfo_appversion);
            appSize = (TextView) itemView.findViewById(R.id.appinfo_appsize);
            pkgName = (TextView) itemView.findViewById(R.id.appinfo_packagename);
            signatures = (TextView) itemView.findViewById(R.id.appinfo_signatures);
        }
    }
}
