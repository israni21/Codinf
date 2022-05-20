package rest;

import model.GitHubIssue;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubIssueEndPoint {
    @GET("/repos/{owner}/{repo}/issues/{number}")
    Call <GitHubIssue> getIssue(@Path("owner") String owner, @Path("repo") String repo, @Path("number") String integer);
}
