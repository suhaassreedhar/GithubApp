package com.example.githubapp.presenter.files;

import android.content.Context;
import android.util.Log;

import com.example.githubapp.base.NetPresenter;
import com.example.githubapp.entity.response.repos.RepositoryContentBean;
import com.example.githubapp.network.gitdata.GitDataMethod;
import com.example.githubapp.network.repositories.RepositoriesMethod;

import java.util.List;

import rx.Subscriber;



public class FilesPresenter extends NetPresenter implements FilesContract.Presenter {
    private final String TAG = getClass().getName();

    private Context mContext;

    private FilesContract.View mFilesView;

    private Subscriber<List<RepositoryContentBean>> contentSubscriber;
    private Subscriber<String> fileSubscriber;

    private String auth;
    private RepositoriesMethod method;

    public FilesPresenter(Context context, FilesContract.View view) {
        mContext = context;
        mFilesView = view;
        mFilesView.setPresenter(this);
        start();
    }

    @Override
    public void start() {
        auth = getAuth(mContext);
        method = getMethod(RepositoriesMethod.class);
    }

    @Override
    public void stop() {
        unsubscribe(contentSubscriber, fileSubscriber);
    }

    @Override
    public void loadContent(String owner, String repo, String path) {
        contentSubscriber = new Subscriber<List<RepositoryContentBean>>() {
            @Override
            public void onCompleted() {
                mFilesView.loadContentSuccess();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mFilesView.loadContentFail();
            }

            @Override
            public void onNext(List<RepositoryContentBean> beanList) {
                mFilesView.loadingContent(beanList);
            }
        };
        Log.i(TAG, auth);
        method.getRepositoryContent(contentSubscriber, auth, null, owner, repo, path, mFilesView.getRef());
    }

    @Override
    public void loadFile(String owner, String repo, String path, String sha) {
        fileSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mFilesView.loadFileSuccess();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mFilesView.loadFileFail();
            }

            @Override
            public void onNext(String s) {
                mFilesView.loadingFile(s);
            }
        };
        Log.i(TAG, auth);
        if (path.endsWith(".md")) {
            method.getFileContent(fileSubscriber, auth, "application/vnd.github.VERSION.html",
                    owner, repo, path, mFilesView.getBranch());
        } else {
            GitDataMethod.getInstance().getAblob(fileSubscriber, auth, owner, repo, sha);
        }
    }
}
