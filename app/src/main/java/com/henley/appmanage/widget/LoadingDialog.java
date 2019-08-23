package com.henley.appmanage.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.henley.appmanage.R;

/**
 * 加载对话框
 *
 * @author Henley
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
