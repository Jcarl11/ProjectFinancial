package com.example.ratio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ratio.Entities.Pdf;
import com.example.ratio.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private Context context;
    private List<Pdf> pdfList = new ArrayList<>();

    public FileAdapter(Context context, List<Pdf> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.file_row_layout, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        Pdf pdf = pdfList.get(position);
        holder.file_row_date.setText(pdf.getCreatedAt());
        holder.file_row_name.setText(pdf.getFileName());
    }

    @Override
    public int getItemCount() { return pdfList.size(); }

    public class FileViewHolder extends RecyclerView.ViewHolder{
        TextView file_row_date, file_row_name;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            file_row_date = (TextView) itemView.findViewById(R.id.file_row_date);
            file_row_name = (TextView) itemView.findViewById(R.id.file_row_name);
        }
    }
}
