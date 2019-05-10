package com.example.ratio.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Status;
import com.example.ratio.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectAdapter extends  RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private static final String TAG = "ProjectAdapter";
    private Context context;
    private List<Projects> projectsList;

    public ProjectAdapter(Context context, List<Projects> projectsList) {
        this.context = context;
        this.projectsList = projectsList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.portfolio_rowlayout, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Projects projects = projectsList.get(position);
        holder.portfolio_row_code.setText(projects.getProjectCode());
        StringBuilder statusBuilder = new StringBuilder();
        for(Status status : projects.getProjectStatus()){
            statusBuilder.append(status.getName());
            statusBuilder.append(",");
        }
        holder.portfolio_row_status.setText(statusBuilder.toString());
        holder.portfolio_row_projectname.setText(projects.getProjectName());
        Picasso.get().load(projects.getThumbnail().getFilePath()).into(holder.portfolio_row_image);
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView portfolio_row_code;
        TextView portfolio_row_status;
        TextView portfolio_row_projectname;
        ImageView portfolio_row_image;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            portfolio_row_code = (TextView) itemView.findViewById(R.id.portfolio_row_code);
            portfolio_row_status = (TextView) itemView.findViewById(R.id.portfolio_row_status);
            portfolio_row_projectname = (TextView) itemView.findViewById(R.id.portfolio_row_projectname);
            portfolio_row_image = (ImageView) itemView.findViewById(R.id.portfolio_row_image);
        }
    }
}
