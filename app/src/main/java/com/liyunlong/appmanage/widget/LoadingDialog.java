package com.liyunlong.appmanage.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.liyunlong.appmanage.R;

/**
 * 加载对话框
 *
 * @author liyunlong
 * @date 2017/7/13 17:00
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.LoadingDialog);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    private void initDialog() {
        this.setContentView(R.layout.layout_loading_dialog);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
    }

}
