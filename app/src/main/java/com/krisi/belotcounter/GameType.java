package com.krisi.belotcounter;

import android.content.res.Resources;

public enum GameType {
    BELOT(2, R.style.Base_Theme_BelotCounter_Belot, R.string.belot, R.string.team),
    HILQDA(3, R.style.Base_Theme_BelotCounter_Hilqda, R.string.hilqda, R.string.player),
    SANTACE(2, R.style.Base_Theme_BelotCounter_Santace, R.string.santace, R.string.player),
    BLATO(3, R.style.Base_Theme_BelotCounter_Blato, R.string.blato, R.string.player);

    int plCount;
    int theme;
    int name;
    int party;

    GameType(int plCount, int theme1, int name, int party) {
        this.plCount = plCount;
        this.theme = theme1;
        this.name = name;
        this.party = party;
    }
}
