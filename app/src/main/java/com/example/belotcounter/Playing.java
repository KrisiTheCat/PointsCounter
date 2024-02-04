package com.example.belotcounter;

import android.annotation.SuppressLint;
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

public class Playing extends AppCompatActivity {

    private ActivityPlayingBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentAdapter sectionsPagerAdapter = new FragmentAdapter(this, getSupportFragmentManager(), 3);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
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
        findViewById(R.id.tvGameName).setBackground(ContextCompat.getDrawable(Playing.this, Config.currentGame().gameType.upper));
    }
}