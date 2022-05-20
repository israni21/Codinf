package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcoding.codinf.R;

import java.util.ArrayList;

import model.GitHubIssue;
import model.GitHubRepo;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> {

    //Initialise
    private ArrayList<GitHubIssue> issue;
    private int rowLayout;
    private Context context;

    //Constructor
    public IssueAdapter(ArrayList<GitHubIssue> issue, int rowLayout, Context context){
        this.issue = issue;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    //Getter and setter methods
    public ArrayList<GitHubIssue> getIssue() {
        return issue;
    }

    public void setIssue(ArrayList<GitHubIssue> issue) {
        this.issue = issue;
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
    public IssueAdapter.IssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new IssueAdapter.IssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueAdapter.IssueViewHolder holder, int position) {

        holder.issueBody.setText(issue.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return issue.size();
    }

    public class IssueViewHolder extends RecyclerView.ViewHolder{
        LinearLayout issueLayout;
        TextView issueBody;

        public IssueViewHolder(@NonNull View itemView){
            super(itemView);
           issueLayout = itemView.findViewById(R.id.issue_item_layout);
           issueBody = itemView.findViewById(R.id.issueBody);
        }
    }
}


