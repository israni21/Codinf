package com.androidcoding.codinf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import android.widget.Button;

import adapter.ReposAdapter;
import model.GitHubRepo;
import rest.ApiClient;
import rest.GitHubRepoEndPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repositories extends AppCompatActivity {

    //Initialise values
    String newString;
    TextView users;
    Button btn_issues;
    RecyclerView recyclerView;
    Bundle extras;
    ArrayList<GitHubRepo> gitHubRepoList = new ArrayList<>();
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        //Assign values
        extras = getIntent().getExtras();
        newString = extras.getString("username_String");

        users = findViewById(R.id.users);
        users.setText(newString);


        recyclerView = findViewById(R.id.repositories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReposAdapter(gitHubRepoList, R.layout.list_item_repo, getApplicationContext());
        recyclerView.setAdapter(adapter);

        btn_issues = findViewById(R.id.btn_issues);
        btn_issues.setOnClickListener(this::onClick);


        //Function to load repositories
        loadRepositories();

    }

    //Perform onClick
    public void onClick(View view) {
        if (view.getId() == R.id.btn_issues){
            Intent intent = new Intent(Repositories.this, Issues.class);
            intent.putExtra("issues", newString);
            startActivity(intent);
        }
    }

    //Implement function to load repositories
    private void loadRepositories(){
        GitHubRepoEndPoint apiService = ApiClient.getClient().create(GitHubRepoEndPoint.class);
        Call<ArrayList<GitHubRepo>> call = apiService.getRepo(newString);
        call.enqueue(new Callback<ArrayList<GitHubRepo>>() {
            @Override
            public void onResponse(Call<ArrayList<GitHubRepo>> call, Response<ArrayList<GitHubRepo>> response) {

                gitHubRepoList.clear();
                gitHubRepoList.addAll(response.body());
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<ArrayList<GitHubRepo>> call, Throwable t) {

                Log.d("Repos", t.toString());
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
            startActivity(new Intent(Repositories.this, SecondActivity.class));
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