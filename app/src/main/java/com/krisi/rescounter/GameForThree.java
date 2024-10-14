package com.krisi.rescounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public abstract class GameForThree extends Game{

    GameForThree(){

    }

    @Override
    void fillInGameLandscape(Context context, View view) {
        ((TextView) view.findViewById(R.id.tvTeam1)).setText(teamNames.get(0));
        ((TextView) view.findViewById(R.id.tvFinal1)).setText(getPoints(0)+"");
        ((TextView) view.findViewById(R.id.tvTeam2)).setText(teamNames.get(1));
        ((TextView) view.findViewById(R.id.tvFinal2)).setText(getPoints(1)+"");
        ((TextView) view.findViewById(R.id.tvTeam3)).setText(teamNames.get(2));
        ((TextView) view.findViewById(R.id.tvFinal3)).setText(getPoints(2)+"");
    }

    @Override
    void initNames(Context context, LayoutInflater layoutInflater, Runnable runnable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View customLayout = layoutInflater.inflate(R.layout.popup_teams_names, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.show();

        ((EditText) customLayout.findViewById(R.id.etTeam1)).setHint(context.getResources().getString(R.string.nameHint) + " " + context.getResources().getString(gameType.party) + " 1");
        ((EditText) customLayout.findViewById(R.id.etTeam2)).setHint(context.getResources().getString(R.string.nameHint) + " " + context.getResources().getString(gameType.party) + " 2");
        ((EditText) customLayout.findViewById(R.id.etTeam3)).setHint(context.getResources().getString(R.string.nameHint) + " " + context.getResources().getString(gameType.party) + " 3");
        if(teamNames.size() != 0){
            ((EditText) customLayout.findViewById(R.id.etTeam1)).setText(Config.currentGame().getTeamNames().get(0));
            ((EditText) customLayout.findViewById(R.id.etTeam2)).setText(Config.currentGame().getTeamNames().get(1));
            ((EditText) customLayout.findViewById(R.id.etTeam3)).setText(Config.currentGame().getTeamNames().get(2));
        }
        ((TextView) customLayout.findViewById(R.id.etAddPtsTeam1)).setText(String.format("%s 1", context.getResources().getString(gameType.party)));
        ((TextView) customLayout.findViewById(R.id.etAddPtsTeam2)).setText(String.format("%s 2", context.getResources().getString(gameType.party)));
        ((TextView) customLayout.findViewById(R.id.etAddPtsTeam3)).setText(String.format("%s 3", context.getResources().getString(gameType.party)));
        Button continueBtn = customLayout.findViewById(R.id.btnDelYes);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = ((TextView) customLayout.findViewById(R.id.etTeam1)).getText().toString();
                String name2 = ((TextView) customLayout.findViewById(R.id.etTeam2)).getText().toString();
                String name3 = ((TextView) customLayout.findViewById(R.id.etTeam3)).getText().toString();
                if(name1.length() == 0 || name2.length() == 0 || name3.length() == 0){
                    Toast.makeText(context, context.getResources().getText(R.string.toast_empty_name), Toast.LENGTH_SHORT).show();
                }
                else {
                    teamNames.clear();
                    teamNames.add(name1);
                    teamNames.add(name2);
                    teamNames.add(name3);
                    winner = Winner.NONE;
                    dialog.dismiss();
                    runnable.run();
                }
            }
        });
    }

    @Override
    ArrayList<LinearLayout> getGraphLayouts(Context context, View view) {
        ArrayList<LinearLayout> ans = new ArrayList<>();
        ans.add((LinearLayout) view.findViewById(R.id.llGraphTeam1));
        ans.add((LinearLayout) view.findViewById(R.id.llGraphTeam2));
        ans.add((LinearLayout) view.findViewById(R.id.llGraphTeam3));
        return ans;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void initEntryPopup(View editLayout, int id) {
        super.initEntryPopup(editLayout,id);
        if (id != -1 && Config.currentGame().getTurns().get(id).getPoints(1) != 0)
            ((EditText) editLayout.findViewById(R.id.etTeam3)).setText(turns.get(id).getPoints(2) + "");
        else
            ((EditText) editLayout.findViewById(R.id.etTeam3)).setText("");

        ((TextView) editLayout.findViewById(R.id.etAddPtsTeam3)).setText(teamNames.get(2));
    }
}
