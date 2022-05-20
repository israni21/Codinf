package rest;

import model.GitHubUsers1;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubUserEndPoint1 {

    @GET("/users/{user}")

    Call<GitHubUsers1> getUser(@Path("user") String user);
}
