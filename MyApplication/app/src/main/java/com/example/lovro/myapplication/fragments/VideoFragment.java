package com.example.lovro.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Story;

@SuppressLint("ValidFragment")
public class VideoFragment extends Fragment {
    private VideoView videoView;
    private ImageView play_button;
    private boolean video_start = false;
    private Story story;

    @SuppressLint("ValidFragment")
    public VideoFragment(Story story){
        this.story = story;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videoview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoView = view.findViewById(R.id.videoView);
        videoView.setVideoPath(story.getVideoUrl());
        play_button = view.findViewById(R.id.play_video_button);

        initListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners(){
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(video_start){
                    if(videoView.isPlaying()) {
                        videoView.pause();
                        video_start = false;
                        play_button.setVisibility(View.VISIBLE);
                    }
                }else{
                    video_start = true;
                    play_button.setVisibility(View.INVISIBLE);
                    videoView.start();
                }
                return false;
            }
        });

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
