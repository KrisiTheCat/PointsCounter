package com.example.belotcounter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public enum GameType {
    BELOT(
            2,
            R.color.belot1,
            R.color.belot2,
            R.color.belot3,
            R.drawable.gradient_belot,
            R.drawable.upper_belot,
            R.string.belot,
            R.string.team
    ),
    HILQDA(
            3,
            R.color.hilqda1,
            R.color.hilqda2,
            R.color.hilqda3,
            R.drawable.gradient_hilqda,
            R.drawable.upper_hilqda,
            R.string.hilqda,
            R.string.player
    );

    final int plCount;
    final int colorLight;
    final int colorAccent;
    final int colorDark;
    final int gradient;
    final int upper;
    final int gameName;
    final int partyName;

    GameType(int plCount, int colorLight, int colorAccent, int colorDark, int gradient, int upper, int gameName, int partyName) {
        this.plCount = plCount;
        this.colorLight = colorLight;
        this.colorAccent = colorAccent;
        this.colorDark = colorDark;
        this.gradient = gradient;
        this.upper = upper;
        this.gameName = gameName;
        this.partyName = partyName;
    }
}
