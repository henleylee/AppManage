package com.liyunlong.appmanage.widget.indicator;

import android.content.Context;

/**
 * 带颜色渐变和缩放的指示器标题
 *
 * @author liyunlong
 * @date 2017/4/20 13:21
 */
public class ScaleTransitionPagerTitleView extends ColorTransitionPagerTitleView {

    private float mMinScale = 0.75f;

    public ScaleTransitionPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(index, totalCount, leavePercent, leftToRight);    // 实现颜色渐变
        setScaleX(1.0f + (mMinScale - 1.0f) * getEndInterpolator().getInterpolation(leavePercent));
        setScaleY(1.0f + (mMinScale - 1.0f) * getEndInterpolator().getInterpolation(leavePercent));
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        super.onEnter(index, totalCount, enterPercent, leftToRight);    // 实现颜色渐变
        setScaleX(mMinScale + (1.0f - mMinScale) * getStartInterpolator().getInterpolation(enterPercent));
        setScaleY(mMinScale + (1.0f - mMinScale) * getStartInterpolator().getInterpolation(enterPercent));
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }
}