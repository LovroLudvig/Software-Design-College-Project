package com.craftery.lovro.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.DoubleClickListener;
import com.craftery.lovro.myapplication.domain.Story;
import com.squareup.picasso.Picasso;

@SuppressLint("ValidFragment")
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

        initListeners();
    }

    private void initListeners(){
        story_image.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
            }

            @Override
            public void onDoubleClick(View v) {
                open_fullscreen();
            }
        });
    }

    private void open_fullscreen(){
        Dialog builder = new Dialog(getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(getContext());
        Picasso.get().load(story.getImageUrl()).into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
