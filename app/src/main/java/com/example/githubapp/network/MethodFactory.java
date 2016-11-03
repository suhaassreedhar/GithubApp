package com.example.githubapp.network;

import com.example.githubapp.base.BaseNetMethod;
import com.example.githubapp.network.activity.ActivityMethod;
import com.example.githubapp.network.gitdata.GitDataMethod;
import com.example.githubapp.network.overview.OverviewMethod;
import com.example.githubapp.network.repositories.RepositoriesMethod;
import com.example.githubapp.network.user.UserMethod;

public class MethodFactory {
    public static BaseNetMethod getMethod(Class<? extends BaseNetMethod> clazz) {
        if (clazz.getName().equals(ActivityMethod.class.getName())) {
            return ActivityMethod.getInstance();
        } else if (clazz.getName().equals(GitDataMethod.class.getName())) {
            return GitDataMethod.getInstance();
        } else if (clazz.getName().equals(OverviewMethod.class.getName())) {
            return OverviewMethod.getInstance();
        } else if (clazz.getName().equals(RepositoriesMethod.class.getName())) {
            return RepositoriesMethod.getInstance();
        } else if (clazz.getName().equals(UserMethod.class.getName())) {
            return UserMethod.getInstance();
        } else {
            return null;
        }
    }
}
