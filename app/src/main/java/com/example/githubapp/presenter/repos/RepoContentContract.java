package com.example.githubapp.presenter.repos;

import com.example.githubapp.base.BasePresenter;
import com.example.githubapp.base.BaseView;
import com.example.githubapp.entity.response.repos.BranchBean;
import com.example.githubapp.entity.response.repos.RepositoriesBean;
import com.example.githubapp.entity.response.repos.TagBean;

import java.util.List;


public class RepoContentContract {
    public interface Presenter extends BasePresenter {
        void loadReadMe();

        void loadRepo();

        void loadBranches();

        void loadTags();

        void checkStarred();

        void starRepo();

        void unstarRepo();

        void checkWatched();

        void watchRepo();

        void unwatchRepo();
    }

    public interface View extends BaseView<Presenter> {
        void loadBranchesSuccess();

        void loadBranchesFail();

        void loadTagsSuccess();

        void loadTagsFail();

        void loadingBranches(List<BranchBean> branchBeen);

        void loadingTags(List<TagBean> tagBeen);

        void loadReadMeFail();

        void noReadMe();

        void loadReadMeSuccess();

        void loadingReadMe(String string);

        void loadRepoSuccess();

        void loadRepoFail();

        void loadingRepo(RepositoriesBean bean);

        void checkStarredFail();

        void isStarred();

        void isUnstarred();

        void starSuccess();

        void starFail();

        void unstarSuccess();

        void unstarFail();

        void isWatched();

        void isUnwatched();

        void checkWatchedFail();

        void watchSuccess();

        void watchFail();

        void unwatchSuccess();

        void unwatchFail();

        String getUsername();

        String getRepoName();

        String getRef();
    }
}
