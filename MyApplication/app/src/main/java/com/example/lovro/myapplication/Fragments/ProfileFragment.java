package com.example.lovro.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.activities.EditProfileActivity;
import com.example.lovro.myapplication.activities.OfferDetailsActivity;
import com.example.lovro.myapplication.domain.Offer;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.domain.UserForRegister;
import com.example.lovro.myapplication.domain.UserProfile;
import com.example.lovro.myapplication.network.ApiService;
import com.example.lovro.myapplication.network.GenericResponse;
import com.example.lovro.myapplication.network.InitApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Button editProfileButton;
    private ApiService apiService = InitApiService.apiService;
    private TextView nameUsernameText;
    private TextView locationText;
    private TextView emailText;
    private TextView cardText;
    private ProgressBar progressBar;
    private UserProfile currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editProfileButton=view.findViewById(R.id.editProfileButton);
        progressBar=view.findViewById(R.id.progressbar);
        nameUsernameText=view.findViewById(R.id.name_username_text);
        locationText=view.findViewById(R.id.location_text);
        emailText=view.findViewById(R.id.email_text);
        cardText=view.findViewById(R.id.card_text);
        initListeners();
        if (currentUser==null){
            loadUserFromAPI();
        } else{
            setUpLayout(currentUser);
        }

    }

    private void initListeners() {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),EditProfileActivity.class);
                startActivity(i);
            }
        });
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

    private void loadUserFromAPI(){
        progressBar.setVisibility(View.VISIBLE);
        Call<UserProfile> getUser = apiService.getUserByUsername(getUserAuth(), getUserUsername());

        getUser.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), "i got a user", Toast.LENGTH_SHORT).show();
                    UserProfile currentUser=response.body();
                    setUpLayout(currentUser);
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
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(getActivity(),"Error loading user", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void setUpLayout(UserProfile user) {
        currentUser=user;
        if (user==null){
            Toast.makeText(getContext(), "user not found", Toast.LENGTH_SHORT).show();
        }else{
            String town;
            if (user.getTown()==null){
                town="unknown";
            }else{
                town=user.getTown().getName();
            }

            nameUsernameText.setText("Name: "+getName(user.getName())+", also known as "+getName(user.getUsername()));
            locationText.setText("Lives in "+getName(user.getAddress())+", "+town);
            //emailText.setText(user.getEmail()); //TODO: after email is added to backend do this too
            cardText.setText(getName(user.getCardNumber()));
        }
    }

    private String getName(String name){
        if (name==null){
            return "unknown";
        }else{
            return name;
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
}
