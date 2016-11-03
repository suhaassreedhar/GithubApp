package com.example.githubapp.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;

import com.example.githubapp.Constants;
import com.example.githubapp.utils.LanguageUtil;
import com.example.githubapp.utils.RetrofitUtil;
import com.example.githubapp.utils.SPUtil;

public class BaseApplication extends Application {
    private static BaseApplication instance;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitUtil.setupContext(getApplicationContext());

        getLocalLanguageSetting();
    }

    public static BaseApplication getInstance() {
        if (instance == null) {
            synchronized (BaseApplication.class) {
                instance = new BaseApplication();
            }
        }
        return instance;
    }

    private void getLocalLanguageSetting() {
        int language = SPUtil.getField(getApplicationContext(), Constants.LOCAL_CONFIGURATION, Constants.LOCAL_LANGUAGE_CONFIG, -1);

        if (language == -1) {
            return;
        }
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = LanguageUtil.getLanguageSetting(language);
        resources.updateConfiguration(config, dm);
    }
}
