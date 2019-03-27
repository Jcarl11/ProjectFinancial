package com.example.projectfinancial;

import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.holder.StringHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PortfolioProjects extends AbstractItem<PortfolioProjects, PortfolioProjects.ViewHolder> {
    String portfolio_rowlayout_projectname;
    String portfolio_rowlayout_createdat;
    String portfolio_rowlayout_projectcode;
    String portfolio_rowlayout_projectowner;
    String portfolio_rowlayout_projectcategory;
    String portfolio_rowlayout_projecttype;
    String portfolio_rowlayout_expenses;
    String portfolio_rowlayout_revenue;


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


    public class ViewHolder extends FastAdapter.ViewHolder<PortfolioProjects> {

        @BindView(R.id.portfolio_rowlayout_projectname)
        TextView portfolio_rowlayout_projectname;
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
        public void bindView(PortfolioProjects item, List<Object> payloads) {
            portfolio_rowlayout_projectname.setText(item.portfolio_rowlayout_projectname);
            portfolio_rowlayout_createdat.setText(item.portfolio_rowlayout_createdat);
            portfolio_rowlayout_projectcode.setText(item.portfolio_rowlayout_projectcode);
            portfolio_rowlayout_projectowner.setText(item.portfolio_rowlayout_projectowner);
            portfolio_rowlayout_projectcategory.setText(item.portfolio_rowlayout_projectcategory);
            portfolio_rowlayout_projecttype.setText(item.portfolio_rowlayout_projecttype);
            portfolio_rowlayout_expenses.setText(item.portfolio_rowlayout_expenses);
            portfolio_rowlayout_revenue.setText(item.portfolio_rowlayout_revenue);
        }

        @Override
        public void unbindView(PortfolioProjects item) {
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
