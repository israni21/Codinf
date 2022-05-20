package rest;

import model.GitHubUsers2;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubUserEndPoint2 {

    @GET("/users/{user}")
    Call<GitHubUsers2> getUser(@Path("user") String user);
}
