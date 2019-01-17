package com.craftery.lovro.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.domain.DoubleClickListener;
import com.craftery.lovro.myapplication.domain.Story;
import com.craftery.lovro.myapplication.events.PauseVideoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@SuppressLint("ValidFragment")
public class VideoFragment extends Fragment {
    private VideoView videoView;
    private ImageView play_button;
    private boolean video_start = false;
    private Story story;
    private FrameLayout relativeLayout;
    private Uri uriVideo=null;
    private boolean toast = true;

    @SuppressLint("ValidFragment")
    public VideoFragment(Story story){
        this.story = story;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        play_button = view.findViewById(R.id.play_video_button);
        relativeLayout = view.findViewById(R.id.fullscreen);
        initListeners();
        downloadVideo(story.getVideoUrl());
    }

    private void downloadVideo(String url) {
        File videoFile = null;
        try {
            videoFile = createVideoFile();
        } catch (IOException ex) {
        }
        if (videoFile != null) {
            uriVideo = FileProvider.getUriForFile(getContext(), "com.craftery.android.crafteryHrFileProvider", videoFile);
            downloadFile(url,new File(uriVideo.getPath()));
        }
    }


    //pise da se ne koristi ali koristi se!!!!!!!!!!!!! NEMOJ BRISAT
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPauseVideoEvent(PauseVideoEvent event) {
        videoView.pause();
        video_start = false;
        play_button.setVisibility(View.VISIBLE);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initListeners(){
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video_start = false;
                play_button.setVisibility(View.VISIBLE);

            }
        });
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

        relativeLayout.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
            }

            @Override
            public void onDoubleClick(View v) {
                videoView.pause();
                video_start = false;
                play_button.setVisibility(View.VISIBLE);
                open_fullscreen();
            }
        });


    }

    private void open_fullscreen(){
        LayoutInflater inflater= LayoutInflater.from(getContext());
        View view=inflater.inflate(R.layout.fullscreen_video, null);
        VideoView videoViewFullscreen = view.findViewById(R.id.new_video);
        videoViewFullscreen.setVideoURI(uriVideo);

        DisplayMetrics metrics = new DisplayMetrics(); getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoViewFullscreen.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoViewFullscreen.setLayoutParams(params);
        videoViewFullscreen.setZOrderOnTop(true);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setView(view);
        videoViewFullscreen.start();

        AlertDialog alert = alertDialog.create();
        alert.show();
    }


    private File createVideoFile() throws IOException {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File video = File.createTempFile(
                "VIDEO",  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );
        video.deleteOnExit();
        return video;
    }



    private void downloadFile(final String url, final File outputFile) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                URL u;
                InputStream is = null;

                try {
                    u = new URL(url);
                    is = u.openStream();
                    HttpURLConnection huc = (HttpURLConnection)u.openConnection(); //to know the size of video

                    if(huc != null) {

                        FileOutputStream fos = new FileOutputStream(outputFile);
                        byte[] buffer = new byte[1024];
                        int len1 = 0;
                        if(is != null) {
                            while ((len1 = is.read(buffer)) > 0) {
                                fos.write(buffer,0, len1);
                            }
                        }
                        if(fos != null) {
                            fos.close();
                        }
                        if(getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    videoView.setVideoURI(uriVideo);
                                }
                            });
                        }

                    }
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                    Log.d("tag", "eror1");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("tag", "eror2");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("tag", "eror3");
                } finally {
                    try {
                        if(is != null) {
                            is.close();
                        }
                    } catch (IOException ioe) {
                        Log.d("tag", "eror3");
                    }
                }
            }
        });
        thread.start();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
