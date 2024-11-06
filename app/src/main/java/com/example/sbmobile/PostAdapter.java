package com.example.sbmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.titleTextView.setText(post.getTitle());
        holder.descriptionTextView.setText(post.getDescription());
        if (!post.getImageUrl().isEmpty()) {
            Glide.with(holder.imageView.getContext()).load(post.getImageUrl()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image); // Usa una imagen placeholder si no hay imagen
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView imageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
        //    titleTextView = itemView.findViewById(R.id.postTitle);
            //           descriptionTextView = itemView.findViewById(R.id.postDescription);
            //       imageView = itemView.findViewById(R.id.postImage);
        }
    }
}
