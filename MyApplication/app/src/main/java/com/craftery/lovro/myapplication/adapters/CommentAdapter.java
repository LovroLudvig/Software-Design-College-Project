package com.craftery.lovro.myapplication.adapters;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.Colors;
import com.craftery.lovro.myapplication.domain.Comment;

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

        if (comment.getUser()==null){
            ((TextView)viewHolder.itemView.findViewById(R.id.profile_image_text)).setText(String.valueOf("G"));
            Drawable background = viewHolder.itemView.findViewById(R.id.profile_image_circle).getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(Colors.getColor(((int) 'g')-97));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(Colors.getColor(((int) 'g')-97));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(Colors.getColor(((int) 'g')));
            }
        }else{
            ((TextView)viewHolder.itemView.findViewById(R.id.profile_image_text)).setText(String.valueOf(comment.getUser().getUsername().toUpperCase().charAt(0)));
            Drawable background = viewHolder.itemView.findViewById(R.id.profile_image_circle).getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(Colors.getColor((int) comment.getUser().getUsername().toLowerCase().charAt(0)-97));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(Colors.getColor((int) comment.getUser().getUsername().toLowerCase().charAt(0)-97));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(Colors.getColor((int) comment.getUser().getUsername().toLowerCase().charAt(0)-97));
            }
        }

        if(comment.getUser() == null){
            username.setText("Guest");
        }else{
            username.setText(comment.getUser().getUsername());
        }
        user_comment.setText(comment.getText());


    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
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
