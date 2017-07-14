package com.liyunlong.appmanage.widget.indicator;

/**
 * 抽象的指示器标题，适用于CommonNavigator
 *
 * @author liyunlong
 * @date 2017/4/20 11:59
 */
public interface IPagerTitleView {

    /**
     * 被选中
     */
    void onSelected(int index, int totalCount);

    /**
     * 未被选中
     */
    void onDeselected(int index, int totalCount);

    /**
     * 离开
     *
     * @param leavePercent 离开的百分比, 0.0f - 1.0f
     * @param leftToRight  从左至右离开
     */
    void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight);

    /**
     * 进入
     *
     * @param enterPercent 进入的百分比, 0.0f - 1.0f
     * @param leftToRight  从左至右离开
     */
    void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight);
}
