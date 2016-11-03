package com.example.githubapp.presenter.login;

import android.content.Context;

import com.example.githubapp.Api;
import com.example.githubapp.Constants;
import com.example.githubapp.base.NetPresenter;
import com.example.githubapp.entity.request.AuthorizationRequest;
import com.example.githubapp.entity.response.AppAuthorizationBean;
import com.example.githubapp.entity.response.AuthenticatedUserBean;
import com.example.githubapp.network.overview.OverviewMethod;
import com.example.githubapp.network.user.UserMethod;
import com.example.githubapp.presenter.login.LoginContract.Presenter;
import com.example.githubapp.utils.SPUtil;

import java.util.ArrayList;

import rx.Subscriber;


public class LoginPresenter extends NetPresenter implements Presenter {
    private final String TAG = getClass().getName();

    private Context mContext;
    private LoginContract.View mLoginView;

    private Subscriber<AppAuthorizationBean> mLoginSubscriber;
    private Subscriber<AuthenticatedUserBean> mUserSubscriber;

    private OverviewMethod overviewMethod;
    private UserMethod userMethod;

    public LoginPresenter(Context context, LoginContract.View view) {
        mContext = context;
        mLoginView = view;
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {
        overviewMethod = getMethod(OverviewMethod.class);
        userMethod = getMethod(UserMethod.class);
    }

    @Override
    public void stop() {
        unsubscribe(mLoginSubscriber, mUserSubscriber);
    }

    @Override
    public void login() {
        mLoginSubscriber = new Subscriber<AppAuthorizationBean>() {
            @Override
            public void onCompleted() {
                mLoginView.loginSuccess();
            }

            @Override
            public void onError(Throwable e) {
                mLoginView.loginFail();
                e.printStackTrace();
            }

            @Override
            public void onNext(AppAuthorizationBean appAuthorization) {
                mLoginView.logining(appAuthorization);
            }
        };
        String auth = mLoginView.getAuth();
        AuthorizationRequest request = new AuthorizationRequest();
        request.setClient_secret(Api.CLIENT_SERECT);
        ArrayList<String> scopes = new ArrayList<>();
        for (String str : Api.SCPOES) {
            scopes.add(str);
        }
        request.setScopes(scopes);
        request.setNote(Api.NOTE);
        overviewMethod.getOrCreateAuthorization(mLoginSubscriber, auth, request);
    }

    @Override
    public void loadUserInfo() {
        mUserSubscriber = new Subscriber<AuthenticatedUserBean>() {
            @Override
            public void onCompleted() {
                mLoginView.loadSuccess();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoginView.loadFail();
            }

            @Override
            public void onNext(AuthenticatedUserBean authenticatedUser) {
                mLoginView.loadUserInfo(authenticatedUser);
            }
        };
        userMethod.getAuthenticatedUser(mUserSubscriber, SPUtil.getString(mContext, Constants.USER_INFO, Constants.USER_AUTH, null));
    }
}
