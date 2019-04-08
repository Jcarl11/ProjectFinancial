package com.example.ratio.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ratio.Entities.ProjectsEntity;
import com.example.ratio.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectAdapter extends AbstractItem<ProjectAdapter, ProjectAdapter.ViewHolder> {

    private ProjectsEntity projectsEntity;

    public ProjectAdapter(ProjectsEntity projectsEntity) {
        this.projectsEntity = projectsEntity;
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

        @BindView(R.id.portfolio_row_code)
        public TextView portfolio_row_code;
        @BindView(R.id.portfolio_row_image)
        ImageView portfolio_row_image;
        @BindView(R.id.portfolio_row_status)
        TextView portfolio_row_status;
        @BindView(R.id.portfolio_row_projectname)
        TextView portfolio_row_projectname;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView(ProjectAdapter item, List<Object> payloads) {
            portfolio_row_code.setText(item.projectsEntity.getProjectCode());
            portfolio_row_status.setText(item.projectsEntity.getProjectStatus());
            portfolio_row_projectname.setText(item.projectsEntity.getProjectName());
            Picasso.get().load(item.projectsEntity.getImagePath()).into(portfolio_row_image);
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
