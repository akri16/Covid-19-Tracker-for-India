package com.example.covidtracker.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.covidtracker.R;
import com.example.covidtracker.databinding.ActivityMainBinding;
import com.example.covidtracker.utils.GeneralUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNav.setOnNavigationItemSelectedListener(new MyBottomNavItemSelectedListener());
        loadFragment(new StatisticsFragment());
    }


    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_nav_fragment_container, fragment)
                .disallowAddToBackStack()
                .commit();
    }

    private class MyBottomNavItemSelectedListener implements
            BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.navigation_records:
                    loadFragment(new StatisticsFragment());
                    return true;

                case R.id.navigation_help:
                    loadFragment(new HelpFragment());
                    return true;

                case R.id.navigation_news:
                    loadFragment(new NewsFragment());
                    return true;

            }

            return false;
        }
    }

}
