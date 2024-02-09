package com.example.belotcounter;

import android.app.AlertDialog;
import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class GameForTwo extends Game{

    GameForTwo(){

    }

    @Override
    void fillInGameLandscape(Context context, View view) {
        view.findViewById(R.id.llTeam3).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tvTeam1)).setText(teamNames.get(0));
        ((TextView) view.findViewById(R.id.tvFinal1)).setText(getPoints(0)+"");
        ((TextView) view.findViewById(R.id.tvTeam2)).setText(teamNames.get(1));
        ((TextView) view.findViewById(R.id.tvFinal2)).setText(getPoints(1)+"");
    }

    @Override
    void initNames(Context context, LayoutInflater layoutInflater, Runnable runnable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View customLayout = layoutInflater.inflate(R.layout.popup_teams_names, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.show();
        customLayout.findViewById(R.id.etAddPtsTeam3).setVisibility(View.GONE);
        customLayout.findViewById(R.id.etTeam3).setVisibility(View.GONE);
        customLayout.findViewById(R.id.viewBehind3).setVisibility(View.GONE);
        ((TextView) customLayout.findViewById(R.id.etAddPtsTeam1)).setText(String.format("%s 1", context.getResources().getString(gameType.partyName)));
        ((TextView) customLayout.findViewById(R.id.etAddPtsTeam2)).setText(String.format("%s 2", context.getResources().getString(gameType.partyName)));
        Button continueBtn = customLayout.findViewById(R.id.btnDelYes);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = ((TextView) customLayout.findViewById(R.id.etTeam1)).getText().toString();
                String name2 = ((TextView) customLayout.findViewById(R.id.etTeam2)).getText().toString();
                teamNames.add(name1);
                teamNames.add(name2);
                winner = Winner.NONE;
                dialog.dismiss();
                runnable.run();
            }
        });
    }

    @Override
    ArrayList<LinearLayout> getGraphLayouts(Context context, View view) {
        view.findViewById(R.id.llGraphTeam3).setVisibility(View.GONE);
        ArrayList<LinearLayout> ans = new ArrayList<>();
        ans.add((LinearLayout) view.findViewById(R.id.llGraphTeam1));
        ans.add((LinearLayout) view.findViewById(R.id.llGraphTeam2));
        return ans;
    }
}
