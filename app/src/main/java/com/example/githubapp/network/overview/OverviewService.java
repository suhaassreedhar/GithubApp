package com.example.githubapp.network.overview;

import com.example.githubapp.Api;
import com.example.githubapp.entity.request.AuthorizationRequest;
import com.example.githubapp.entity.response.AppAuthorizationBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import rx.Observable;


public interface OverviewService {
    @PUT("/authorizations/clients/" + Api.CLIENT_ID)
    Observable<AppAuthorizationBean> login(@Header("Authorization") String auth,
                                           @Body AuthorizationRequest request);
}
