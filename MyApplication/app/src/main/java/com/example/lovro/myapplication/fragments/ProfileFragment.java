package com.example.lovro.myapplication.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.activities.EditProfileActivity;
import com.example.lovro.myapplication.activities.SignUpActivity;
import com.example.lovro.myapplication.domain.Colors;
import com.example.lovro.myapplication.domain.Role;
import com.example.lovro.myapplication.domain.User;
import com.example.lovro.myapplication.events.EditProfileEvent;
import com.example.lovro.myapplication.events.StyleChangeEvent;
import com.example.lovro.myapplication.network.ApiService;
import com.example.lovro.myapplication.network.InitApiService;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

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
    private User currentUser;
    private Button joinNowButton;
    private View adminTab;
    private View blockButton;
    private TextView userLetter;
    private View imageCircle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adminTab=view.findViewById(R.id.admin_tab);
        if (userIsRegistered()){
            editProfileButton=view.findViewById(R.id.editProfileButton);
            progressBar=view.findViewById(R.id.progressbar);
            nameUsernameText=view.findViewById(R.id.name_username_text);
            locationText=view.findViewById(R.id.location_text);
            emailText=view.findViewById(R.id.email_text);
            cardText=view.findViewById(R.id.card_text);
            blockButton=view.findViewById(R.id.block_button);
            userLetter=view.findViewById(R.id.profile_image_text);
            imageCircle=view.findViewById(R.id.profile_image_circle);

            view.findViewById(R.id.registered_user_panel).setVisibility(View.VISIBLE);
            view.findViewById(R.id.unregistered_user_panel).setVisibility(View.GONE);
            if (currentUser==null){
                loadUserFromAPI();
            } else{
                setUpLayout(currentUser);
            }
            initListeners();
        }else{
            view.findViewById(R.id.registered_user_panel).setVisibility(View.GONE);
            view.findViewById(R.id.unregistered_user_panel).setVisibility(View.VISIBLE);
            joinNowButton=view.findViewById(R.id.joinNowButton);
            initListenersUnregistered();
            adminTab.setVisibility(View.GONE);
        }


    }

    private void initListenersUnregistered() {
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SignUpActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Gson gson = new Gson();
                currentUser = gson.fromJson(result,User.class);
                setUpLayout(currentUser);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void initListeners() {
        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.black_list_user);

                Button okButton = dialog.findViewById(R.id.block_button);
                Button cancelButton = dialog.findViewById(R.id.cancel_button);
                EditText editText =dialog.findViewById(R.id.username_editText);

                

                dialog.show();
            }
        });
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String userAsString = gson.toJson(currentUser);

                Intent i = new Intent(getActivity(),EditProfileActivity.class);
                i.putExtra("UserCurrent",userAsString);
                i.putExtra("purchase",false);
                startActivityForResult(i,1);
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
        Call<User> getUser = apiService.getUserByUsername2(getUserAuth(), getUserUsername());

        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), "i got a user", Toast.LENGTH_SHORT).show();
                    User currentUser=response.body();
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
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(),"Error loading user", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }


    private void setUpLayout(User user) {
        currentUser=user;

        if (user==null){
            Toast.makeText(getContext(), "user not found", Toast.LENGTH_SHORT).show();
        }else{

            userLetter.setText(String.valueOf(currentUser.getUsername().toUpperCase().charAt(0)));



            //((TextView)).setText(story.getUser().getUsername().charAt(0));
            Drawable background = imageCircle.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(Colors.getColor((int) currentUser.getUsername().toLowerCase().charAt(0)-97));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(Colors.getColor((int) currentUser.getUsername().charAt(0)-97));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(Colors.getColor((int) currentUser.getUsername().charAt(0)-97));
            }
            if (checkIfUserAdmin()){
                adminTab.setVisibility(View.VISIBLE);
            }else{
                adminTab.setVisibility(View.GONE);
            }
            String town;
            if (user.getTown()==null){
                town="unknown";
            }else{
                town=String.valueOf(user.getTown().getPostCode())+" "+user.getTown().getName();
            }

            nameUsernameText.setText("His/hers name is "+getName(user.getName())+", also known as "+getName(user.getUsername()));
            locationText.setText("Lives in "+getName(user.getAddress())+", "+town);
            emailText.setText(getName(user.getEmail()));
            cardText.setText(getName(user.getCardNumber()));
        }
    }

    private boolean checkIfUserAdmin() {
        for (Role role:currentUser.getRoles()){
            if (role.getId()==4){
                return true;
            }
        }
        return false;
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

    private boolean userIsRegistered(){
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        if(prefs.getBoolean("saved",false)){
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditProfileEvent(EditProfileEvent event) {
        loadUserFromAPI();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
