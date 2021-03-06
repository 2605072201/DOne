package com.dzy.done.presenter;

import android.util.Log;

import com.dzy.done.model.bean.ListItem;
import com.dzy.done.model.ListModel;
import com.dzy.done.model.ListModelCallback;
import com.dzy.done.util.NetworkUtils;
import com.dzy.done.view.ContentListView;

import java.util.ArrayList;
import java.util.List;

/**
 *  主界面list的presenter
 * Created by dzysg on 2015/10/9 0009.
 */
public class MainListPresenter implements ListPresenter
{

    ContentListView mView;
    ListModel mModel;
    int mType;
    boolean isLoading = false;
    int page = 1;
    List<ListItem> mDatas = new ArrayList<>();

    private ListModelCallback mCallback = new ListModelCallback()
    {
        @Override
        public void onFinish(List<ListItem> items)
        {

            isLoading = false;
            if (mView==null)
                return;
            if (items!=null)
            {
                mDatas.addAll(items);
                mView.showDatas(mDatas);
                page++;
                Log.i("tag", "presenter onFinish    items:" + items.size() + "");
            }
            mView.hideProgress();
        }

        @Override
        public void OnFalure(String msg)
        {
            isLoading = false;
            if (mView==null)
                return;
            Log.e("tag", "onFalure    " + msg);
            mView.showMsg(msg);
            mView.hideProgress();
        }
    };

    public MainListPresenter(int type)
    {
        mType = type;
        mModel = ListModel.getInstant();
    }

    /**
     * 加载数据
     */
    @Override
    public void loadListDates()
    {
        if(!NetworkUtils.isNetworkConnected())
        {
            mView.showMsg("当前无网络");
            mView.hideProgress();
            return;
        }


        if (isLoading)
            return;

        page=1;
        isLoading = true;
        mDatas.clear();
        mView.showProgress();
        mModel.LoadDatas(page,mType,mCallback);
    }


    /**
     * 加载更多，当列表滑底时被调用
     */
    @Override
    public void loadMore()
    {
        if(!NetworkUtils.isNetworkConnected())
        {
            mView.showMsg("当前无网络");
            mView.hideProgress();
            return;
        }

        if (isLoading)
            return;

        isLoading = true;
        mView.showProgress();
        mModel.LoadDatas(page,mType,mCallback);
    }

    @Override
    public void attachView(ContentListView view)
    {
        mView = view;
    }

    @Override
    public void detach()
    {
        mView = null;
    }

    @Override
    public void onResume()
    {
        mView.showDatas(mDatas);
    }


}
