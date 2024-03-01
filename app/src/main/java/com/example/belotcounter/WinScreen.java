package com.example.belotcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class WinScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLanguage(MainActivity.currLocale, this);
        setContentView(R.layout.activity_win_screen);

        Config.currentGame().customizeWinScreen(WinScreen.this, findViewById(R.id.clWinScreen));

        findViewById(R.id.backBtn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btnToMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WinScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        TextView tvWinner = (TextView) findViewById(R.id.tvWinner);
        switch(Config.currentGame().winner){
            case TEAM_1:
                tvWinner.setText(Config.currentGame().getTeamNames().get(0)); break;
            case TEAM_2:
                tvWinner.setText(Config.currentGame().getTeamNames().get(1)); break;
            case TEAM_3:
                tvWinner.setText(Config.currentGame().getTeamNames().get(2)); break;
            default:
                tvWinner.setText(getResources().getString(R.string.nooneDid)); break;
        }

        addWinningMessage(detectWinningStyle());
    }

    int detectWinningStyle(){
        int messType = -1;
        if(Config.currentGame().winner == Winner.TIE) return 4;
        int winner = -1;
        switch(Config.currentGame().winner){
            case TEAM_1: winner = 0; break;
            case TEAM_2: winner = 1; break;
            default: winner = 2; break;
        }
        int cons = 0;
        for(int i = 0; i < Config.currentGame().turns.size(); i++){
            if(Config.currentGame().turns.get(i).getWinner() == winner){
                cons++;
            }
            else{
                cons = 0;
            }
        }
        if(cons == Config.currentGame().turns.size()) return 1;
        if(cons <= 3) return 3;
        return 2;
    }

    @SuppressLint("SetTextI18n")
    void addWinningMessage(int messType){
        if(messType == 4){
            ((TextView) findViewById(R.id.analysisHead)).setText(getResources().getString(R.string.analisys4_name));
            ((TextView) findViewById(R.id.analysisBody)).setText(getResources().getString(R.string.analisys4_full));
            return;
        }
        int winner;
        switch(Config.currentGame().winner){
            case TEAM_1: winner = 0; break;
            case TEAM_2: winner = 1; break;
            default: winner = 2; break;
        }
        switch(messType){
            case 1:
                ((TextView) findViewById(R.id.analysisHead)).setText(getResources().getString(R.string.analisys1_name));
                ((TextView) findViewById(R.id.analysisBody)).setText(
                        getResources().getString(R.string.analisys1_part1) + " " +
                        Config.currentGame().teamNames.get(winner) + " " +
                        getResources().getString(R.string.analisys1_part2));
                break;
            case 2:
                ((TextView) findViewById(R.id.analysisHead)).setText(getResources().getString(R.string.analisys2_name));
                ((TextView) findViewById(R.id.analysisBody)).setText(
                        getResources().getString(R.string.analisys2_part1) + " " +
                        Config.currentGame().teamNames.get(winner) + " " +
                        getResources().getString(R.string.analisys2_part2));
                break;
            default:
                ((TextView) findViewById(R.id.analysisHead)).setText(getResources().getString(R.string.analisys3_name));
                ((TextView) findViewById(R.id.analysisBody)).setText(
                        getResources().getString(R.string.analisys3_part1) + " " +
                        Config.currentGame().teamNames.get(winner) + " " +
                        getResources().getString(R.string.analisys3_part2));
                break;
        }
    }
}