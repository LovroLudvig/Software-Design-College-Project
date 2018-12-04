package com.example.lovro.myapplication;

import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<Notification> getNotifications(){
        return new ArrayList<>();
    }

    private void doNotificationAction(){}

    private boolean removeNotification(){
        return true;
    }
}
