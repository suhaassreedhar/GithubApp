package com.example.githubapp.utils.viewmanager;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.githubapp.adapter.LoadMoreRecyclerViewAdapter;
import com.example.githubapp.listener.LoadListener;

import java.util.List;



public abstract class LoadMoreManager implements ViewManager, LoadListener {
    private final String TAG = getClass().getName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public LoadMoreManager(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void manager() {
        setLoadMoreInRecylerView(recyclerView, swipeRefreshLayout);
    }

    public boolean hasNoMoreData(List<?> list, LoadMoreRecyclerViewAdapter adapter) {
        if (list == null || list.size() == 0) {
            Log.i(TAG, "has no more");
            if (adapter.isHasMoreData()) {
                adapter.setHasLoading(false);
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    protected void loadMoreAction() {
        this.load();
    }

    public void setLoadMoreInRecylerView(RecyclerView recyclerView,
                                         @Nullable final SwipeRefreshLayout swipeRefreshLayout) {
        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
            return;
        }
        if (!(recyclerView.getAdapter() instanceof LoadMoreRecyclerViewAdapter)) {
            return;
        }
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final LoadMoreRecyclerViewAdapter adapter = (LoadMoreRecyclerViewAdapter) recyclerView.getAdapter();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                int firstItemPosition = manager.findFirstCompletelyVisibleItemPosition();
                if (lastItemPosition == adapter.getItemCount() - 1
                        && firstItemPosition != adapter.getItemCount() - 1
                        && adapter.isHasMoreData()) {
                    if (swipeRefreshLayout == null) {
                        loadMoreAction();
                    } else {
                        if (!swipeRefreshLayout.isRefreshing()) {
                            loadMoreAction();
                        }
                    }
                }
            }
        });
    }
}
