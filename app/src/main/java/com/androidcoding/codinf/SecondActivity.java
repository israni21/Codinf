package com.androidcoding.codinf;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import model.GitHubUsers1;
import rest.ApiClient;
import rest.GitHubUserEndPoint1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {

    //Initialise values
    private EditText userName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Assign value
        userName = findViewById(R.id.enter_username);
        Button login = findViewById(R.id.btn_login);


        //Implement OnClickListener
        login.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
            String user = userName.getText().toString();

            final GitHubUserEndPoint1 apiService = ApiClient.getClient().create(GitHubUserEndPoint1.class);
            Call<GitHubUsers1> call = apiService.getUser(user);

            call.enqueue(new Callback<GitHubUsers1>() {
                @Override
                public void onResponse(Call<GitHubUsers1> call, Response<GitHubUsers1> response) {

                    if(response.isSuccessful()){
                        try{
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                        intent.putExtra("username_String", user);

                        userName.setHint("Username");
                        startActivity(intent);

                    }

                    else{
                        if(userName.getText().toString().equals("")) {
                            Toast.makeText(SecondActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(SecondActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<GitHubUsers1> call, Throwable t) {

                }
            });
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
            startActivity(new Intent(SecondActivity.this, MainActivity.class));
        });
        //Negative no button
        builder.setNegativeButton("NO", (dialog, which) -> {
            //Dismiss dialog
            dialog.dismiss();
        });
        //Show dialog
        builder.show();
    }
}