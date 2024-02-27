package com.example.belotcounter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameSantace extends GameForTwo{
    public GameSantace() {
        super();
        gameType = GameType.SANTACE;
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

        return new Turn(newVal);
    }

    @Override
    void addToTurnCL(Context context, ConstraintLayout constraintLayout, int id) {

    }

    @Override
    List<Integer> addToGraph() {
        return Arrays.asList(0, 11);
    }

    @Override
    void checkForWinner() {
        int p0 = getPoints(0), p1 = getPoints(1);
        if(p0>=11 && p1>=11){
            if(p0>p1) winner = Winner.TEAM_1;
            else if(p0<p1) winner = Winner.TEAM_2;
            else winner = Winner.NONE;
        }
        winner = Winner.NONE;
    }
}

