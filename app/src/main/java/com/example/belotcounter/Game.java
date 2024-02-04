package com.example.belotcounter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public abstract class Game {
    public GameType gameType;
    protected ArrayList<String> teamNames;
    protected ArrayList<Turn> turns;
    public Winner winner;


    Game(){
        teamNames = new ArrayList<>();
        turns = new ArrayList<>();
        teamNames.add("");
        teamNames.add("");
        winner = Winner.NONE;
    }
    Game(String n1, String n2){
        teamNames = new ArrayList<>();
        turns = new ArrayList<>();
        teamNames.add(n1);
        teamNames.add(n2);
        winner = Winner.NONE;
    }

    abstract void initNames(Context context, LayoutInflater layoutInflater, Runnable runnable);
    abstract void checkForWinner();
    abstract void initEntryPopup(View editLayout, int id);
    abstract Turn extractEntry(View editLayout, Context context);
    abstract ArrayList<LinearLayout> getGraphLayouts(Context context, View view);

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


    public void customizeGameBtn(Context context, View view, boolean gradient){
        if(gradient) view.setBackground(ContextCompat.getDrawable(context, gameType.gradient));
        else view.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, gameType.colorAccent)));
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

    public void customizeInGameLandscape(Context context, View view){
        ((TextView) view.findViewById(R.id.tvTeam1)).setTextColor(Color.WHITE);
        ((TextView) view.findViewById(R.id.tvFinal1)).setTextColor(Color.WHITE);
        view.findViewById(R.id.viewTeam1).setBackgroundColor(ContextCompat.getColor(context, gameType.colorAccent));
        ((TextView) view.findViewById(R.id.tvTeam2)).setTextColor(Color.WHITE);
        ((TextView) view.findViewById(R.id.tvFinal2)).setTextColor(Color.WHITE);
        view.findViewById(R.id.viewTeam2).setBackgroundColor(ContextCompat.getColor(context, gameType.colorDark));
    }
    public void customizeInGamePortrait(Context context, View view){
        ((TextView) view.findViewById(R.id.tvTeam1)).setTextColor(ContextCompat.getColor(context, gameType.colorAccent));
        ((TextView) view.findViewById(R.id.tvFinal1)).setTextColor(ContextCompat.getColor(context, gameType.colorAccent));
        ((TextView) view.findViewById(R.id.tvTeam2)).setTextColor(ContextCompat.getColor(context, gameType.colorDark));
        ((TextView) view.findViewById(R.id.tvFinal2)).setTextColor(ContextCompat.getColor(context, gameType.colorDark));
        view.findViewById(R.id.addPointsBtn).setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, gameType.colorLight)));
    }
    public void customizeEditEntry(Context context, View view){
        view.findViewById(R.id.viewBehind1).setBackgroundColor(ContextCompat.getColor(context, gameType.colorAccent));
        view.findViewById(R.id.viewBehind2).setBackgroundColor(ContextCompat.getColor(context, gameType.colorDark));
    }
    public void customizeWinScreen(Context context, View view){
        view.findViewById(R.id.btnToMenu).setBackgroundColor(ContextCompat.getColor(context, gameType.colorAccent));
        switch(winner){
            case TEAM_1:
                ((TextView) view.findViewById(R.id.tvWinner)).setText(teamNames.get(0));
                ((TextView) view.findViewById(R.id.tvWinner)).setTextColor(ContextCompat.getColor(context, gameType.colorAccent));
                break;
            case TEAM_2:
                ((TextView) view.findViewById(R.id.tvWinner)).setText(teamNames.get(1));
                ((TextView) view.findViewById(R.id.tvWinner)).setTextColor(ContextCompat.getColor(context, gameType.colorDark));
                break;
            case TEAM_3:
                ((TextView) view.findViewById(R.id.tvWinner)).setText(teamNames.get(2));
                ((TextView) view.findViewById(R.id.tvWinner)).setTextColor(ContextCompat.getColor(context, gameType.colorAccent));
                break;
            default:
                /**TODO
                 * - tie
                 */
        }
    }
    public void customizeGraph(Context context, View view){
        int c = 0;
        ArrayList<LinearLayout> llTeams = getGraphLayouts(context, view);
        for(int i = 0; i < gameType.plCount; i++){
            ((TextView) llTeams.get(i).findViewWithTag("tvGraphTeam")).setText(teamNames.get(i));
            if(i%2==0)
                c = ContextCompat.getColor(context, gameType.colorAccent);
            else
                c = ContextCompat.getColor(context, gameType.colorDark);
            llTeams.get(i).findViewWithTag("vGraphTeam").setBackgroundTintList(ColorStateList.valueOf(c));
        }
        initGraph(context, view.findViewById(R.id.idGraphView));
    }

    void initGraph(Context context, GraphView graphView) {
        graphView.getGridLabelRenderer().setHumanRounding(false);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 151),
                new DataPoint(turns.size(), 151)/*TODO: remove 151*/
        });
        series.setColor(ContextCompat.getColor(context, R.color.myDark));
        graphView.addSeries(series);
        for(int i = 0 ; i < gameType.plCount; i++) {
            DataPoint[] values = new DataPoint[turns.size()+1];
            int s = 0;
            values[0] = new DataPoint(0,0);
            for (int j=0; j<turns.size(); j++) {
                s+=turns.get(j).getPoints(i);
                System.out.println(s);
                values[j+1] = new DataPoint(j, s);
            }
            series = new LineGraphSeries<DataPoint>(values);
            if(i%2==0)
                series.setColor(ContextCompat.getColor(context, gameType.colorAccent));
            else
                series.setColor(ContextCompat.getColor(context, gameType.colorDark));
            graphView.addSeries(series);
        }
    }
}
