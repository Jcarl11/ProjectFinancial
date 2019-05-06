package com.example.ratio.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ratio.Entities.Projects;
import com.example.ratio.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;

public class ProjectAdapter extends AbstractItem<ProjectAdapter, ProjectAdapter.ViewHolder> {

    private Projects projects;

    public ProjectAdapter(Projects projects) {
        this.projects = projects;
    }


    @Override
    public int getType() {
        return R.id.portfolio_recyclerview;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.portfolio_rowlayout;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends FastAdapter.ViewHolder<ProjectAdapter> {

        TextView portfolio_row_code;
        ImageView portfolio_row_image;
        TextView portfolio_row_status;
        TextView portfolio_row_projectname;


        public ViewHolder(View itemView) {
            super(itemView);
            portfolio_row_code = (TextView) itemView.findViewById(R.id.portfolio_row_code);
            portfolio_row_image = (ImageView) itemView.findViewById(R.id.portfolio_row_image);
            portfolio_row_status = (TextView) itemView.findViewById(R.id.portfolio_row_status);
            portfolio_row_projectname = (TextView) itemView.findViewById(R.id.portfolio_row_projectname);
        }

        @Override
        public void bindView(ProjectAdapter item, List<Object> payloads) {
            portfolio_row_code.setText(item.projects.getProjectCode());
            portfolio_row_status.setText(item.projects.getProjectStatus().getName());
            portfolio_row_projectname.setText(item.projects.getProjectName());
            Picasso.get().load(item.projects.getThumbnail().getFilePath()).into(portfolio_row_image);
        }

        @Override
        public void unbindView(ProjectAdapter item) {
            portfolio_row_code.setText(null);
            portfolio_row_status.setText(null);
            portfolio_row_projectname.setText(null);
            portfolio_row_image.setImageDrawable(null);
        }
    }
}
