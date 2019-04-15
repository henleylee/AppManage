package com.henley.appmanage.widget.indicator;

import java.util.List;

/**
 * 抽象的viewpager指示器，适用于CommonNavigator
 *
 * @author Henley
 * @date 2017/4/20 11:59
 */
public interface IPagerIndicator {

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    void onPositionDataProvide(List<PositionData> dataList);
}
