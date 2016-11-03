package com.example.githubapp.utils.viewmanager;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.example.githubapp.adapter.LoadMoreRecyclerViewAdapter;



public abstract class LoadMoreInSwipeRefreshLayoutMoreManager extends LoadMoreManager {

    public LoadMoreInSwipeRefreshLayoutMoreManager(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        super(recyclerView, swipeRefreshLayout);
    }

    public void setSwipeRefreshLayoutRefreshing(LoadMoreRecyclerViewAdapter adapter) {
        adapter.setHasLoading(true);
        adapter.hideLoadingView();
        load();
    }
}
