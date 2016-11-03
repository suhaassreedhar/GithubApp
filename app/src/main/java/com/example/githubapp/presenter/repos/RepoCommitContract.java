package com.example.githubapp.presenter.repos;

import com.example.githubapp.base.BasePresenter;
import com.example.githubapp.base.BaseView;
import com.example.githubapp.entity.response.repos.SingleCommitBean;

import java.util.List;


public class RepoCommitContract {
    public interface Presenter extends BasePresenter {
        void getCommits();

        void setPageId(int pageId);
    }

    public interface View extends BaseView<Presenter> {
        void getCommitsSuccess();

        void getCommitsFail();

        void gettingCommits(List<SingleCommitBean> singleCommitBeen);

        String getOwner();

        String getRepo();
    }
}
