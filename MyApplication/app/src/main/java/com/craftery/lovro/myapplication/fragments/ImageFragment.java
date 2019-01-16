package com.craftery.lovro.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.Story;
import com.squareup.picasso.Picasso;

public class ImageFragment extends Fragment {

    private ImageView story_image;
    private Story story;

    @SuppressLint("ValidFragment")
    public ImageFragment(Story story){
        this.story = story;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imageview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        story_image = view.findViewById(R.id.storydetails_image);
        Picasso.get().load(story.getImageUrl()).into(story_image);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
