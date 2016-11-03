package com.example.githubapp.view.repositories;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.githubapp.R;
import com.example.githubapp.adapter.CommitsRecyclerViewAdapter;
import com.example.githubapp.entity.response.repos.SingleCommitBean;
import com.example.githubapp.presenter.repos.RepoCommitContract;
import com.example.githubapp.presenter.repos.RepoCommitPresenter;
import com.example.githubapp.ui.RefreshView;
import com.example.githubapp.utils.viewmanager.LoadMoreInSwipeRefreshLayoutMoreManager;
import com.example.githubapp.utils.viewmanager.RefreshViewManager;
import com.example.githubapp.view.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;


public class RepoCommitActivity extends ToolbarActivity implements RepoCommitContract.View {
    private final String TAG = getClass().getName();

    public static final String REPONAME = "REPONAME";
    public static final String OWNER = "OWNER";

    private RepoCommitContract.Presenter mPresenter;

    private String repoName;
    private String owner;

    private RefreshView mRefreshView;
    private SwipeRefreshLayout mCommitSRLayout;
    private RecyclerView mCommitRV;

    private CommitsRecyclerViewAdapter mCommitAdapter;

    private List<SingleCommitBean> commitList = new ArrayList<>();

    private LoadMoreInSwipeRefreshLayoutMoreManager loadMoreInSwipeRefreshLayoutMoreManager;
    private RefreshViewManager refreshViewManager;

    private void getAttrs() {
        repoName = getIntent().getStringExtra(REPONAME);
        owner = getIntent().getStringExtra(OWNER);
    }

    @Override
    protected void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    @Override
    public void initViews() {
        getAttrs();

        setContent(R.layout.content_repo_commit);

        new RepoCommitPresenter(this, this);

        mRefreshView = (RefreshView) findViewById(R.id.commit_RefreshView);
        mCommitSRLayout = (SwipeRefreshLayout) findViewById(R.id.commit_SRLayout);
        mCommitRV = (RecyclerView) findViewById(R.id.commit_RV);

        setupSwipeRefreshLayout();
        setupRecyclerView();

        setViewManager(new LoadMoreInSwipeRefreshLayoutMoreManager(mCommitRV, mCommitSRLayout) {
            @Override
            public void load() {
                getCommits();
            }
        }, new RefreshViewManager(mRefreshView, mCommitSRLayout) {
            @Override
            public void load() {
                mPresenter.setPageId(1);
                getCommits();
            }
        });

        loadMoreInSwipeRefreshLayoutMoreManager = getViewManager(LoadMoreInSwipeRefreshLayoutMoreManager.class);
        refreshViewManager = getViewManager(RefreshViewManager.class);
    }

    @Override
    protected void setToolbar() {
        super.setToolbar();
        setToolbarTitle(R.string.signout);
        setOnToolbarNavClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void launchActiivty(Context context, String owner, String repoName) {
        Intent intent = new Intent();
        intent.putExtra(REPONAME, repoName);
        intent.putExtra(OWNER, owner);
        intent.setClass(context, RepoCommitActivity.class);
        context.startActivity(intent);
    }

    private void getCommits() {
        mPresenter.getCommits();
    }

    private void setupSwipeRefreshLayout() {
        mCommitSRLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        mCommitSRLayout.setColorSchemeResources(R.color.colorAccent);
        mCommitSRLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.setPageId(1);
                loadMoreInSwipeRefreshLayoutMoreManager.setSwipeRefreshLayoutRefreshing(mCommitAdapter);
            }
        });
    }

    private void setupRecyclerView() {
        mCommitAdapter = new CommitsRecyclerViewAdapter(this);
        mCommitRV.setLayoutManager(new LinearLayoutManager(this));
        mCommitRV.setAdapter(mCommitAdapter);
    }

    @Override
    public void getCommitsSuccess() {
        mCommitSRLayout.setRefreshing(false);
        if (!mRefreshView.isRefreshSuccess()) {
            mRefreshView.refreshSuccess();
        }
    }

    @Override
    public void getCommitsFail() {
        mCommitSRLayout.setRefreshing(false);
        mRefreshView.refreshFail();
    }

    @Override
    public void gettingCommits(List<SingleCommitBean> singleCommitBeen) {
        if (mCommitSRLayout.isRefreshing()) {
            commitList.clear();
        }
        loadMoreInSwipeRefreshLayoutMoreManager.hasNoMoreData(singleCommitBeen, mCommitAdapter);
        commitList.addAll(singleCommitBeen);
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getRepo() {
        return repoName;
    }

    @Override
    public void setPresenter(RepoCommitContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
