package com.example.githubapp.base;

import android.content.Context;

import com.example.githubapp.Constants;
import com.example.githubapp.network.MethodFactory;
import com.example.githubapp.utils.SPUtil;

import rx.Subscriber;


public class NetPresenter {
    public <T extends BaseNetMethod> T getMethod(Class<T> clazz) {
        return (T) MethodFactory.getMethod(clazz);
    }

    public String getAuth(Context context) {
        return SPUtil.getString(context, Constants.USER_INFO, Constants.USER_AUTH, null);
    }

    public void unsubscribe(Subscriber... subscribers) {
        for (Subscriber subscriber : subscribers) {
            if (subscriber != null) {
                if (subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe();
                }
            }
        }
    }
}
