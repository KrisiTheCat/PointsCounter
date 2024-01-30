package com.example.belotcounter;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

public class GameBelot extends Game{
    public GameBelot(String n1, String n2, Context context) {
        super(n1,n2);
        gameType = GameType.BELOT;
    }

    @Override
    void checkForWinner() {

    }
}
