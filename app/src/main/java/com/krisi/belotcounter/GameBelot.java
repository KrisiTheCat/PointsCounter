package com.krisi.belotcounter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBelot extends GameForTwo{

    public GameBelot() {
        super();
        gameType = GameType.BELOT;
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

        boolean kapo = ((CheckBox) editLayout.findViewById(R.id.cbKapo)).isChecked();
        boolean inside = ((CheckBox) editLayout.findViewById(R.id.cbInside)).isChecked();
        if(allZero){
            Toast.makeText(context, context.getResources().getText(R.string.toast_all_zero), Toast.LENGTH_SHORT).show();
            return null;
        }
        else if(inside && newVal.get(0)!=0 && newVal.get(1)!=0) {
            Toast.makeText(context, context.getResources().getText(R.string.toast_invalid_inside), Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            return new Turn(newVal,kapo,inside);
        }
    }

    @Override
    void addToTurnCL(Context context, ConstraintLayout constraintLayout, int id) {
        if(turns.get(id).isInside()){
            ImageView imageView = new ImageView(context);
            imageView.setId(View.generateViewId());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    100,
                    100
            ));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if(turns.get(id).getPoints(1) == 0)
                imageView.setImageResource(R.drawable.inside_arrow_left);
            else
                imageView.setImageResource(R.drawable.inside_arrow_right);

            constraintLayout.addView(imageView);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(imageView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(imageView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.connect(imageView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet.setVerticalBias(imageView.getId(), 0.5f);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    List<Integer> addToGraph() {
        return Arrays.asList(0, 151);
    }

    @Override
    void checkForWinner() {
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

    @Override
    public void initEntryPopup(View editLayout, int id) {
        super.initEntryPopup(editLayout,id);
        editLayout.findViewById(R.id.llBelot).setVisibility(View.VISIBLE);
        if(id!=-1) {
            setSelectedCheckBox(((CheckBox) editLayout.findViewById(R.id.cbKapo)), turns.get(id).isKapo());
            setSelectedCheckBox(((CheckBox) editLayout.findViewById(R.id.cbInside)), turns.get(id).isInside());
        }
        else{
            setSelectedCheckBox(((CheckBox) editLayout.findViewById(R.id.cbKapo)), false);
            setSelectedCheckBox(((CheckBox) editLayout.findViewById(R.id.cbInside)), false);
        }
    }

    void setSelectedCheckBox(CheckBox checkBox, boolean state){
        if(state && !checkBox.isChecked()){
            checkBox.toggle();
        }
        if(!state && checkBox.isChecked()){
            checkBox.toggle();
        }
    }
}
