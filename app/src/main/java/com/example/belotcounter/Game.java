package com.example.belotcounter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public abstract class Game {
    public GameType gameType;
    private ArrayList<String> teamNames;
    private ArrayList<Turn> turns;


    Game(){
        teamNames = new ArrayList<>();
        turns = new ArrayList<>();
        teamNames.add("");
        teamNames.add("");
    }
    Game(String n1, String n2){
        teamNames = new ArrayList<>();
        turns = new ArrayList<>();
        teamNames.add(n1);
        teamNames.add(n2);
    }

    public ArrayList<String> getTeamNames() {
        return teamNames;
    }

    public void setTeamNames(ArrayList<String> teamNames) {
        this.teamNames = teamNames;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }
    public void addTurn(Turn turn) {
        turns.add(turn);
    }
    public void setTurn(Turn turn, int id) {
        turns.set(id, turn);
    }
    public void deleteTurn(int id){
        turns.remove(id);
    }

    public int getPoints(int team){
        int sum = 0;
        for(int i = 0; i < turns.size();i++){
            sum += turns.get(i).getPoints(team);
        }
        return sum;
    }

    abstract void checkForWinner();

    public void customizeLastGame(Context context, View view){
        view.setBackground(ContextCompat.getDrawable(context, gameType.gradient));
        view.findViewById(R.id.viewEdge).setBackground(ContextCompat.getDrawable(context, gameType.edge));
        view.findViewById(R.id.line1).setBackgroundColor(ContextCompat.getColor(context, gameType.colorLight));
        view.findViewById(R.id.line2).setBackgroundColor(ContextCompat.getColor(context, gameType.colorLight));
        ((TextView) view.findViewById(R.id.tvVs)).setTextColor(ContextCompat.getColor(context, gameType.colorLight));
        ((TextView) view.findViewById(R.id.edgeText)).setTextColor(ContextCompat.getColor(context, gameType.colorDark));
        ((TextView) view.findViewById(R.id.edgeText)).setText(context.getResources().getString(gameType.gameName));
        ((FloatingActionButton) view.findViewById(R.id.fabDelGame)).setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, gameType.colorLight)));
        ((FloatingActionButton) view.findViewById(R.id.fabDelGame)).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, gameType.colorDark)));

        ((TextView) view.findViewById(R.id.tvTeam1)).setText(teamNames.get(0));
        ((TextView) view.findViewById(R.id.tvPts1)).setText(getPoints(0)+"");
        ((TextView) view.findViewById(R.id.tvTeam2)).setText(teamNames.get(1));
        ((TextView) view.findViewById(R.id.tvPts2)).setText(getPoints(1)+"");

    }
}
