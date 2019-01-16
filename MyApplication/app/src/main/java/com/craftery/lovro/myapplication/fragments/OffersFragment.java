package com.craftery.lovro.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.craftery.lovro.myapplication.R;
import com.craftery.lovro.myapplication.activities.AddOfferActivity;
import com.craftery.lovro.myapplication.activities.OfferDetailsActivity;
import com.craftery.lovro.myapplication.adapters.OfferAdapter;
import com.craftery.lovro.myapplication.domain.Offer;
import com.craftery.lovro.myapplication.events.StyleChangeEvent;
import com.craftery.lovro.myapplication.network.ApiService;
import com.craftery.lovro.myapplication.network.InitApiService;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class OffersFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Offer> offerList = new ArrayList<>();
    private Call<List<Offer>> getOffers;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private ApiService apiService = InitApiService.apiService;
    private OfferAdapter offerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview_offers);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        progressBar = view.findViewById(R.id.offers_progressbar);
        floatingActionButton = view.findViewById(R.id.offer_add);

        if(!userIsAdmin()){
            floatingActionButton.setVisibility(View.GONE);
        }else{
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addOffer = new Intent(getActivity(),AddOfferActivity.class);
                    startActivityForResult(addOffer,1);
                }
            });
        }

        progressBar.setVisibility(View.VISIBLE);

        initRecyclerView();
        initOfferAdapter(offerList);
        initListeners();

        if(isInternetAvailable()){
            if(offerList.size() == 0){
                loadOffers(getUserAuth());
            }else{
                progressBar.setVisibility(View.GONE);
            }
        }else{
            showError("No internet connection!");
        }
    }

    private boolean userIsAdmin(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        return preferences.getBoolean("admin",false);
    }

    private void initListeners(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadOffers(getUserAuth());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        offerAdapter.setListener(new OfferAdapter.OnShowClickListener() {
            @Override
            public void onShowClick(Offer offer) {
                Gson gson = new Gson();
                String offerAsString = gson.toJson(offer);

                Intent offer_details = new Intent(getActivity(),OfferDetailsActivity.class);
                offer_details.putExtra("Offer",offerAsString);
                startActivity(offer_details);
            }
        });
    }


    protected boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initOfferAdapter(List<Offer> offerList){
        offerAdapter = new OfferAdapter(offerList);
        recyclerView.setAdapter(offerAdapter);
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

    private void loadOffers(String auth){
        progressBar.setVisibility(View.VISIBLE);
        getOffers = apiService.getAllOffers();
        getOffers.enqueue(new Callback<List<Offer>>() {
            @Override
            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                if(response.isSuccessful()){
                    offerList = response.body();
                    displayOffers(offerList);
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
            public void onFailure(Call<List<Offer>> call, Throwable t) {
                Toast.makeText(getActivity(),"Error loading offers", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void displayOffers(List<Offer> offers){
        progressBar.setVisibility(View.GONE);
        if(offerAdapter != null){
            offerAdapter.setOffers(offers);
        }else{
            initOfferAdapter(offers);
        }
    }

    protected void showError(String message){
        new AlertDialog.Builder(getContext())
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                loadOffers(getUserAuth());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStyleChangeEvent(StyleChangeEvent event) {
        loadOffers(getUserAuth());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
