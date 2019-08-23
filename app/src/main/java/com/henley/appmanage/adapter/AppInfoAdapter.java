package com.henley.appmanage.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.henley.appmanage.R;
import com.henley.appmanage.data.AppInfo;
import com.henley.appmanage.utils.Utility;

import java.util.List;
import java.util.Locale;

/**
 * App信息列表适配器
 *
 * @author Henley
 * @date 2017/4/17 17:12
 */
public class AppInfoAdapter extends BaseAdapter {

    private List<AppInfo> mList;
    private Context mContext;

    public AppInfoAdapter(List<AppInfo> list) {
        this.mList = list;
    }

    public List<AppInfo> getData() {
        return mList;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_appinfo, parent, false);
            holder = new AppInfoViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AppInfoViewHolder) convertView.getTag();
        }
        final AppInfo appInfo = getItem(position);
        if (appInfo != null) {
            String versionName = appInfo.getVersionName();
            if (!TextUtils.isEmpty(versionName) && versionName.contains("-")) {
                appInfo.setVersionName(versionName.substring(0, versionName.indexOf("-")));
            }
            final String appLable = appInfo.getAppName();
            final String appVersion = formatString("版本: %s(版本号: %d)", appInfo.getVersionName(), appInfo.getVersionCode());
            final String pkgName = formatString("包名: %s", appInfo.getPackageName());
            final String signatures = formatString("签名: %s", appInfo.getSignatureMD5());
            final CharSequence appSize = computeStorageSpace(appInfo);
            holder.appIcon.setImageDrawable(appInfo.getAppIcon());
            holder.appLable.setText(appLable);
            holder.appVersion.setText(appVersion);
            holder.appSize.setText(appSize);
            holder.appSize.setVisibility(TextUtils.isEmpty(appSize) ? View.GONE : View.VISIBLE);
            holder.pkgName.setText(pkgName);
            holder.signatures.setText(signatures);
            holder.actionCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = formatString("应用名称：%s", appLable) + "\n"
                            + formatString("版本: %s(版本号: %d)", appInfo.getVersionName(), appInfo.getVersionCode()) + "\n"
                            + formatString("包名: %s", appInfo.getPackageName()) + "\n"
                            + formatString("MD5签名: %s", appInfo.getSignatureMD5()) + "\n"
                            + formatString("SHA-1签名: %s", appInfo.getSignatureSHA1()) + "\n"
                            + formatString("SHA-256签名: %s", appInfo.getSignatureSHA256());
                    if (Utility.copy(mContext, text)) {
                        Toast.makeText(mContext, "应用信息复制成功", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return convertView;
    }

    private CharSequence computeStorageSpace(AppInfo appInfo) {
        if (appInfo.getTotalSize() > 0) {
            String totalSize = formateSize(appInfo.getTotalSize());
            String codeSize = formateSize(appInfo.getCodeSize());
            String dataSize = formateSize(appInfo.getDataSize());
            String cacheSize = formateSize(appInfo.getCacheSize());
            String appSize = formatString("存储总计: %s\n应用: %s；数据: %s；缓存: %s", totalSize, codeSize, dataSize, cacheSize);
            Spannable spannable = new SpannableString(appSize);
            spannable.setSpan(new AbsoluteSizeSpan(12, true), 6 + totalSize.length(), appSize.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannable;
        } else {
            return null;
        }
    }

    private String formateSize(long sizeBytes) {
        return Formatter.formatFileSize(mContext, sizeBytes);
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
            appIcon = itemView.findViewById(R.id.appinfo_applogo);
            actionCopy = itemView.findViewById(R.id.appinfo_action_copy);
            appLable = itemView.findViewById(R.id.appinfo_appname);
            appVersion = itemView.findViewById(R.id.appinfo_appversion);
            appSize = itemView.findViewById(R.id.appinfo_appsize);
            pkgName = itemView.findViewById(R.id.appinfo_packagename);
            signatures = itemView.findViewById(R.id.appinfo_signatures);
        }
    }

}
