package com.liyunlong.appmanage.widget.indicator;

/**
 * 可测量内容区域的指示器标题
 *
 * @author liyunlong
 * @date 2017/4/20 12:00
 */
public interface IMeasurablePagerTitleView extends IPagerTitleView {

    int getContentLeft();

    int getContentTop();

    int getContentRight();

    int getContentBottom();
}

