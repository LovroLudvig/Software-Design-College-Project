package com.example.lovro.myapplication.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.adapters.NotificationAdapter;
import com.example.lovro.myapplication.adapters.OfferAdapter;
import com.example.lovro.myapplication.domain.Notification;
import com.example.lovro.myapplication.domain.Offer;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private  NotificationAdapter notifAdapter;
    private List<Notification> notifList = new ArrayList<>();
    private List<Notification> mockedList = new ArrayList<>();


    private OnFragmentInteractionListener mListener;



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
        progressBar.setVisibility(View.VISIBLE);
        if (userIsRegistered()){
            view.findViewById(R.id.registered_user_panel).setVisibility(View.VISIBLE);
            view.findViewById(R.id.unregistered_user_panel).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.registered_user_panel).setVisibility(View.GONE);
            view.findViewById(R.id.unregistered_user_panel).setVisibility(View.VISIBLE);
        }


        if (notifList.size()==0){
            Notification notif1 = new Notification("Craftery admin", "Accepted your offer", "tue 15:33");
            Notification notif2 = new Notification("Craftery admin", "Accepted your offer", "tue 15:33");
            Notification notif3 = new Notification("Craftery admin", "Accepted your offer", "tue 15:33");

            mockedList.add(notif1);
            mockedList.add(notif2);
            mockedList.add(notif3);
            notifList = mockedList;

            initRecyclerView();
            initNotificationAdapter(notifList);
            //initListeners();

            //TODO pull the data from API here, check internet
            displayNotifications(notifList);
        }







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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    private void displayNotifications(List<Notification> notificationList){
        progressBar.setVisibility(View.GONE);
        if(notifAdapter != null){
            notifAdapter.setOffers(notificationList);
        }else{
            initNotificationAdapter(notificationList);
        }
    }


    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initNotificationAdapter(List<Notification> notificationList){
        notifAdapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(notifAdapter);
    }
}
