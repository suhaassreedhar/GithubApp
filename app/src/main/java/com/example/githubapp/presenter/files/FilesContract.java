package com.example.githubapp.presenter.files;

import com.example.githubapp.base.BasePresenter;
import com.example.githubapp.base.BaseView;
import com.example.githubapp.entity.response.repos.RepositoryContentBean;

import java.util.List;


public class FilesContract {
    public interface Presenter extends BasePresenter {
        void loadContent(String owner, String repo, String path);

        void loadFile(String owner, String repo, String path, String sha);
    }

    public interface View extends BaseView<Presenter> {
        void loadContentSuccess();

        void loadContentFail();

        void loadingContent(List<RepositoryContentBean> beanList);

        void loadFileSuccess();

        void loadFileFail();

        void loadingFile(String file);

        String getRef();

        String getBranch();
    }
}
