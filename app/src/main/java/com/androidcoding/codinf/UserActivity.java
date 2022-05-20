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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import model.GitHubUsers2;
import rest.ApiClient;
import rest.GitHubUserEndPoint2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    //Initialise values
    ImageView avatar;
    TextView username, login, followers, following, createdAt, updatedAt;
    Button btn_rep;
    Bundle extras;
    String newString;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Assign values
        avatar = findViewById(R.id.avatar);
        username = findViewById(R.id.username);
        login = findViewById(R.id.login);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        createdAt = findViewById(R.id.created_at);
        updatedAt = findViewById(R.id.updated_at);


        extras = getIntent().getExtras();
        newString = extras.getString("username");

        btn_rep = findViewById(R.id.btn_rep);
        btn_rep.setOnClickListener(this::onClick);

        //Function to load user data
        loadUserData();
    }

    //Perform onclick
    public void onClick(View view) {
        if (view.getId() == R.id.btn_rep){
            Intent intent = new Intent(UserActivity.this, Repositories.class);
            intent.putExtra("username_String", newString);
            startActivity(intent);
        }
    }

    //Download image
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... strings){
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    //Implement function to load user data
    private void loadUserData(){
        final GitHubUserEndPoint2 apiService = ApiClient.getClient().create(GitHubUserEndPoint2.class);
        Call<GitHubUsers2> call = apiService.getUser(newString);
        call.enqueue(new Callback<GitHubUsers2>() {
            @Override
            public void onResponse(Call<GitHubUsers2> call, Response<GitHubUsers2> response) {

                ImageDownloader task = new ImageDownloader();
                try{
                    assert response.body() != null;
                    bitmap = task.execute(response.body().getAvatar()).get();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }

                avatar.setImageBitmap(bitmap);
                avatar.getLayoutParams().height = 600;
                avatar.getLayoutParams().width = 400;


                if (response.body().getName() == null){
                    username.setText("No name provided");
                }

                else{
                    username.setText("Username: " + response.body().getName());
                }

                login.setText("Login: " + response.body().getLogin());
                followers.setText("Followers: " + response.body().getFollowers());
                following.setText("Following: " + response.body().getFollowing());
                createdAt.setText("Created At: " + response.body().getCreated_at());
                updatedAt.setText("Updated At: " + response.body().getUpdated_at());


            }

            @Override
            public void onFailure(Call<GitHubUsers2> call, Throwable t) {

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
            startActivity(new Intent(UserActivity.this, SecondActivity.class));
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