package com.example.githubapp.presenter.repos;

import com.example.githubapp.base.BasePresenter;
import com.example.githubapp.base.BaseView;
import com.example.githubapp.entity.response.repos.RepositoriesBean;

import java.util.List;



public class ReposContract {
    public interface Presenter extends BasePresenter {
        void loadUserRepositories();
    }

    public interface View extends BaseView<Presenter> {
        void loadingRepos(List<RepositoriesBean> list);

        void loadFail();

        void loadSuccess();

        String getUsername();
    }
}
