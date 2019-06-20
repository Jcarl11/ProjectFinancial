package com.example.ratio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ratio.Entities.Image;
import com.example.ratio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Image> imageList;

    public ImageAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.image_row_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = imageList.get(position);
        holder.image_row_date.setText(image.getCreatedAt());
        holder.image_row_name.setText(image.getFileName().toUpperCase());
        Picasso.get().load(image.getFilePath()).into(holder.image_row_thumbnail);
    }

    @Override
    public int getItemCount() { return imageList.size(); }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image_row_thumbnail;
        TextView image_row_date, image_row_name;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image_row_date = (TextView) itemView.findViewById(R.id.image_row_date);
            image_row_name = (TextView) itemView.findViewById(R.id.image_row_name);
            image_row_thumbnail = (ImageView) itemView.findViewById(R.id.image_row_thumbnail);
        }
    }
}
