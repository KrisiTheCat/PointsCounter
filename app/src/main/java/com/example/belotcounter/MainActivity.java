package com.example.belotcounter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    View swipeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Config.gamesLoaded) loadGames();
        initGamesTabs();

        FloatingActionButton startGameFab = (FloatingActionButton) findViewById(R.id.startGameFab);
        startGameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final View customLayout = getLayoutInflater().inflate(R.layout.popup_teams_names, null);
                builder.setView(customLayout);

                AlertDialog dialog = builder.create();
                dialog.show();

                Button continueBtn = (Button) customLayout.findViewById(R.id.btnDelYes);
                continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> names = new ArrayList<>();
                        String name1 = ((TextView) customLayout.findViewById(R.id.etTeam1)).getText().toString();
                        String name2 = ((TextView) customLayout.findViewById(R.id.etTeam2)).getText().toString();
                        Config.startGame(name1, name2, MainActivity.this);
                        Intent i = new Intent(MainActivity.this, InGame.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initGamesTabs() {
        LinearLayout llPrevGames = ((LinearLayout) findViewById(R.id.llPrevGames));
        llPrevGames.removeAllViews();
        System.out.println("Config.games.size(): " + Config.games.size());
        if(Config.games.size() == 0){
            ((ConstraintLayout) findViewById(R.id.clLastGame)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tvNoGames1)).setVisibility(View.VISIBLE);
            ((ScrollView) findViewById(R.id.svPrevGames)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tvNoGames2)).setVisibility(View.VISIBLE);
        }
        else {
            /*Last game*/
            View lastGameLayout = findViewById(R.id.clLastGame);
            Config.games.get(0).customizeLastGame(MainActivity.this, lastGameLayout);
            lastGameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, InGame.class);
                    startActivity(i);
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
                    View view = LayoutInflater.from(this).inflate(R.layout.old_game_group, null);
                    ((TextView) view.findViewById(R.id.tvTeam1)).setText(Config.games.get(i).getTeamNames().get(0));
                    ((TextView) view.findViewById(R.id.tvPts1)).setText(Config.games.get(i).getPoints(0) + "");
                    ((TextView) view.findViewById(R.id.tvTeam2)).setText(Config.games.get(i).getTeamNames().get(1));
                    ((TextView) view.findViewById(R.id.tvPts2)).setText(Config.games.get(i).getPoints(1) + "");
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
                            Intent i = new Intent(MainActivity.this, InGame.class);
                            startActivity(i);
                        }
                    });
                    llPrevGames.addView(view);
                }
            }
        }
    }

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

    private void loadGames(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref3",MODE_PRIVATE);
        int n = sharedPreferences.getInt("gamesCount", 0);
        for(int i = 0; i < n; i++){
            Gson gson = new Gson();
            String type = sharedPreferences.getString("gameType"+i, "");
            Game game = null;
            String json = sharedPreferences.getString("game"+i, "");
            System.out.println(json);
            switch(GameType.valueOf(type)){
                case BELOT:
                    game = gson.fromJson(json, GameBelot.class);
                    break;

            }
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
}

