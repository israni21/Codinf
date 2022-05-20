package model;

import com.google.gson.annotations.SerializedName;
public class GitHubRepo {

    @SerializedName("name")
    private String name;


    public GitHubRepo(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
