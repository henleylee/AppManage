package com.henley.appmanage.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;

/**
 * SearchView动画辅助类
 *
 * @author Henley
 * @date 2017/7/4 9:42
 */
public class SearchViewHelper {

    private static final int DEFAULT_ANIM_DURATION = 300;

    public static void handleSearchViewState(final Context context, final View searchView, final EditText editText) {
        if (context == null || searchView == null || editText == null) {
            return;
        }
        int centerX = searchView.getWidth() - Utility.dip2px(context, 56);
        int centerY = Utility.dip2px(context, 23);
        float radius = (float) Math.hypot(searchView.getWidth(), searchView.getHeight());// 确定圆的半径
        if (searchView.getVisibility() == View.VISIBLE) { // 如果当前为显示状态则隐藏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animatorHide = ViewAnimationUtils.createCircularReveal(searchView, centerX, centerY, radius, 0);
                animatorHide.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        InputMethodHelper.hideInputMethod(context, editText); // 关闭输入法
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchView.setVisibility(View.GONE);
                        editText.clearFocus();
                    }

                });
                animatorHide.setDuration(DEFAULT_ANIM_DURATION);
                animatorHide.start();
            } else {
                InputMethodHelper.hideInputMethod(context, editText); // 关闭输入法
                searchView.setVisibility(View.GONE);
                editText.clearFocus();
            }
            editText.setText("");
            searchView.setEnabled(false);
        } else { // 如果当前为隐藏状态则显示
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animator = ViewAnimationUtils.createCircularReveal(searchView, centerX, centerY, 0, radius);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        searchView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        editText.requestFocus();
                        InputMethodHelper.showInputMethod(context, editText); // 打开输入法
                    }

                });
                animator.setDuration(DEFAULT_ANIM_DURATION);
                animator.start();
                searchView.setEnabled(true);
            } else {
                searchView.setVisibility(View.VISIBLE);
                searchView.setEnabled(true);
                editText.requestFocus();
                InputMethodHelper.showInputMethod(context, editText); // 打开输入法
            }
        }
    }

}
