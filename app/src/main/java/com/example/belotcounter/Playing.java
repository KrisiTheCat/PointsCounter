package com.example.belotcounter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.belotcounter.databinding.ActivityPlayingBinding;
import com.google.gson.Gson;

public class Playing extends AppCompatActivity {

    private ActivityPlayingBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPortrait();
        Config.currentGame().customizeInGameLandscape(Playing.this, findViewById(R.id.allLayout));
    }

    void initPortrait(){
        binding = ActivityPlayingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentAdapter sectionsPagerAdapter = new FragmentAdapter(this, getSupportFragmentManager(), 3);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText() == getResources().getString(R.string.tab_text_2)){
                    sectionsPagerAdapter.fragmentGraph.refreshGraph();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabs.setupWithViewPager(viewPager);


        ((android.widget.ImageButton) findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, Config.currentGame().gameType.colorLight));

        ((TextView) findViewById(R.id.tvGameName)).setText(Playing.this.getResources().getString(Config.currentGame().gameType.gameName));
        findViewById(R.id.tvGameName).setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, Config.currentGame().gameType.colorLight)));
    }
    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        int orient = getResources().getConfiguration().orientation;
        switch(orient) {
            case Configuration.ORIENTATION_LANDSCAPE:
                findViewById(R.id.allLayout).setVisibility(View.VISIBLE);
                Config.currentGame().fillInGameLandscape(Playing.this, findViewById(R.id.allLayout));
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                findViewById(R.id.allLayout).setVisibility(View.INVISIBLE);
                break;
            default:
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveGames();
    }
    private void saveGames(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref3",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Config.games.get(0));
        myEdit.putString("game0", json);
        myEdit.apply();
    }
}