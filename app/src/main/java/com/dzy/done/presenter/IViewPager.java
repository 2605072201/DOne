package com.dzy.done.presenter;

import com.dzy.done.bean.ListItem;

import java.util.List;

public interface IViewPager {

    void showDatas(List<ListItem> dailies);

    void showProgress();

    void hideProgress();

    void failload();

}