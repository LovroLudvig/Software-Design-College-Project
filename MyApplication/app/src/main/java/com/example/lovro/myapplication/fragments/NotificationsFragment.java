package com.example.lovro.myapplication.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.activities.HomeActivity;
import com.example.lovro.myapplication.adapters.NotificationAdapter;
import com.example.lovro.myapplication.domain.Notification;
import com.example.lovro.myapplication.domain.NotificationStorySuggest;
import com.example.lovro.myapplication.domain.NotificationStyleSuggest;
import com.example.lovro.myapplication.domain.Order;
import com.example.lovro.myapplication.domain.Role;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.events.StyleChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.lovro.myapplication.network.InitApiService.apiService;


public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private NotificationAdapter notifAdapter;
    private RelativeLayout no_notifications_panel;
    private List<Notification> notificationList = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.notif_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.notif_swipeLayout);
        progressBar = view.findViewById(R.id.notif_progressbar);
        no_notifications_panel=view.findViewById(R.id.no_notifications_panel);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

                initRecyclerView();
                notificationList=new ArrayList<>();
                notifAdapter.setNotifs(notificationList);

                getUserAndCheckForNotifications();

            }
        });
        if (userIsRegistered()){
            initRecyclerView();
            initNotificationAdapter(notificationList);
            view.findViewById(R.id.registered_user_panel).setVisibility(View.VISIBLE);
            view.findViewById(R.id.unregistered_user_panel).setVisibility(View.GONE);
            getUserAndCheckForNotifications();
        }else{
            view.findViewById(R.id.registered_user_panel).setVisibility(View.GONE);
            view.findViewById(R.id.unregistered_user_panel).setVisibility(View.VISIBLE);
        }



    }

    private void getUserAndCheckForNotifications() {
        loadUserFromAPI();
    }

    private boolean userIsRegistered(){
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        if(prefs.getBoolean("saved",false)){
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }




    private void displayNotifications(List<Notification> notificationStyleSuggestList){
        progressBar.setVisibility(View.GONE);
        if(notifAdapter != null){
            notifAdapter.setNotifs(notificationStyleSuggestList);
        }else{
            initNotificationAdapter(notificationStyleSuggestList);
        }
    }


    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadUserFromAPI(){
        progressBar.setVisibility(View.VISIBLE);
        Call<User> getUser = apiService.getUserByUsername2(getUserAuth(), getUserUsername());

        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    checkForNotifications(response.body());
                }else{
                    progressBar.setVisibility(View.GONE);
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
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(),"Error loading user", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void checkForNotifications(User user) {
        if (checkIfUserAdmin(user)){
            progressBar.setVisibility(View.VISIBLE);
            Call<List<Order>> getOrders = apiService.getAllOrders(getUserAuth());

            getOrders.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.isSuccessful()){
                        listAdminNotifications(response.body());
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
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Toast.makeText(getActivity(),"Error loading orders", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    t.printStackTrace();

                }
            });

        }else{
            //nije admin, za sad nema notifikacija
            no_notifications_panel.setVisibility(View.VISIBLE);
        }

    }

    private void listAdminNotifications(List<Order> orders) {

        if (orders.size()==0){
            no_notifications_panel.setVisibility(View.VISIBLE);
        }else{
            //TODO: IZLISTAJ
            notificationList=new ArrayList<>();
            notificationList.add(new NotificationStorySuggest());
            for (Order order:orders){
                notificationList.add(new NotificationStyleSuggest(order));
            }
            notifAdapter.setNotifs(notificationList);



        }

    }


    private String getUserUsername(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        return username;

    }

    private String getUserAuth(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        String pass = prefs.getString("password","");

        String token = username.trim()+":"+pass.trim();
        String encoded_token = Base64.encodeToString(token.getBytes(),0);
        String auth = "Basic"+" "+encoded_token.trim();

        return auth;
    }

    protected void showError(String message){
        new AlertDialog.Builder(getContext())
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
    }

    private boolean checkIfUserAdmin(User currentUser) {
        for (Role role:currentUser.getRoles()){
            if (role.getId()==4){
                return true;
            }
        }
        return false;
    }



    private void initNotificationAdapter(List<Notification> notificationStyleSuggestList){
        notifAdapter = new NotificationAdapter(notificationStyleSuggestList, getUserAuth(), getContext(),no_notifications_panel,new OnStyleClick(){


            @Override
            public void hideKeyboardAndTriggerEvent() {
                ((HomeActivity)getActivity()).hideKeyboard();
                EventBus bus = EventBus.getDefault();
                bus.post(new StyleChangeEvent());
            }
        });
        recyclerView.setAdapter(notifAdapter);
    }


    public interface OnStyleClick {
        void hideKeyboardAndTriggerEvent();

    }
}
