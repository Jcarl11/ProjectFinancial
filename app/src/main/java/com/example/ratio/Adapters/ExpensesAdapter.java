package com.example.ratio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ratio.Entities.Expenses;
import com.example.ratio.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {
    private Context context;
    private List<Expenses> expensesList = new ArrayList<>();

    public ExpensesAdapter(Context context, List<Expenses> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.expenses_row_layout, parent, false);
        return new ExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {
        Expenses expenses = expensesList.get(position);
        holder.expenses_row_date.setText(expenses.getTimestamp());
        holder.expenses_row_attachments.setText(String.valueOf(expenses.isAttachments()));
        holder.expenses_row_desc.setText(expenses.getDescription());
        holder.expenses_row_amount.setText(String.format("Php %s", expenses.getAmount()));
    }

    @Override
    public int getItemCount() { return expensesList.size(); }

    public class ExpensesViewHolder extends RecyclerView.ViewHolder{
        TextView expenses_row_date;
        TextView expenses_row_attachments;
        TextView expenses_row_desc;
        TextView expenses_row_amount;
        public ExpensesViewHolder(@NonNull View itemView) {
            super(itemView);
            expenses_row_date = (TextView) itemView.findViewById(R.id.expenses_row_date);
            expenses_row_attachments = (TextView) itemView.findViewById(R.id.expenses_row_attachments);
            expenses_row_desc = (TextView) itemView.findViewById(R.id.expenses_row_desc);
            expenses_row_amount = (TextView) itemView.findViewById(R.id.expenses_row_amount);
        }
    }
}
