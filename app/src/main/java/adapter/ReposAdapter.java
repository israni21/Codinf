package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcoding.codinf.R;

import java.util.ArrayList;

import model.GitHubRepo;


public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ReposViewHolder> {

    //Initialise
    private ArrayList<GitHubRepo> repos;
    private int rowLayout;
    private Context context;

    //Constructor
    public ReposAdapter(ArrayList<GitHubRepo> repos, int rowLayout, Context context){
        this.repos = repos;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    //Getter and setter methods
    public ArrayList<GitHubRepo> getRepos() {
        return repos;
    }

    public void setRepos(ArrayList<GitHubRepo> repos) {
        this.repos = repos;
    }

    public int getRowLayout() {
        return rowLayout;
    }

    public void setRowLayout(int rowLayout) {
        this.rowLayout = rowLayout;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ReposAdapter.ReposViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new ReposViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReposAdapter.ReposViewHolder holder, int position) {

        holder.repoName.setText(repos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public class ReposViewHolder extends RecyclerView.ViewHolder{
        LinearLayout reposLayout;
        TextView repoName;

        public ReposViewHolder(@NonNull View itemView){
            super(itemView);
           reposLayout = itemView.findViewById(R.id.repo_item_layout);
           repoName = itemView.findViewById(R.id.repoName);
        }
    }
}
