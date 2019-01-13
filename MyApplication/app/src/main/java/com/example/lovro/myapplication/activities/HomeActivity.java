package com.example.lovro.myapplication.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lovro.myapplication.fragments.NotificationsFragment;
import com.example.lovro.myapplication.fragments.OffersFragment;
import com.example.lovro.myapplication.fragments.ProfileFragment;
import com.example.lovro.myapplication.fragments.StoryFragment;
import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.adapters.SectionsPagerAdapter;

public class HomeActivity extends BasicActivity implements ProfileFragment.OnFragmentInteractionListener, StoryFragment.OnFragmentInteractionListener, OffersFragment.OnFragmentInteractionListener {

    private int backButtonCount=0;
    private TabLayout tabLayout;
    private View logoutButon;

    //OVDJE MOZEMO PISATI STA GOD DA NAM PADNE NA PAMET DA JE JOS POTREBNO ZA APLIKACIJU

    //DONE: IMPLEMENTIRATI PREDLAGANJE STILA ZE OFFER (KAD NEREGISTRIRANI KORISNIK PRITISNE NA BUTTON ZA DODAVANJE STILA MORA DOBITI TOAST DA SE TREBA REGISTRIRATI ZA TO)
    //DONE: NOTIFIKACIJE, POSEBNO ADMINISTRATORU, POSEBNO REGISTRIRANOM KORISNIKU ------------------> REZERVIRA LOVRO
    //DONE: U PROFILE TAB DODATI POSEBAN CARD KOJI CE VIDJETI SAMO ADMIN NA KOJEM MOZE BLOKIRATI USERE I RADITI NEKE DRUGE STVARI KOJE ADMIN SAMO MOZE (AKO TAKE STVARI JOS POSTOJE UOPCE) --------> REZERVIRA LOVRO
    //TODO: IMPLEMENTACIJA PREDLAGANJA STORYA SA SLIKOM I VIDEOM (KAD NEREGISTRIRANI KORISNIK PRITISNE NA FLOATING BUTTON ZA DODAVANJE MORA DOBITI TOAST DA SE TREBA REGISTRIRATI ZA TO)
    //TODO: IMPLEMENTACIJA OBJAVLJIVANJA OFFERA, OVO MOZE SAMO ADMIN (FLOATING BUTTON KOJI CE VIDJETI SAMO ADMINISTRATORI)
    //TODO: FIX RAM ERROR

    //STA GOD TI PADNE NA PAMET DA JOS TREBA NAPRAVITI NAPISI GORE I MOZES OZNACIT STO CES TI NAPRAVITI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupViewPager();


    }


    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StoryFragment()); //index 0
        adapter.addFragment(new OffersFragment()); //index 1
        adapter.addFragment(new NotificationsFragment()); //index 2
        adapter.addFragment(new ProfileFragment()); //index 3
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        logoutButon=findViewById(R.id.logout_image);

        if(!userIsRegistered()){
            logoutButon.setVisibility(View.GONE);
        }

        tabLayout.getTabAt(0).setIcon(R.drawable.story_btn);
        tabLayout.getTabAt(1).setIcon(R.drawable.shop_btn_deselected);
        tabLayout.getTabAt(2).setIcon(R.drawable.notif_btn_deselected);
        tabLayout.getTabAt(3).setIcon(R.drawable.profile_btn_deselected);

        logoutButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                alertDialog.setTitle("Log out");
                alertDialog.setMessage("Are you sure you want to log out?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("saved",false);
                                editor.putBoolean("admin",false);
                                editor.putString("username","");
                                editor.putString("password","");
                                editor.apply();
                                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideKeyboard();
                if (tab.getPosition()==0){
                    tabLayout.getTabAt(0).setIcon(R.drawable.story_btn);
                }else if (tab.getPosition()==1){
                    tabLayout.getTabAt(1).setIcon(R.drawable.shop_btn);
                }else if (tab.getPosition()==2){
                    tabLayout.getTabAt(2).setIcon(R.drawable.notif_btn);
                }else if (tab.getPosition()==3){
                    tabLayout.getTabAt(3).setIcon(R.drawable.profile_btn);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    tabLayout.getTabAt(0).setIcon(R.drawable.story_btn_deselected);
                }else if (tab.getPosition()==1){
                    tabLayout.getTabAt(1).setIcon(R.drawable.shop_btn_deselected);
                }else if (tab.getPosition()==2){
                    tabLayout.getTabAt(2).setIcon(R.drawable.notif_btn_deselected);
                }else if (tab.getPosition()==3){
                    tabLayout.getTabAt(3).setIcon(R.drawable.profile_btn_deselected);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        backButtonCount=0;
    }

    @Override
    public void onBackPressed()
    {
        if(userIsRegistered()){
            if(backButtonCount >= 1)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
                backButtonCount++;
            }
        }else{
            finish();
        }
    }

    private boolean userIsRegistered(){
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        if(prefs.getBoolean("saved",false)){
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
