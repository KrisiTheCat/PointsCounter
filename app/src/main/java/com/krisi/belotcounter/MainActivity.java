package com.krisi.belotcounter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    boolean fabOpen;

    public static String currLocale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPreferences();
        if(!Config.gamesLoaded) loadGames();
        if(Config.games.size() == 0)
            setTheme(GameType.BELOT.theme);
        else
            setTheme(Config.games.get(0).gameType.theme);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.WHITE);

        setLanguage(currLocale, this);
        setContentView(R.layout.activity_main);
        if(currLocale.equals("en")) ((ImageButton) findViewById(R.id.btnLanguage)).setImageResource(R.drawable.en);
        else ((ImageButton) findViewById(R.id.btnLanguage)).setImageResource(R.drawable.bg);
        fabOpen = false;

        initGamesTabs();

        FloatingActionButton startGameFab = (FloatingActionButton) findViewById(R.id.startGameFab);
        startGameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabOpen) {
                    closeButton();
                }
                else {
                    openButton();
                }
                fabOpen = !fabOpen;
            }
        });

        final Game[] game = {null};
        Runnable openGame = new Runnable() {
            @Override
            public void run(){
                Config.addGame(game[0]);
                Intent i = new Intent(MainActivity.this, Playing.class);
                startActivity(i);
                finish();
                if(fabOpen)closeButton();
            }
        };
        findViewById(R.id.startBelotFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabOpen) {
                    game[0] = new GameBelot();
                    game[0].initNames(MainActivity.this, getLayoutInflater(), openGame);
                }
            }
        });
        findViewById(R.id.startHilqdaFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabOpen) {
                    game[0] = new GameHilqda();
                    game[0].initNames(MainActivity.this, getLayoutInflater(), openGame);
                }
            }
        });
        findViewById(R.id.startBlatoFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabOpen) {
                    game[0] = new GameBlato();
                    game[0].initNames(MainActivity.this, getLayoutInflater(), openGame);
                }
            }
        });
        findViewById(R.id.startSantaceFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabOpen) {
                    game[0] = new GameSantace();
                    game[0].initNames(MainActivity.this, getLayoutInflater(), openGame);
                }
            }
        });

        findViewById(R.id.btnLanguage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currLocale.equals("bg"))
                    currLocale = "en";
                else
                    currLocale = "bg";
                savePreferences();
                restartApp();
            }
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initGamesTabs() {
        LinearLayout llPrevGames = ((LinearLayout) findViewById(R.id.llPrevGames));
        llPrevGames.removeAllViews();
        System.out.println(Config.games.size()+" games");
        if(Config.games.size() == 0){
            ((ConstraintLayout) findViewById(R.id.clLastGame)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tvNoGames1)).setVisibility(View.VISIBLE);
            ((ScrollView) findViewById(R.id.svPrevGames)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tvNoGames2)).setVisibility(View.VISIBLE);
        }
        else {
            /*Last game*/
            setTheme(Config.games.get(0).gameType.theme);
            View lastGameLayout = findViewById(R.id.clLastGame);
            Config.games.get(0).customizeGameBtn(MainActivity.this, lastGameLayout, true);
            lastGameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, Playing.class);
                    startActivity(i);
                    finish();
                    if(fabOpen)closeButton();
                }
            });
            lastGameLayout.findViewById(R.id.fabDelGame).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View deleteLayout = getLayoutInflater().inflate(R.layout.popup_delete_game, null);
                    builder.setView(deleteLayout);
                    Dialog deleteDialog = builder.create();
                    deleteDialog.show();
                    deleteLayout.findViewById(R.id.btnDelYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Config.deleteGame(0);
                            initGamesTabs();
                            deleteDialog.dismiss();
                        }
                    });
                }
            });

            if(Config.games.size() == 1){
                ((ScrollView) findViewById(R.id.svPrevGames)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.tvNoGames2)).setVisibility(View.VISIBLE);
            }
            else{
                for(int i = 1; i < Config.games.size(); i++) {
                    ContextThemeWrapper newContext = new ContextThemeWrapper(this, Config.games.get(i).gameType.theme);
                    System.out.println(Config.games.get(i).gameType.theme);
                    View view = LayoutInflater.from(newContext).inflate(R.layout.old_game_group2, null);
                    Config.games.get(i).customizeGameBtn(newContext, view, false);
                    int finalI = i;
                    view.findViewById(R.id.fabDelGame).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            View deleteLayout = getLayoutInflater().inflate(R.layout.popup_delete_game, null);
                            builder.setView(deleteLayout);
                            Dialog deleteDialog = builder.create();
                            deleteDialog.show();
                            deleteLayout.findViewById(R.id.btnDelYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Config.deleteGame(finalI);
                                    initGamesTabs();
                                    Toast.makeText(MainActivity.this,getResources().getText(R.string.toast_game_deleted), Toast.LENGTH_SHORT).show();
                                    deleteDialog.dismiss();
                                }
                            });
                        }
                    });
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Config.openGame(finalI);
                            Intent i = new Intent(MainActivity.this, Playing.class);
                            startActivity(i);
                            if(fabOpen) closeButton();
                        }
                    });
                    llPrevGames.addView(view);
                }
            }
        }
    }

    private void loadGames(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref3",MODE_PRIVATE);
        int n = sharedPreferences.getInt("gamesCount", 0);
        for(int i = 0; i < n; i++){
            Gson gson = new Gson();
            String type = sharedPreferences.getString("gameType"+i, "");
            Game game = null;
            String json = sharedPreferences.getString("game"+i, "");
            switch(GameType.valueOf(type)){
                case BELOT:
                    game = gson.fromJson(json, GameBelot.class);
                    break;
                case BLATO:
                    game = gson.fromJson(json, GameBlato.class);
                    break;
                case HILQDA:
                    game = gson.fromJson(json, GameHilqda.class);
                    break;
                case SANTACE:
                    game = gson.fromJson(json, GameSantace.class);
                    break;

            }
            if(game.winner==null) game.winner = Winner.NONE;
            Config.games.add(game);
        }
        Config.gamesLoaded = true;
    }

    private void saveGames(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref3",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        Gson gson = new Gson();
        myEdit.putInt("gamesCount", Config.games.size());
        for(int i = 0; i < Config.games.size(); i++){
            String json = gson.toJson(Config.games.get(i));
            myEdit.putString("game"+i, json);
            myEdit.putString("gameType"+i, Config.games.get(i).gameType.toString());
        }
        myEdit.apply();
    }


    private void loadPreferences(){
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("MySharedPref3",MODE_PRIVATE);
        currLocale = sharedPreferences.getString("locale", "en");
    }

    private void savePreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref3",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("locale", currLocale);
        myEdit.apply();
        myEdit.commit();
    }

    public static  void setLanguage(String languageCode, Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
    public void restartApp() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(getPackageName());
        finishAffinity();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public Context createConfiguration(Context context, String lan) {
        Locale locale = new Locale(lan);
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private void closeButton(){
        Handler handler = new Handler();
        Handler handler2 = new Handler();
        FloatingActionButton startGameFab = (FloatingActionButton) findViewById(R.id.startGameFab);
        startGameFab.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_from_45));
        findViewById(R.id.startBelotFab).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.to_bottom_anim));
        findViewById(R.id.tvBelotBtn).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.to_bottom_anim));
        handler.postDelayed(new Runnable() {
            public void run() {
                findViewById(R.id.startBlatoFab).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.to_bottom_anim));
                findViewById(R.id.tvBlatoBtn).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.to_bottom_anim));
                handler.postDelayed(new Runnable() {
                    public void run() {
                        findViewById(R.id.startSantaceFab).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.to_bottom_anim));
                        findViewById(R.id.tvSantaceBtn).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.to_bottom_anim));

                        handler2.postDelayed(new Runnable() {
                            public void run() {
                                findViewById(R.id.startHilqdaFab).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.to_bottom_anim));
                                findViewById(R.id.tvHilqdaBtn).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.to_bottom_anim));
                            }
                        }, 200);
                    }
                }, 200);
            }
        }, 200);
    }
    private void openButton(){
        Handler handler = new Handler();
        Handler handler2 = new Handler();
        FloatingActionButton startGameFab = (FloatingActionButton) findViewById(R.id.startGameFab);
        startGameFab.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_to_45));
        findViewById(R.id.startBelotFab).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.from_bottom_anim));
        findViewById(R.id.tvBelotBtn).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.from_bottom_anim));
        handler.postDelayed(new Runnable() {
            public void run() {
                findViewById(R.id.startBlatoFab).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.from_bottom_anim));
                findViewById(R.id.tvBlatoBtn).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.from_bottom_anim));
                handler.postDelayed(new Runnable() {
                    public void run() {
                        findViewById(R.id.startSantaceFab).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.from_bottom_anim));
                        findViewById(R.id.tvSantaceBtn).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.from_bottom_anim));

                        handler2.postDelayed(new Runnable() {
                            public void run() {
                                findViewById(R.id.startHilqdaFab).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.from_bottom_anim));
                                findViewById(R.id.tvHilqdaBtn).startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.from_bottom_anim));
                            }
                        }, 200);
                    }
                }, 200);
            }
        }, 200);
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        System.out.println("rawr");
        loadPreferences();
        System.out.println(currLocale);
        super.attachBaseContext(createConfiguration(newBase, currLocale));
    }*/

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initGamesTabs();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveGames();
    }
}

