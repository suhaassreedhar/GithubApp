package com.example.githubapp.network.gitdata;

import com.example.githubapp.Api;
import com.example.githubapp.base.BaseNetMethod;
import com.example.githubapp.utils.RetrofitUtil;
import com.example.githubapp.utils.StringConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class GitDataMethod extends BaseNetMethod {
    private Retrofit retrofit;

    private GitDataService service;

    private static class Nested {
        static GitDataMethod instance = new GitDataMethod();
    }

    private GitDataMethod() {
        retrofit = RetrofitUtil.initRetrofit(Api.GitHubApi);
        service = retrofit.create(GitDataService.class);
    }

    public static GitDataMethod getInstance() {
        return Nested.instance;
    }

    public void getAblob(Observer<String> observer, String auth, String owner,
                         String repo, String sha) {
        Retrofit retrofit = RetrofitUtil.initCustomRetrofit(Api.GitHubApi, StringConverterFactory.create(),
                RxJavaCallAdapterFactory.create());
        GitDataService service = retrofit.create(GitDataService.class);
        service.getABlob(auth, "application/vnd.github.VERSION.raw", owner, repo, sha)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
