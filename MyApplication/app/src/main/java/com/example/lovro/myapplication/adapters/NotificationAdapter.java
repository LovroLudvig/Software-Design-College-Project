package com.example.lovro.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovro.myapplication.fragments.NotificationsFragment;
import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Notification;
import com.example.lovro.myapplication.domain.NotificationStyleSuggest;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lovro.myapplication.network.InitApiService.apiService;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private List<Notification> notificationStyleSuggests;
    private String auth;
    private Context context;
    private RelativeLayout noNotificationsPanel;
    private NotificationsFragment.OnStyleClick onStyleClick;

    public NotificationAdapter(List<Notification> notificationStyleSuggests, String auth, Context context, RelativeLayout no_notifications_panel, NotificationsFragment.OnStyleClick onStyleClick){
        this.notificationStyleSuggests = notificationStyleSuggests;
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
        final Notification notification = notificationStyleSuggests.get(position);


        if (notification instanceof NotificationStyleSuggest){
            holder.adminStyleNotification.setVisibility(View.VISIBLE);
            final NotificationStyleSuggest notif=(NotificationStyleSuggest) notification;
            holder.adNameAdmin.setText(notif.getOrder().getOffer().getName());
            holder.styleAdmin.setText(notif.getOrder().getStyle().getDescription());
            holder.notifUsernameAdmin.setText(notif.getOrder().getName());
            holder.acceptAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.priceAdmin.getText().toString().equals("")){
                        Toast.makeText(context, "Please enter price", Toast.LENGTH_SHORT).show();
                    }else {
                        Call<ResponseBody> manageOrder = apiService.manageOrder(auth, String.valueOf(notif.getOrder().getId()), "true", holder.priceAdmin.getText().toString());
                        manageOrder.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                                Toast.makeText(context,"Error loading orders", Toast.LENGTH_LONG).show();
                                t.printStackTrace();
                            }
                        });
                    }
                }
            });
            holder.denyAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<ResponseBody> manageOrder = apiService.manageOrder(auth, String.valueOf(notif.getOrder().getId()), "false", "0");
                    manageOrder.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                            Toast.makeText(context,"Error loading orders", Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });
                }
            });
        }

    }


    public void setNotifs(List<Notification> notificationStyleSuggests) {
        this.notificationStyleSuggests = notificationStyleSuggests;
        if (this.notificationStyleSuggests.size()!=0){
            noNotificationsPanel.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    public void removeNotif(Notification notification){
        for (Notification notif:this.notificationStyleSuggests){
            if (notif.equals(notification)){
                this.notificationStyleSuggests.remove(notif);
                notifyDataSetChanged();
            }
        }
        if (this.notificationStyleSuggests.size()==0){
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
        return notificationStyleSuggests.size();
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

        }
    }



}
