package com.androidcoding.codinf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import model.GitHubIssue;
import rest.ApiClient;
import rest.GitHubIssueEndPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Issues extends AppCompatActivity {

    //Initialise values
    EditText repositoryName, issueNum;
    Button issue;
    String ownerName;
    TextView users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        //Assign values
        Bundle extras = getIntent().getExtras();
        ownerName = extras.getString("issues");

        users = findViewById(R.id.userIssue);
        users.setText(ownerName);

        repositoryName = findViewById(R.id.repositoryName);
        issueNum = findViewById(R.id.issueNum);

        issue = findViewById(R.id.btn_issue);
        issue.setOnClickListener(this::onClick);
    }


    //Perform onClick
    public void onClick(View view){
        if(view.getId() == R.id.btn_issue){
            Intent intent = new Intent(Issues.this, IssueActivity.class);
            String projectName = repositoryName.getText().toString();
            String issueNumber = issueNum.getText().toString();

            final GitHubIssueEndPoint apiService = ApiClient.getClient().create(GitHubIssueEndPoint.class);
            Call<GitHubIssue> call = apiService.getIssue(ownerName, projectName, issueNumber);



            call.enqueue(new Callback<GitHubIssue>() {
                @Override
                public void onResponse(Call<GitHubIssue> call, Response<GitHubIssue> response) {
                    if(response.isSuccessful()){
                        try{
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        intent.putExtra("ownerName", ownerName);
                        intent.putExtra("repoName", projectName);
                        intent.putExtra("issueNum", issueNumber);

                        repositoryName.setHint("Name of Project");
                        issueNum.setHint("Issue Number");
                        startActivity(intent);

                    }

                    else{

                        if(repositoryName.getText().toString().equals("")){
                            Toast.makeText(Issues.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                        }

                        else if(issueNum.getText().toString().equals("")){
                            Toast.makeText(Issues.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Issues.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GitHubIssue> call, Throwable t) {

                }
            });
        }
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
            startActivity(new Intent(Issues.this, SecondActivity.class));
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