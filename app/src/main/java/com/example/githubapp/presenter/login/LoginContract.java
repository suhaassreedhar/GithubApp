package com.example.githubapp.presenter.login;

import com.example.githubapp.base.BasePresenter;
import com.example.githubapp.base.BaseView;
import com.example.githubapp.entity.response.AppAuthorizationBean;
import com.example.githubapp.entity.response.AuthenticatedUserBean;


public class LoginContract {
    public interface Presenter extends BasePresenter {
        void login();

        void loadUserInfo();
    }

    public interface View extends BaseView<Presenter> {
        void loginSuccess();

        void loginFail();

        void logining(AppAuthorizationBean bean);

        void loadUserInfo(AuthenticatedUserBean user);

        void loadSuccess();

        void loadFail();

        String getAuth();
    }
}
