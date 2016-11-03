package com.example.githubapp.network.overview;

import com.example.githubapp.Constants;
import com.example.githubapp.base.BaseNetMethod;
import com.example.githubapp.entity.request.AuthorizationRequest;
import com.example.githubapp.entity.response.AppAuthorizationBean;
import com.example.githubapp.utils.RetrofitUtil;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class OverviewMethod extends BaseNetMethod {
    private Retrofit retrofit;

    private OverviewService service;

    private static class Nested {
        static OverviewMethod instance = new OverviewMethod();
    }

    private OverviewMethod() {
        retrofit = RetrofitUtil.initRetrofit(Constants.GITHUB_API_URL);

        service = retrofit.create(OverviewService.class);
    }

    public static OverviewMethod getInstance() {
        return Nested.instance;
    }

    public void getOrCreateAuthorization(Observer<AppAuthorizationBean> observer, String auth, AuthorizationRequest requestBean) {
        service.login(auth, requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
