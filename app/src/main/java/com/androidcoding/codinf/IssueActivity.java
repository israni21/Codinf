package com.androidcoding.codinf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import adapter.IssueAdapter;
import adapter.ReposAdapter;
import model.GitHubIssue;
import model.GitHubRepo;
import rest.ApiClient;
import rest.GitHubIssueEndPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueActivity extends AppCompatActivity {

    //Initialise values
    TextView projectName, title;
    String string1, string2, string3;
    RecyclerView recyclerView;
    ArrayList<GitHubIssue> gitHubIssueList = new ArrayList<>();
    RecyclerView.Adapter adapter;
    int integer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        //Assign values
        title = findViewById(R.id.title);

        Bundle extras = getIntent().getExtras();
        string1 = extras.getString("ownerName");
        string2 = extras.getString("repoName");
        string3 = extras.getString("issueNum");
        integer = Integer.parseInt(string3);

        recyclerView = findViewById(R.id.issue);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new IssueAdapter(gitHubIssueList, R.layout.list_item_issue, getApplicationContext());
        recyclerView.setAdapter(adapter);

        projectName = findViewById(R.id.projectName);
        projectName.setText(string2);


        //Function to load issue data
        loadIssueData();
    }


    //Implement function to load issue data
    private void loadIssueData(){
        final GitHubIssueEndPoint apiService = ApiClient.getClient().create(GitHubIssueEndPoint.class);
        Call<GitHubIssue> call = apiService.getIssue(string1, string2, string3);

        call.enqueue(new Callback<GitHubIssue>() {
            @Override
            public void onResponse(Call<GitHubIssue> call, Response<GitHubIssue> response) {
                title.setText(response.body().getTitle());
                gitHubIssueList.clear();
                gitHubIssueList.add(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GitHubIssue> call, Throwable t) {
                System.out.println("Failed!" + t.toString());
            }
        });
    }

    //click logout
    public void ClickLogOut(View view){
        //Close app
        Logout(this);
    }

    public void Logout(final Activity activity){
        //Initialise alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set title
        builder.setTitle("Logout");
        //Set message
        builder.setMessage("Are you sure you want to logout?");
        //Positive yes button
        builder.setPositiveButton("YES", (dialog, which) -> {
            //Finish activity
            activity.finishAffinity();

            //Go back to previous activity
            startActivity(new Intent(IssueActivity.this, SecondActivity.class));
        });
        //Negative no button
        builder.setNegativeButton("NO", (dialog, which) -> {
            //Dismiss dialog
            dialog.dismiss();
        });
        //Show dialog
        builder.show();
    }

    //click to go back
    public void ClickBack(View view){
        if(view.getId()==R.id.back_button)
        {
            finish();
        }
    }
}