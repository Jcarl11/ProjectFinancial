package com.example.projectfinancial.Adapters;

import android.view.View;
import android.widget.TextView;

import com.example.projectfinancial.Entities.ProjectsEntity;
import com.example.projectfinancial.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

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
        return R.id.portfolio_recyclerview_projectlist;
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

        @BindView(R.id.portfolio_rowlayout_projectname)
        public TextView portfolio_rowlayout_projectname;
        @BindView(R.id.portfolio_rowlayout_createdat)
        TextView portfolio_rowlayout_createdat;
        @BindView(R.id.portfolio_rowlayout_projectcode)
        TextView portfolio_rowlayout_projectcode;
        @BindView(R.id.portfolio_rowlayout_projectowner)
        TextView portfolio_rowlayout_projectowner;
        @BindView(R.id.portfolio_rowlayout_projectcategory)
        TextView portfolio_rowlayout_projectcategory;
        @BindView(R.id.portfolio_rowlayout_projecttype)
        TextView portfolio_rowlayout_projecttype;
        @BindView(R.id.portfolio_rowlayout_expenses)
        TextView portfolio_rowlayout_expenses;
        @BindView(R.id.portfolio_rowlayout_revenue)
        TextView portfolio_rowlayout_revenue;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView(ProjectAdapter item, List<Object> payloads) {
            portfolio_rowlayout_projectname.setText(item.projectsEntity.getProjectName());
            portfolio_rowlayout_createdat.setText(item.projectsEntity.getCreatedAt());
            portfolio_rowlayout_projectcode.setText(item.projectsEntity.getProjectCode());
            portfolio_rowlayout_projectowner.setText(item.projectsEntity.getProjectOwner());
            portfolio_rowlayout_projectcategory.setText(item.projectsEntity.getProjectCategory());
            portfolio_rowlayout_projecttype.setText(item.projectsEntity.getProjectType());
            portfolio_rowlayout_expenses.setText(item.projectsEntity.getProjectExpenses());
            portfolio_rowlayout_revenue.setText(item.projectsEntity.getProjectRevenue());
        }

        @Override
        public void unbindView(ProjectAdapter item) {
            portfolio_rowlayout_projectname.setText(null);
            portfolio_rowlayout_createdat.setText(null);
            portfolio_rowlayout_projectcode.setText(null);
            portfolio_rowlayout_projectowner.setText(null);
            portfolio_rowlayout_projectcategory.setText(null);
            portfolio_rowlayout_projecttype.setText(null);
            portfolio_rowlayout_expenses.setText(null);
            portfolio_rowlayout_revenue.setText(null);
        }
    }
}
