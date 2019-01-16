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
import android.widget.ImageView;
import android.widget.TextView;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.Colors;
import com.craftery.lovro.myapplication.domain.Story;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private List<Story> stories;
    private StoryAdapter.OnShowClickListener onShowClickListener;


    public StoryAdapter(List<Story> stories){
        this.stories = stories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_story,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder viewHolder, int i) {
        final Story story = stories.get(i);
        ImageView imageView = viewHolder.itemView.findViewById(R.id.story_image);
        TextView storyDesc = viewHolder.itemView.findViewById(R.id.story_desc);
        //ImageView profileImage = viewHolder.itemView.findViewById(R.id.profile_image);
        TextView textView = viewHolder.itemView.findViewById(R.id.username_story);
        TextView userLetter=viewHolder.itemView.findViewById(R.id.profile_image_text);
        userLetter.setText(String.valueOf(story.getUser().getUsername().toUpperCase().charAt(0)));



        //((TextView)).setText(story.getUser().getUsername().charAt(0));
        Drawable background = viewHolder.itemView.findViewById(R.id.profile_image_circle).getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Colors.getColor((int) story.getUser().getUsername().toLowerCase().charAt(0)-97));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Colors.getColor((int) story.getUser().getUsername().toLowerCase().charAt(0)-97));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Colors.getColor((int) story.getUser().getUsername().toLowerCase().charAt(0)-97));
        }

        Picasso.get().load(story.getImageUrl()).into(imageView);
        storyDesc.setText(story.getText());
        textView.setText(story.getUser().getUsername());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowClickListener.onShowClick(story);
            }
        };

        viewHolder.itemView.setOnClickListener(listener);
    }


    public void setStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View itemView;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
        }
    }

    public void setListener(StoryAdapter.OnShowClickListener listener){
        this.onShowClickListener = listener;
    }


    public interface OnShowClickListener{
        void onShowClick(Story story);
    }
}
