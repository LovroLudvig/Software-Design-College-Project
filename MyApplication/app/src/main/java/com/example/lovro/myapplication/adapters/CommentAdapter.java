package com.example.lovro.myapplication.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments){
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder viewHolder, int i) {
        Comment comment = comments.get(i);
        TextView username = viewHolder.itemView.findViewById(R.id.comment_username);
        TextView user_comment = viewHolder.itemView.findViewById(R.id.user_comment);

        //TODO dodati ovo za boju

        username.setText(comment.getUser().getUsername());
        user_comment.setText(comment.getText());


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        private View itemView;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
        }
    }

}
