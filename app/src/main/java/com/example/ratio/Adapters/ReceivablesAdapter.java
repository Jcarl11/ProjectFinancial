package com.example.ratio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ratio.Entities.Receivables;
import com.example.ratio.HelperClasses.CurrencyFormat;
import com.example.ratio.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceivablesAdapter extends RecyclerView.Adapter<ReceivablesAdapter.ReceivablesViewHolder> {
    private CurrencyFormat currencyFormat = new CurrencyFormat();
    private Context context;
    private List<Receivables> receivablesList = new ArrayList<>();

    public ReceivablesAdapter(Context context, List<Receivables> receivablesList) {
        this.context = context;
        this.receivablesList = receivablesList;
    }

    @NonNull
    @Override
    public ReceivablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recievables_row_layout, parent, false);
        return new ReceivablesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivablesViewHolder holder, int position) {
        Receivables receivables = receivablesList.get(position);
        holder.receivables_row_date.setText(receivables.getTimestamp());
        holder.receivables_row_attachments.setText(String.valueOf(receivables.isAttachments()));
        holder.receivables_row_desc.setText(receivables.getDescription());
        holder.receivables_row_amount.setText(currencyFormat.toPhp(Double.parseDouble(receivables.getAmount())));
    }

    @Override
    public int getItemCount() { return receivablesList.size(); }

    public class ReceivablesViewHolder extends RecyclerView.ViewHolder{
        TextView receivables_row_date;
        TextView receivables_row_attachments;
        TextView receivables_row_desc;
        TextView receivables_row_amount;
        public ReceivablesViewHolder(@NonNull View itemView) {
            super(itemView);
            receivables_row_date = (TextView) itemView.findViewById(R.id.receivables_row_date);
            receivables_row_attachments = (TextView) itemView.findViewById(R.id.receivables_row_attachments);
            receivables_row_desc = (TextView) itemView.findViewById(R.id.receivables_row_desc);
            receivables_row_amount = (TextView) itemView.findViewById(R.id.receivables_row_amount);
        }
    }
}
