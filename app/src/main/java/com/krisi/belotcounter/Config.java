package com.krisi.belotcounter;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class Config {
    public static ArrayList<Game> games = new ArrayList<>();
    public static boolean gamesLoaded = false;

    public static void addGame(Game game){
        games.add(0, game);
    }
    public static void startGame(String n1, String n2, Context context){
        //games.add(0, new GameBelot(n1,n2, context));
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
