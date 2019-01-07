package com.example.lovro.myapplication.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Notification;
import com.example.lovro.myapplication.domain.Offer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications){
        this.notifications = notifications;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_notification,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        final Notification notification = notifications.get(position);

        holder.notifText.setText(notification.getText());
        holder.userName.setText(notification.getUserWhoCaused());
        holder.notifDate.setText(notification.getDate());

        //TODO REMOVE MOCK Always the same value (mocked)
        //holder.userFoto.setImageResource(R.drawable.app_logo);
        //Picasso.get().load(offer.getImageUrl()).into(offerImage);


        //TODO Functionality - set up listener

    }


    public void setOffers(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private ImageView userFoto;
        private TextView userName;
        private TextView notifText;
        private TextView notifDate;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;

            userFoto = itemView.findViewById(R.id.notif_picture);
            userName = itemView.findViewById(R.id.notif_userName);
            notifText = itemView.findViewById(R.id.notif_text);
            notifDate = itemView.findViewById(R.id.notif_date);

        }
    }



}
