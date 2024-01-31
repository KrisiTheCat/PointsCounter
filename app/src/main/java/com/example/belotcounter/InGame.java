package com.example.belotcounter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InGame extends AppCompatActivity {


    int[] teamsPoints = null;
    ArrayList<Turn> turns = new ArrayList<>();
    ArrayList<TextView> tvNames = new ArrayList<>();
    ArrayList<TextView> tvFinal = new ArrayList<>();
    LinearLayout tvLayout = null;
    AlertDialog editDialog;
    View editLayout;
    ArrayList<EditText> etPointsPopup = new ArrayList<>();

    AlertDialog deleteDialog;
    View deleteLayout;

    int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        init();
    }

    void init() {
        if(InGame.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Config.currentGame().customizeInGameLandscape(InGame.this, findViewById(R.id.inGameLayout));
        }
        else{
            Config.currentGame().customizeInGamePortrait(InGame.this, findViewById(R.id.inGameLayout));
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        tvLayout = (LinearLayout) findViewById(R.id.llTeam);

        tvNames.add((TextView) findViewById(R.id.tvTeam1));
        tvNames.add((TextView) findViewById(R.id.tvTeam2));

        tvFinal.add((TextView) findViewById(R.id.tvFinal1));
        tvFinal.add((TextView) findViewById(R.id.tvFinal2));

        turns = Config.currentGame().getTurns();
        refreshNames();
        refreshPoints();


        ((Button) findViewById(R.id.addPointsBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEntry(-1);
            }
        });
        ((android.widget.ImageButton) findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(InGame.this);
        editLayout = getLayoutInflater().inflate(R.layout.popup_edit_entry, null);
        builder.setView(editLayout);
        editDialog = builder.create();
        etPointsPopup.add((EditText) editLayout.findViewById(R.id.etTeam1));
        etPointsPopup.add((EditText) editLayout.findViewById(R.id.etTeam2));
        Config.currentGame().customizeEditEntry(InGame.this, editLayout);

        builder = new AlertDialog.Builder(InGame.this);
        deleteLayout = getLayoutInflater().inflate(R.layout.popup_delete_entry, null);
        builder.setView(deleteLayout);
        deleteDialog = builder.create();

        findViewById(R.id.tvTeam1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namesDialog();
            }
        });

    }

    void namesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View customLayout = getLayoutInflater().inflate(R.layout.popup_teams_names, null);
        builder.setView(customLayout);
        ((EditText) customLayout.findViewById(R.id.etTeam1)).setText(Config.currentGame().getTeamNames().get(0));
        ((EditText) customLayout.findViewById(R.id.etTeam2)).setText(Config.currentGame().getTeamNames().get(1));

        AlertDialog dialog = builder.create();
        dialog.show();

        Button continueBtn = (Button) customLayout.findViewById(R.id.btnDelYes);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> names = new ArrayList<>();
                names.add(((TextView) customLayout.findViewById(R.id.etTeam1)).getText().toString());
                names.add(((TextView) customLayout.findViewById(R.id.etTeam2)).getText().toString());
                Config.currentGame().setTeamNames(names);
                refreshNames();
                Toast.makeText(InGame.this,getResources().getText(R.string.toast_names_changed), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        Config.currentGame().customizeInGamePortrait(InGame.this, findViewById(R.id.inGameLayout));
    }

    @SuppressLint("SetTextI18n")
    void editEntry(int id){
        editDialog.show();

        for(int i = 0; i < etPointsPopup.size(); i++){
            if(id!=-1 && Config.currentGame().getTurns().get(id).getPoints(i)!=0)
                etPointsPopup.get(i).setText(Config.currentGame().getTurns().get(id).getPoints(i)+"");
            else
                etPointsPopup.get(i).setText("");
        }
        ((TextView)editLayout.findViewById(R.id.etAddPtsTeam1)).setText(Config.currentGame().getTeamNames().get(0));
        ((TextView)editLayout.findViewById(R.id.etAddPtsTeam2)).setText(Config.currentGame().getTeamNames().get(1));
        if(id!=-1) {
            ((CheckBox) editLayout.findViewById(R.id.cbKapo)).setSelected(Config.currentGame().getTurns().get(id).isKapo());
            ((CheckBox) editLayout.findViewById(R.id.cbInside)).setSelected(Config.currentGame().getTurns().get(id).isInside());
        }
        ((Button) editLayout.findViewById(R.id.btnDelYes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> newVal = new ArrayList<>();
                boolean allZero = true;
                for(int i = 0; i < etPointsPopup.size(); i++){
                    String temp = etPointsPopup.get(i).getText().toString();
                    if(temp.length() == 0) newVal.add(0);
                    else{
                        newVal.add(Integer.parseInt(temp));
                        if(Integer.parseInt(temp) != 0) allZero = false;
                    }
                }
                boolean kapo = ((CheckBox) editLayout.findViewById(R.id.cbKapo)).isChecked();
                boolean inside = ((CheckBox) editLayout.findViewById(R.id.cbInside)).isChecked();
                if(allZero){
                    Toast.makeText(InGame.this, getResources().getText(R.string.toast_all_zero), Toast.LENGTH_SHORT).show();
                }
                else if(inside && newVal.get(0)!=0 && newVal.get(1)!=0) {
                    Toast.makeText(InGame.this, getResources().getText(R.string.toast_invalid_inside), Toast.LENGTH_SHORT).show();
                }
                else{
                    if(id == -1)
                        Config.currentGame().addTurn(new Turn(newVal,kapo,inside));
                    else
                        Config.currentGame().setTurn(new Turn(newVal,kapo,inside), id);
                    refreshPoints();
                    editDialog.hide();
                }
            }
        });
    }

    void deleteEntry(int id){
        deleteDialog.show();

        ((Button) deleteLayout.findViewById(R.id.btnDelYes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.currentGame().deleteTurn(id);
                refreshPoints();
                deleteDialog.hide();
            }
        });

        ((Button) deleteLayout.findViewById(R.id.btnDelNo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.hide();
            }
        });
    }

    void refreshNames() {
        for(int i = 0; i < Config.TEAMS_COUNT; i++) {
            tvNames.get(i).setText(Config.currentGame().getTeamNames().get(i));
        }
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    void refreshPoints(){
        teamsPoints = new int[Config.TEAMS_COUNT];
        tvLayout.removeAllViews();
        View editRibbon = findViewById(R.id.cLEditEntry);
        View deleteRibbon = findViewById(R.id.cLDeleteEntry);
        for(int j = 0; j < turns.size(); j++) {
            ConstraintLayout constraintLayout = new ConstraintLayout(this);
            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);

            constraintLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    100
            ));
            ConstraintSet constraintSet = new ConstraintSet();

            textView1.setId(View.generateViewId());
            textView1.setLayoutParams(new ConstraintLayout.LayoutParams(0,100));
            textView1.setGravity(Gravity.CENTER);
            textView1.setText(turns.get(j).getPoints(0)+"");
            textView1.setFreezesText(true);
            textView1.setTextSize(18);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textView1.setTypeface(ResourcesCompat.getFont(this, R.font.dosis_bold));
            }
            constraintLayout.addView(textView1);

            ImageView imageView = new ImageView(this);
            if(turns.get(j).isInside()){
                imageView.setId(View.generateViewId());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        100,
                        100
                ));
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if(turns.get(j).getPoints(0) == 0)
                    imageView.setImageResource(R.drawable.inside_arrow_left);
                else
                    imageView.setImageResource(R.drawable.inside_arrow_right);

                constraintLayout.addView(imageView);
            }

            textView2.setId(View.generateViewId());
            textView2.setLayoutParams(new ConstraintLayout.LayoutParams(0,100));
            textView2.setGravity(Gravity.CENTER);
            textView2.setText(turns.get(j).getPoints(1)+"");
            textView2.setFreezesText(true);
            textView2.setTextSize(18);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textView2.setTypeface(ResourcesCompat.getFont(this, R.font.dosis_bold));
            }
            constraintLayout.addView(textView2);

            constraintSet.clone(constraintLayout);

            constraintSet.connect(textView1.getId(), ConstraintSet.END, textView2.getId(), ConstraintSet.START);
            constraintSet.connect(textView1.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(textView1.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet.setHorizontalBias(textView1.getId(), 0.5f);

            if(turns.get(j).isInside()){
                constraintSet.connect(imageView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                constraintSet.connect(imageView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                constraintSet.connect(imageView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraintSet.setVerticalBias(imageView.getId(), 0.5f);
            }

            constraintSet.connect(textView2.getId(), ConstraintSet.START, textView1.getId(), ConstraintSet.END);
            constraintSet.connect(textView2.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.connect(textView2.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet.setHorizontalBias(textView2.getId(), 0.5f);

            constraintSet.applyTo(constraintLayout);

            int finalJ = j;
            constraintLayout.setOnTouchListener(new View.OnTouchListener() {
                float dXe, dXd;
                int[] startView = new int[2];
                int[] startMotion = new int[2];
                int[] startEdit = new int[2];
                int[] startDelete = new int[2];
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:;
                            startMotion[0] = (int) event.getRawX();
                            startMotion[1] = (int) event.getRawY();
                            view.getLocationInWindow(startView);
                            editRibbon.getLocationInWindow(startEdit);
                            deleteRibbon.getLocationInWindow(startDelete);
                            dXe = startEdit[0] - event.getRawX();
                            startView[1] -= 115;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(event.getRawX() > startMotion[0]){
                                editRibbon.animate()
                                        .x(event.getRawX() + dXe)
                                        .y(startView[1])
                                        .alpha(event.getRawX()/screenWidth)
                                        .setDuration(0)
                                        .start();
                            }
                            else {
                                deleteRibbon.animate()
                                        .x(event.getRawX() + dXd)
                                        .y(startView[1])
                                        .alpha(1-event.getRawX()/screenWidth)
                                        .setDuration(0)
                                        .start();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if(event.getRawX() > startMotion[0] && event.getRawX()/screenWidth >= 0.75){
                                editEntry(finalJ);
                            }
                            if(event.getRawX() < startMotion[0] && event.getRawX()/screenWidth <= 0.25){
                                deleteEntry(finalJ);
                            }
                            editRibbon.animate()
                                    .x(startEdit[0])
                                    .y(startView[1])
                                    .alpha(0)
                                    .setDuration(100)
                                    .start();
                            deleteRibbon.animate()
                                    .x(startDelete[0])
                                    .y(startView[1])
                                    .alpha(0)
                                    .setDuration(100)
                                    .start();
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });

            tvLayout.addView(constraintLayout);
            teamsPoints[0] += turns.get(j).getPoints(0);
            teamsPoints[1] += turns.get(j).getPoints(1);
        }
        for(int i = 0; i < Config.TEAMS_COUNT; i++){
            tvFinal.get(i).setText(teamsPoints[i]+"");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveGames();
    }
    private void saveGames(){
        /*SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Config.games.get(0));
        myEdit.putString("game0", json);
        myEdit.apply();*/
    }
}