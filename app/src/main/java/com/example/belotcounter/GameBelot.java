package com.example.belotcounter;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class GameBelot extends Game{
    public GameBelot(String n1, String n2, Context context) {
        super(n1,n2);
        gameType = GameType.BELOT;
    }

    @Override
    void checkForWinner() {
        System.out.println("HERE");
        int p0 = getPoints(0), p1 = getPoints(1);
        if(p0>=151 && p1>=151){
            if(p0>p1) winner = Winner.TEAM_1;
            else if(p0<p1) winner = Winner.TEAM_2;
            else winner = Winner.NONE;
        }
        if(p0>=151 && !turns.get(turns.size()-1).isKapo()) winner = Winner.TEAM_1;
        else if(p1>=151 && !turns.get(turns.size()-1).isKapo()) winner = Winner.TEAM_2;
        else winner = Winner.NONE;
    }
}
