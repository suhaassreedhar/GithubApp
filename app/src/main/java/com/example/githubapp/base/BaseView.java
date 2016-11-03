package com.example.githubapp.base;


public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
