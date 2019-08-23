package com.henley.appmanage.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.henley.appmanage.widget.indicator.CommonNavigator;
import com.henley.appmanage.widget.indicator.CommonNavigatorAdapter;
import com.henley.appmanage.widget.indicator.IPagerIndicator;
import com.henley.appmanage.widget.indicator.IPagerTitleView;
import com.henley.appmanage.widget.indicator.LinePagerIndicator;
import com.henley.appmanage.widget.indicator.ScaleTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Navigator辅助类
 *
 * @author Henley
 * @date 2017/7/14 9:45
 */
public class NavigatorHelper {

    @NonNull
    public static CommonNavigator getCommonNavigator(Context context, final ViewPager viewPager, final List<String> titles) {
        // 缩放 + 颜色渐变
        final CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setEnablePivotScroll(true);
        commonNavigator.setScrollPivotX(0.8f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                return getPagerTitleView(context, index, titles, viewPager);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return getLinePagerIndicator(context);
            }
        });
        return commonNavigator;
    }

    @NonNull
    private static LinePagerIndicator getLinePagerIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
        indicator.setLineHeight(Utility.dip2px(context, 2));
        List<String> colorList = new ArrayList<>();
        colorList.add("#FF5500");
        indicator.setColorList(colorList);
        return indicator;
    }

    @NonNull
    private static ScaleTransitionPagerTitleView getPagerTitleView(Context context, final int index, List<String> titles, final ViewPager mViewPager) {
        ScaleTransitionPagerTitleView pagerTitleView = new ScaleTransitionPagerTitleView(context);
        pagerTitleView.setText(titles.get(index));
        pagerTitleView.setTextSize(18);
        pagerTitleView.setNormalColor(Color.parseColor("#191919"));
        pagerTitleView.setSelectedColor(Color.parseColor("#FC5638"));
        pagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(index);
            }
        });
        return pagerTitleView;
    }

}
