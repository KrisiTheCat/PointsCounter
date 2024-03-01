package com.krisi.belotcounter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameHilqda extends GameForThree{

    public GameHilqda() {
        super();
        gameType = GameType.HILQDA;
    }

    @Override
    Turn extractEntry(View editLayout, Context context){
        ArrayList<Integer> newVal = new ArrayList<>();
        boolean allZero = true;

        String temp = ((EditText) editLayout.findViewById(R.id.etTeam1)).getText().toString();
        if(temp.length() == 0) newVal.add(0);
        else{
            newVal.add(Integer.parseInt(temp));
            if(Integer.parseInt(temp) != 0) allZero = false;
        }
        temp = ((EditText) editLayout.findViewById(R.id.etTeam2)).getText().toString();
        if(temp.length() == 0) newVal.add(0);
        else{
            newVal.add(Integer.parseInt(temp));
            if(Integer.parseInt(temp) != 0) allZero = false;
        }
        temp = ((EditText) editLayout.findViewById(R.id.etTeam3)).getText().toString();
        if(temp.length() == 0) newVal.add(0);
        else{
            newVal.add(Integer.parseInt(temp));
            if(Integer.parseInt(temp) != 0) allZero = false;
        }

        if(allZero){
            Toast.makeText(context, context.getResources().getText(R.string.toast_all_zero), Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            return new Turn(newVal);
        }
    }

    @Override
    void addToTurnCL(Context context, ConstraintLayout constraintLayout, int id) {

    }

    @Override
    List<Integer> addToGraph() {
        return Arrays.asList(0, 1000);
    }

    @Override
    void checkForWinner() {
        int p0 = getPoints(0), p1 = getPoints(1), p2 = getPoints(2);
        boolean overTh0 = p0>=1000, overTh1 = p1>=1000,  overTh2 = p2>=1000;
        if(overTh0 || overTh1 || overTh2){
            if(!(overTh0||overTh1)) winner = Winner.TEAM_3;
            else if(!(overTh1||overTh2)) winner = Winner.TEAM_1;
            else if(!(overTh0||overTh2)) winner = Winner.TEAM_2;
            else{
                if(p0>p1 && p0>p2) winner = Winner.TEAM_1;
                else if(p1>p0 && p1>p2) winner = Winner.TEAM_2;
                else if(p2>p0 && p2>p1) winner = Winner.TEAM_3;
                else winner = Winner.TIE;
            }
        }
        else winner = Winner.NONE;
    }
}
