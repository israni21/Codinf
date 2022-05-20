package com.androidcoding.codinf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class ThirdActivity extends AppCompatActivity {

    //Initialise values
    ImageView imageView;
    TextView textView;
    Bundle extras;
    String newString;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //Assign values
        imageView = findViewById(R.id.profile);
        textView = findViewById(R.id.user);

        extras = getIntent().getExtras();
        newString = extras.getString("username_String");

        loadUserData();
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


                imageView.setImageBitmap(bitmap);

                if (response.body().getName() == null){
                    textView.setText("No name provided");
                }

                else{
                    textView.setText(response.body().getName());
                }

            }

            @Override
            public void onFailure(Call<GitHubUsers2> call, Throwable t) {

                System.out.println("Failed!" + t.toString());
            }
        });
    }

    //click to go back
    public void ClickBack(View view){
        if(view.getId()==R.id.back_button)
        {
            finish();
        }
    }

    //click to know info
    public void ClickInfo(View view){
        if(view.getId()==R.id.info)
        {
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            intent.putExtra("username", newString);
            startActivity(intent);
        }
    }

    //click to get repositories
    public void ClickRepositories(View view){
        if(view.getId()==R.id.findRepo)
        {
            Intent intent = new Intent(getApplicationContext(), Repositories.class);
            intent.putExtra("username_String", newString);
            startActivity(intent);
        }
    }

    //click to get issue
    public void ClickIssue(View view){
        if(view.getId()==R.id.getIssue)
        {
            Intent intent = new Intent(getApplicationContext(), Issues.class);
            intent.putExtra("issues", newString);
            startActivity(intent);
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
            startActivity(new Intent(ThirdActivity.this, SecondActivity.class));
        });
        //Negative no button
        builder.setNegativeButton("NO", (dialog, which) -> {
            //Dismiss dialog
            dialog.dismiss();
        });
        //Show dialog
        builder.show();
    }

    //click switch
    public void ClickSwitch(View view)
    {
        startActivity(new Intent(ThirdActivity.this, SecondActivity.class));
    }

}