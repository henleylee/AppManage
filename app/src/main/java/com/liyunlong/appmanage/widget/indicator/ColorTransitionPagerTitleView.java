package com.liyunlong.appmanage.widget.indicator;

import android.content.Context;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 两种颜色过渡的指示器标题
 *
 * @author liyunlong
 * @date 2017/4/20 13:22
 */
public class ColorTransitionPagerTitleView extends SimplePagerTitleView {

    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Interpolator mEndInterpolator = new LinearInterpolator();

    public ColorTransitionPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index, int totalCount) {
    }

    @Override
    public void onDeselected(int index, int totalCount) {
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        int color = (Integer) ArgbEvaluatorHolder.eval(mEndInterpolator.getInterpolation(leavePercent), getSelectedColor(), getNormalColor());
        setTextColor(color);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        int color = (Integer) ArgbEvaluatorHolder.eval(mStartInterpolator.getInterpolation(enterPercent), getNormalColor(), getSelectedColor());
        setTextColor(color);
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public Interpolator getEndInterpolator() {
        return mEndInterpolator;
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new LinearInterpolator();
        }
    }
}

