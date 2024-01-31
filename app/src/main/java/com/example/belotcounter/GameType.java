package com.example.belotcounter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public enum GameType {
    BELOT(
            R.color.belot1,
            R.color.belot2,
            R.color.belot3,
            R.drawable.gradient_belot,
            R.drawable.edge_belot,
            R.drawable.upper_belot,
            R.string.belot
    );

    final int colorLight;
    final int colorAccent;
    final int colorDark;
    final int gradient;
    final int edge;
    final int upper;
    final int gameName;

    GameType(int colorLight, int colorAccent, int colorDark, int gradient, int edge, int upper, int gameName) {
        this.colorLight = colorLight;
        this.colorAccent = colorAccent;
        this.colorDark = colorDark;
        this.gradient = gradient;
        this.edge = edge;
        this.upper = upper;
        this.gameName = gameName;
    }
}
