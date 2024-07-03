package com.techtravelcoder.alluniversityinformations.notes;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsAdapter<FriendViewHolder> extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {



    @NonNull
    @Override
    public FriendsAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
