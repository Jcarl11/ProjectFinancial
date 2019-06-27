package com.example.ratio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ratio.Entities.Income;
import com.example.ratio.HelperClasses.CurrencyFormat;
import com.example.ratio.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {
    private CurrencyFormat currencyFormat = new CurrencyFormat();
    private Context context;
    private List<Income> incomeList;

    public IncomeAdapter(Context context, List<Income> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.income_row_layout, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Income income = incomeList.get(position);
        holder.income_row_date.setText(income.getTimestamp());
        holder.income_row_attachments.setText(String.valueOf(income.isAttachments()));
        holder.income_row_desc.setText(income.getDescription());
        holder.income_row_amount.setText(currencyFormat.toPhp(Double.parseDouble(income.getAmount())));
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder{
        TextView income_row_date;
        TextView income_row_attachments;
        TextView income_row_desc;
        TextView income_row_amount;
        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            income_row_date = (TextView) itemView.findViewById(R.id.income_row_date);
            income_row_attachments = (TextView) itemView.findViewById(R.id.income_row_attachments);
            income_row_desc = (TextView) itemView.findViewById(R.id.income_row_desc);
            income_row_amount = (TextView) itemView.findViewById(R.id.income_row_amount);
        }
    }
}
