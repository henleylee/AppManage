package com.henley.appmanage.widget.indicator;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 整个框架的入口，核心
 *
 * @author Henley
 * @date 2017/4/20 13:16
 */
public class MagicIndicator extends FrameLayout {

    private static final String TAG = "MagicIndicator";
    private IPagerNavigator mNavigator;
    private ViewPager mViewPager;
    private MagicIndicatorPageChangeListener mPageChangeListener;

    public MagicIndicator(Context context) {
        super(context);
    }

    public MagicIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled() position = " + position + ", positionOffset = " + positionOffset + ", positionOffsetPixels = " + positionOffsetPixels);
        if (mNavigator != null) {
            mNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected() position = " + position);
        if (mNavigator != null) {
            mNavigator.onPageSelected(position);
        }
    }

    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "onPageScrollStateChanged() state = " + state);
        if (mNavigator != null) {
            mNavigator.onPageScrollStateChanged(state);
        }
    }

    public IPagerNavigator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(IPagerNavigator navigator) {
        if (mNavigator == navigator) {
            return;
        }
        if (mNavigator != null) {
            mNavigator.onDetachFromMagicIndicator();
        }
        mNavigator = navigator;
        removeAllViews();
        if (mNavigator instanceof View) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView((View) mNavigator, lp);
            mNavigator.onAttachToMagicIndicator();
        }
    }

    public void setupWithViewPager(ViewPager viewPager) {
        if (mViewPager != null) {
            if (mPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mPageChangeListener);
            }
        }
        if (viewPager != null) {
            mViewPager = viewPager;
            if (mPageChangeListener == null) {
                mPageChangeListener = new MagicIndicatorPageChangeListener(this);
            }
            viewPager.addOnPageChangeListener(mPageChangeListener);
        }
    }

    private static class MagicIndicatorPageChangeListener implements ViewPager.OnPageChangeListener{

        private MagicIndicator indicator;

        MagicIndicatorPageChangeListener(MagicIndicator indicator) {
            this.indicator = indicator;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            indicator.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            indicator.onPageScrollStateChanged(state);
        }
    }
}

