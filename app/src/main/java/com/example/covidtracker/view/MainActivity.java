package com.example.covidtracker.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

    private void hideSystemUI(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
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

                case R.id.navigation_map:
                    loadFragment(new MapsFragment());
                    return true;

            }

            return false;
        }
    }

}
