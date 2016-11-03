package com.example.githubapp.network.gitdata;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;


public interface GitDataService {
    /**
     * Get a Blob
     * @param auth
     * @param owner
     * @param repo
     * @param sha
     * @return
     */
    @Headers("Cache-Control: public, max-age=600")
    @GET("/repos/{owner}/{repo}/git/blobs/{sha}")
    Observable<String> getABlob(@Header("Authorization") String auth,
                                  @Header("accept") String acc,
                                  @Path("owner") String owner,
                                  @Path("repo") String repo,
                                  @Path("sha") String sha);
}
