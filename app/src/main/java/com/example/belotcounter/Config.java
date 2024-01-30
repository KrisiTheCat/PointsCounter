package com.example.belotcounter;

import android.content.Context;

import java.util.ArrayList;

public class Config {
    public static ArrayList<Game> games = new ArrayList<>();

    public static final int TEAMS_COUNT = 2;
    public static boolean gamesLoaded = false;

    public static void startGame(String n1, String n2, Context context){
        games.add(0, new GameBelot(n1,n2, context));
    }
    public static Game currentGame(){
        return games.get(0);
    }

    public static void deleteGame(int id) {
        games.remove(id);
    }
    public static void openGame(int id) {
        Game game = games.get(id);
        games.remove(id);
        games.add(0, game);
    }
}
