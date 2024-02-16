package com.example.belotcounter;

public enum GameType {
    BELOT(
            2,
            R.color.belot1,
            R.color.belot2,
            R.color.belot3,
            R.drawable.gradient_belot,
            R.string.belot,
            R.string.team
    ),
    HILQDA(
            3,
            R.color.hilqda1,
            R.color.hilqda2,
            R.color.hilqda3,
            R.drawable.gradient_hilqda,
            R.string.hilqda,
            R.string.player
    ),
    SANTACE(
            2,
            R.color.santace1,
            R.color.santace2,
            R.color.santace3,
            R.drawable.gradient_santace,
            R.string.santace,
            R.string.player
    ),
    BLATO(
            3,
            R.color.blato1,
            R.color.blato2,
            R.color.blato3,
            R.drawable.gradient_blato,
            R.string.blato,
            R.string.player
    );

    final int plCount;
    final int colorLight;
    final int colorAccent;
    final int colorDark;
    final int gradient;
    final int gameName;
    final int partyName;

    GameType(int plCount, int colorLight, int colorAccent, int colorDark, int gradient, int gameName, int partyName) {
        this.plCount = plCount;
        this.colorLight = colorLight;
        this.colorAccent = colorAccent;
        this.colorDark = colorDark;
        this.gradient = gradient;
        this.gameName = gameName;
        this.partyName = partyName;
    }
}
