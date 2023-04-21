package com.example.mobile.screen;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobile.R;
import com.example.mobile.databinding.ActivityMainBinding;
import com.example.mobile.screen.home.HomeFragment;
import com.example.mobile.screen.message.MessageFragment;
import com.example.mobile.screen.profile.ProfileFragment;
import com.example.mobile.screen.statist.StatistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        HomeFragment homeFragment = new HomeFragment();
        MessageFragment messageFragment = new MessageFragment();
        StatistFragment statistFragment = new StatistFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.navigation_statist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,statistFragment).commit();
                        return true;
                    case R.id.navigation_message:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,messageFragment).commit();
                        return true;
                    case R.id.navigation_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}