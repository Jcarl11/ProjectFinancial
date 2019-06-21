package com.example.ratio.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ratio.Entities.User;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList = new ArrayList<>();

    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.users_row_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.user_row_name.setText(user.getUserinfo().getFullname());
        holder.user_row_username.setText(user.getUserinfo().getUsername());
        holder.user_row_email.setText(user.getUserinfo().getEmail());
        holder.user_row_verified.setText(user.getUserinfo().isVerified() == true ? "Verified" : "Unverified");
        holder.user_row_verified.setTextColor(user.getUserinfo().isVerified() == true ? Color.GREEN : Color.RED);
        holder.user_row_position.setText(user.getUserinfo().getPosition());
        holder.user_row_position.setTextColor(user.getUserinfo().getPosition().equalsIgnoreCase(Constant.PENDING) ? Color.RED : Color.GREEN);
        holder.user_row_status.setText(user.getUserinfo().getStatus());
        holder.user_row_status.setTextColor(user.getUserinfo().getStatus().equalsIgnoreCase(Constant.PENDING) ? Color.RED : Color.GREEN);
    }

    @Override
    public int getItemCount() { return userList.size(); }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView user_row_name, user_row_username, user_row_email, user_row_verified, user_row_position, user_row_status;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            user_row_name = (TextView) itemView.findViewById(R.id.user_row_name);
            user_row_username = (TextView) itemView.findViewById(R.id.user_row_username);
            user_row_email = (TextView) itemView.findViewById(R.id.user_row_email);
            user_row_verified = (TextView) itemView.findViewById(R.id.user_row_verified);
            user_row_position = (TextView) itemView.findViewById(R.id.user_row_position);
            user_row_status = (TextView) itemView.findViewById(R.id.user_row_status);
        }
    }
}
