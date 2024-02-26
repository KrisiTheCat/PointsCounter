package com.example.belotcounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        ((EditText) customLayout.findViewById(R.id.etTeam1)).setHint(context.getResources().getString(R.string.nameHint) + " " + context.getResources().getString(gameType.partyName) + " 1");
        ((EditText) customLayout.findViewById(R.id.etTeam2)).setHint(context.getResources().getString(R.string.nameHint) + " " + context.getResources().getString(gameType.partyName) + " 2");
        if(teamNames.size() != 0) {
            ((EditText) customLayout.findViewById(R.id.etTeam1)).setText(Config.currentGame().getTeamNames().get(0));
            ((EditText) customLayout.findViewById(R.id.etTeam2)).setText(Config.currentGame().getTeamNames().get(1));
        }
        ((TextView) customLayout.findViewById(R.id.etAddPtsTeam1)).setText(String.format("%s 1", context.getResources().getString(gameType.partyName)));
        ((TextView) customLayout.findViewById(R.id.etAddPtsTeam2)).setText(String.format("%s 2", context.getResources().getString(gameType.partyName)));
        Button continueBtn = customLayout.findViewById(R.id.btnDelYes);
        customLayout.findViewById(R.id.etAddPtsTeam3).setVisibility(View.GONE);
        customLayout.findViewById(R.id.etTeam3).setVisibility(View.GONE);
        customLayout.findViewById(R.id.viewBehind3).setVisibility(View.GONE);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = ((TextView) customLayout.findViewById(R.id.etTeam1)).getText().toString();
                String name2 = ((TextView) customLayout.findViewById(R.id.etTeam2)).getText().toString();
                System.out.println("length: " + name1.length());
                if(name1.length() == 0 || name2.length() == 0){
                System.out.println("length: " + name2.length());
                    Toast.makeText(context, context.getResources().getText(R.string.toast_empty_name), Toast.LENGTH_SHORT).show();
                }
                else {
                    teamNames.clear();
                    teamNames.add(name1);
                    teamNames.add(name2);
                    winner = Winner.NONE;
                    dialog.dismiss();
                    Toast.makeText(context,context.getResources().getText(R.string.toast_names_changed), Toast.LENGTH_SHORT).show();
                    runnable.run();

                }
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

    @SuppressLint("SetTextI18n")
    @Override
    public void initEntryPopup(View editLayout, int id) {
        super.initEntryPopup(editLayout,id);
        editLayout.findViewById(R.id.etAddPtsTeam3).setVisibility(View.GONE);
        editLayout.findViewById(R.id.etTeam3).setVisibility(View.GONE);
        editLayout.findViewById(R.id.viewBehind3).setVisibility(View.GONE);
    }
}
