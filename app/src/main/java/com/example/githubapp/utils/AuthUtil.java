package com.example.githubapp.utils;

import okhttp3.Credentials;


public class AuthUtil {
    public static String generateAuth(String username, String password) {
        String basicAuth = Credentials.basic(username, password);
        return basicAuth;
    }
}
