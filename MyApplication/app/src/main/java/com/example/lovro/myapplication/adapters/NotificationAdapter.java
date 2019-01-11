package com.example.lovro.myapplication.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.lovro.myapplication.domain.NotificationStorySuggest;
import com.example.lovro.myapplication.fragments.NotificationsFragment;
import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Notification;
import com.example.lovro.myapplication.domain.NotificationStyleSuggest;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lovro.myapplication.network.InitApiService.apiService;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private List<Notification> notifications;
    private String auth;
    private Context context;
    private RelativeLayout noNotificationsPanel;
    private NotificationsFragment.OnStyleClick onStyleClick;
    private ProgressDialog progressDialog;

    public NotificationAdapter(List<Notification> notificationStyleSuggests, String auth, Context context, RelativeLayout no_notifications_panel, NotificationsFragment.OnStyleClick onStyleClick){
        this.notifications = notificationStyleSuggests;
        this.auth=auth;
        this.context=context;
        this.noNotificationsPanel=no_notifications_panel;
        this.onStyleClick=onStyleClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_notification,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.ViewHolder holder, int position) {
        final Notification notification = notifications.get(position);
        holder.setIsRecyclable(false);


        if (notification instanceof NotificationStyleSuggest){
            final NotificationStyleSuggest notif=(NotificationStyleSuggest) notification;
            holder.adminStyleNotification.setVisibility(View.VISIBLE);
            holder.suggestedStoryNotification.setVisibility(View.GONE);
            holder.priceAdmin.setText("");
            holder.adNameAdmin.setText(notif.getOrder().getOffer().getName());
            holder.styleAdmin.setText(notif.getOrder().getStyle().getDescription());
            holder.notifUsernameAdmin.setText(notif.getOrder().getName());

            holder.acceptAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptStyle(holder, notif,notification);
                }
            });
            holder.denyAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    denyStyle(holder,notif,notification);
                }
            });
        }else if (notification instanceof NotificationStorySuggest){
            final NotificationStorySuggest notif=(NotificationStorySuggest) notification;
            holder.adminStyleNotification.setVisibility(View.GONE);
            holder.hiddenPanel.setVisibility(View.GONE);
            holder.showMorePanelText.setText("Show more");
            holder.showMorePanelPic.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            holder.suggestedStoryNotification.setVisibility(View.VISIBLE);
            holder.showMorePanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.showMorePanelText.getText().toString().equals("Show less")){
                        holder.hiddenPanel.setVisibility(View.GONE);
                        holder.showMorePanelText.setText("Show more");
                        holder.showMorePanelPic.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                    }else{
                        holder.hiddenPanel.setVisibility(View.VISIBLE);
                        holder.showMorePanelText.setText("Show less");
                        holder.showMorePanelPic.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                    }

                }
            });
            holder.storyUsername.setText(notif.getStory().getUser().getUsername());
            holder.storyText.setText(notif.getStory().getText());
            Picasso.get().load(notif.getStory().getImageUrl()).into(holder.storyImage);
            if (notif.getStory().getVideoUrl()==null){
                holder.videoLayout.setVisibility(View.GONE);
                holder.storyVideo.setVideoPath("");
            }else{
                holder.videoLayout.setVisibility(View.VISIBLE);
                holder.storyVideo.setVideoPath(notif.getStory().getVideoUrl());
                holder.storyVideoPlayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.storyVideo.isPlaying()){
                            holder.storyVideo.pause();
                            holder.storyVideoPlayButton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        }else{
                            holder.storyVideo.start();
                            holder.storyVideoPlayButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                        }

                    }
                });

            }
            holder.storyAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptStory(holder, notif, notification);
                }
            });
            holder.storyDenyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    denyStory(holder,notif,notification);
                }
            });


        }

    }

    private void denyStory(ViewHolder holder, NotificationStorySuggest notif, final Notification notification) {
        show_loading("Loading...");
        apiService.manageStory(auth, String.valueOf(notif.getStory().getId()), "false").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                stop_loading();
                if(response.isSuccessful()){
                    removeNotif(notification);
                    Toast.makeText(context, "Story denied", Toast.LENGTH_SHORT).show();
                }else{
                    if(call.isCanceled()){
                        //nothing
                    }else{
                        try {
                            showError(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stop_loading();
                Toast.makeText(context,"Error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void acceptStory(ViewHolder holder, NotificationStorySuggest notif, final Notification notification) {
        show_loading("Loading...");
        apiService.manageStory(auth, String.valueOf(notif.getStory().getId()), "true").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                stop_loading();
                if(response.isSuccessful()){
                    removeNotif(notification);
                    Toast.makeText(context, "Story accepted", Toast.LENGTH_SHORT).show();
                    //TODO: refresh story fragment here!!!!!!!!!!!!!!!!!!!!!!!!!!
                }else{
                    if(call.isCanceled()){
                        //nothing
                    }else{
                        try {
                            showError(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stop_loading();
                Toast.makeText(context,"Error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void denyStyle(ViewHolder holder, NotificationStyleSuggest notif, final Notification notification) {
        show_loading("Loading...");
        Call<ResponseBody> manageOrder = apiService.manageOrder(auth, String.valueOf(notif.getOrder().getId()), "false", "0");
        manageOrder.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                stop_loading();
                if(response.isSuccessful()){
                    removeNotif(notification);
                    onStyleClick.hideKeyboardAndTriggerEvent();
                    Toast.makeText(context, "Style denied", Toast.LENGTH_SHORT).show();
                }else{
                    if(call.isCanceled()){
                        //nothing
                    }else{
                        try {
                            showError(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stop_loading();
                Toast.makeText(context,"Error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }

    private void acceptStyle(ViewHolder holder, NotificationStyleSuggest notif, final Notification notification) {
        if (holder.priceAdmin.getText().toString().equals("")){
            Toast.makeText(context, "Please enter price", Toast.LENGTH_SHORT).show();
        }else {
            show_loading("Loading...");
            Call<ResponseBody> manageOrder = apiService.manageOrder(auth, String.valueOf(notif.getOrder().getId()), "true", holder.priceAdmin.getText().toString());
            manageOrder.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    stop_loading();
                    if(response.isSuccessful()){
                        removeNotif(notification);
                        onStyleClick.hideKeyboardAndTriggerEvent();
                        Toast.makeText(context, "Style accepted", Toast.LENGTH_SHORT).show();
                    }else{
                        if(call.isCanceled()){
                            //nothing
                        }else{
                            try {
                                showError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    stop_loading();
                    Toast.makeText(context,"Error", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        }

    }


    public void setNotifs(List<Notification> notificationStyleSuggests) {
        this.notifications = notificationStyleSuggests;
        if (this.notifications.size()!=0){
            noNotificationsPanel.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    public void removeNotif(Notification notification){
        notifications.remove(notification);
        notifyDataSetChanged();
        if (this.notifications.size()==0){
            noNotificationsPanel.setVisibility(View.VISIBLE);
        }
    }

    protected void showError(String message){
        new AlertDialog.Builder(context)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void show_loading(String message){
        progressDialog = ProgressDialog.show(context,"",message,true,false);
    }

    public void stop_loading(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private Button acceptAdmin;
        private Button denyAdmin;
        private TextView adNameAdmin;
        private EditText priceAdmin;
        private TextView styleAdmin;
        private TextView notifUsernameAdmin;
        private LinearLayout adminStyleNotification;


        private LinearLayout suggestedStoryNotification;
        private LinearLayout showMorePanel;
        private LinearLayout hiddenPanel;
        private TextView showMorePanelText;
        private ImageView showMorePanelPic;
        private TextView storyUsername;
        private TextView storyText;
        private ImageView storyImage;
        private VideoView storyVideo;
        private LinearLayout videoLayout;
        private ImageView storyVideoPlayButton;
        private Button storyAcceptButton;
        private Button storyDenyButton;


        private LinearLayout storyAllowedPanel;
        private View notifSeenButton;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;


            denyAdmin = itemView.findViewById(R.id.deny_admin);
            acceptAdmin = itemView.findViewById(R.id.accept_admin);
            adNameAdmin = itemView.findViewById(R.id.ad_name_admin);
            priceAdmin = itemView.findViewById(R.id.price_admin);
            styleAdmin = itemView.findViewById(R.id.syle_name_admin);
            notifUsernameAdmin = itemView.findViewById(R.id.notif_userName_admin);
            adminStyleNotification=itemView.findViewById(R.id.admin_style_notification);


            suggestedStoryNotification=itemView.findViewById(R.id.suggestedStoryNotification);
            showMorePanel=itemView.findViewById(R.id.showMorePanel);
            hiddenPanel=itemView.findViewById(R.id.hiddenPanel);
            showMorePanelText=itemView.findViewById(R.id.showMorePanelText);
            showMorePanelPic=itemView.findViewById(R.id.showMorePanelPic);
            storyUsername=itemView.findViewById(R.id.storyUsername);
            storyText=itemView.findViewById(R.id.storyText);
            storyImage=itemView.findViewById(R.id.storyImage);
            storyVideo=itemView.findViewById(R.id.storyVideo);
            videoLayout=itemView.findViewById(R.id.videoLayout);
            storyVideoPlayButton=itemView.findViewById(R.id.storyVideoPlayButton);
            storyAcceptButton=itemView.findViewById(R.id.storyAccept);
            storyDenyButton=itemView.findViewById(R.id.storyDeny);


            storyAllowedPanel=itemView.findViewById(R.id.storyAllowedPanel);
            notifSeenButton=itemView.findViewById(R.id.notifSeenButton);
        }
    }



}
