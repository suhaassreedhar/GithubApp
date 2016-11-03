package com.example.githubapp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.githubapp.utils.viewmanager.LoadMoreInSwipeRefreshLayoutMoreManager;
import com.example.githubapp.utils.viewmanager.LoadMoreManager;
import com.example.githubapp.utils.viewmanager.RefreshViewManager;
import com.example.githubapp.utils.viewmanager.ViewManager;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseFragment extends Fragment {
    private final String TAG = getClass().getName();
    public static final String USERNAME = "USERNAME";

    protected View mView;
    protected Map<String, ViewManager> viewManagerMap = new HashMap<>();

    protected String username;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = setContentView(inflater, container);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME, null);
        }
        initViews(mView);

        for (ViewManager viewManager : viewManagerMap.values()) {
            viewManager.manager();
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public Context getContext() {
        return context == null ? BaseApplication.getInstance() : context;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setViewManager(ViewManager... viewManagers) {
        String key;
        for (ViewManager viewManager : viewManagers) {
            key = getKey(viewManager);
            viewManagerMap.put(key, viewManager);
        }
    }

    private String getKey(ViewManager viewManager) {
        if (viewManager instanceof LoadMoreInSwipeRefreshLayoutMoreManager) {
            return LoadMoreInSwipeRefreshLayoutMoreManager.class.getCanonicalName();
        }
        if (viewManager instanceof RefreshViewManager) {
            return RefreshViewManager.class.getCanonicalName();
        }
        if (viewManager instanceof LoadMoreManager) {
            return LoadMoreManager.class.getCanonicalName();
        }
        return null;
    }

    public <T extends ViewManager> T getViewManager(Class<? extends ViewManager> clazz) {
        ViewManager result;
        Log.i(TAG, "get class name = " + clazz.getName());
        if ((result = viewManagerMap.get(clazz.getName())) == null) {
            return null;
        }
        return (T) result;
    }

    protected abstract void initViews(View view);

    protected abstract View setContentView(LayoutInflater inflater, @Nullable ViewGroup container);
}
