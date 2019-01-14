package com.example.lovro.myapplication.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.activities.AddOfferActivity;
import com.example.lovro.myapplication.activities.StoryDetailsActivity;
import com.example.lovro.myapplication.activities.SuggestStoryActivity;
import com.example.lovro.myapplication.adapters.StoryAdapter;
import com.example.lovro.myapplication.domain.Story;
import com.example.lovro.myapplication.network.ApiService;
import com.example.lovro.myapplication.network.InitApiService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class StoryFragment extends Fragment {

    private static final int COMMENT_LIST = 1;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Story> storyList = new ArrayList<>();
    private ProgressBar progressBar;
    private ApiService apiService = InitApiService.apiService;
    private Call<List<Story>> getStories;
    private StoryAdapter storyAdapter;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview_stories);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayoutStory);
        progressBar = view.findViewById(R.id.offers_progressbarStory);
        progressBar.setVisibility(View.VISIBLE);
        fab = view.findViewById(R.id.fab);

        initRecylerView();
        initStoryAdapter(storyList);
        initListeners();

        if(isInternetAvailable()){
            if(storyList.size() == 0){
                load_stories();
            }else{
                progressBar.setVisibility(View.GONE);
            }
        }else{
            showError("No internet connection!");
        }

    }

    private void load_stories(){
        progressBar.setVisibility(View.VISIBLE);
        getStories = apiService.getAllStories();
        getStories.enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    displayStories(response.body());
                }else{
                    progressBar.setVisibility(View.GONE);
                    try {
                        showError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void displayStories(List<Story> stories){
        storyList = stories;
        if(storyAdapter != null){
            storyAdapter.setStories(stories);
        }else{
            initStoryAdapter(stories);
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

    protected boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initStoryAdapter(List<Story> storyList){
        storyAdapter = new StoryAdapter(storyList);
        recyclerView.setAdapter(storyAdapter);
    }

    private void initRecylerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userIsRegistered()){
                    Intent suggestStory = new Intent(getActivity(),SuggestStoryActivity.class);
                    startActivity(suggestStory);
                }else{
                    Toast.makeText(getContext(), "Please register for this feature", Toast.LENGTH_SHORT).show();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load_stories();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        storyAdapter.setListener(new StoryAdapter.OnShowClickListener() {
            @Override
            public void onShowClick(Story story) {
                Gson gson = new Gson();
                String storyAsString = gson.toJson(story);
                Intent intent = new Intent(getActivity(),StoryDetailsActivity.class);
                intent.putExtra("story",storyAsString);
                startActivityForResult(intent,COMMENT_LIST);
            }
        });
    }

    private boolean userIsRegistered(){
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        if(prefs.getBoolean("saved",false)){
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == COMMENT_LIST && resultCode == Activity.RESULT_OK){
            if(isInternetAvailable()){
                load_stories();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
