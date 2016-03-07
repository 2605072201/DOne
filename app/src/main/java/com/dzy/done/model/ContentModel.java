package com.dzy.done.model;

import android.support.v4.util.LruCache;

import com.dzy.done.network.ApiServer;
import com.dzy.done.bean.PictureItem;
import com.dzy.done.bean.ThingItem;
import com.dzy.done.config.app;
import com.dzy.done.util.MLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 数据获取，单例
 * Created by dzysg on 2015/10/13 0013.
 */
public class ContentModel
{

    private static ContentModel Model = null;
    private ApiServer mApiServer;

    private LruCache<String, String> mArticleCache = new LruCache<String, String>(100);
    private LruCache<String, PictureItem> mPictureCache = new LruCache<String, PictureItem>(100);
    private LruCache<String, ThingItem> mThingCache = new LruCache<String, ThingItem>(100);
    private Call mCall = null;

    public ContentModel()
    {
        mApiServer = app.getApiServer();
    }

    public static ContentModel get()
    {
        if (Model == null)
            Model = new ContentModel();
        return Model;
    }

    public void cancel()
    {
        mCall.cancel();
    }

    public void getArticle(final String url, final IGetArticleCallback callback)
    {
        String cache = mArticleCache.get(url);
        if (cache!=null)
        {
            callback.Finish(cache);
            return;
        }


        Call<String> call = mApiServer.getArticle(url);
        mCall = call;
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                mArticleCache.put(url, response.body());
                callback.Finish(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                if (!call.isCanceled())
                {
                    callback.Falure(t.getMessage());
                }
                MLog.getLogger().e(t.getMessage());
            }
        });
    }

    public void getPicture(final String url, final IGetPictureCallback callback)
    {
        PictureItem cache = mPictureCache.get(url);
        if (cache!=null)
        {
            callback.Finish(cache);
            return;
        }


        Call<PictureItem> call = mApiServer.getPicture(url);
        mCall = call;
        call.enqueue(new Callback<PictureItem>()
        {
            @Override
            public void onResponse(Call<PictureItem> call, Response<PictureItem> response)
            {
                mPictureCache.put(url, response.body());
                callback.Finish(response.body());
            }

            @Override
            public void onFailure(Call<PictureItem> call, Throwable t)
            {
                if (!call.isCanceled())
                {
                    callback.Falure(t.getMessage());
                }
                MLog.getLogger().e(t.getMessage());
            }
        });
    }

    public void getThing(final String url, final IGetThingCallback callback)
    {

        ThingItem cache = mThingCache.get(url);
        if (cache!=null)
        {
            callback.Finish(cache);
            return;
        }

        Call<ThingItem> call = mApiServer.getThing(url);
        mCall = call;
        call.enqueue(new Callback<ThingItem>()
        {
            @Override
            public void onResponse(Call<ThingItem> call, Response<ThingItem> response)
            {
                mThingCache.put(url, response.body());
                callback.Finish(response.body());
            }

            @Override
            public void onFailure(Call<ThingItem> call, Throwable t)
            {
                if (!call.isCanceled())
                {
                    callback.Falure(t.getMessage());
                }
                MLog.getLogger().e(t.getMessage());

            }
        });
    }


    public interface IGetArticleCallback
    {
        void Finish(String content);

        void Falure(String msg);
    }

    public interface IGetPictureCallback
    {
        void Finish(PictureItem item);

        void Falure(String msg);
    }

    public interface IGetThingCallback
    {
        void Finish(ThingItem item);

        void Falure(String msg);
    }
}
