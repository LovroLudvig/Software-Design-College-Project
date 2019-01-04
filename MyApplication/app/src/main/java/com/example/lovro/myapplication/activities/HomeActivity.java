package com.example.lovro.myapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lovro.myapplication.Fragments.NotificationsFragment;
import com.example.lovro.myapplication.Fragments.OffersFragment;
import com.example.lovro.myapplication.Fragments.ProfileFragment;
import com.example.lovro.myapplication.Fragments.StoryFragment;
import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.adapters.SectionsPagerAdapter;

public class HomeActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener, StoryFragment.OnFragmentInteractionListener, OffersFragment.OnFragmentInteractionListener {

    private int backButtonCount=0;
    private TabLayout tabLayout;

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

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.story_btn);
        tabLayout.getTabAt(1).setIcon(R.drawable.shop_btn_deselected);
        tabLayout.getTabAt(2).setIcon(R.drawable.notif_btn_deselected);
        tabLayout.getTabAt(3).setIcon(R.drawable.profile_btn_deselected);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
    public void onBackPressed()
    {

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
    }
}
