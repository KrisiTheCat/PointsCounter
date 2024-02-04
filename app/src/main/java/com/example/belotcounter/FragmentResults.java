package com.example.belotcounter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentResults#newInstance} factory method to
 * create an instance of getActivity() fragment.
 */
public class FragmentResults extends Fragment {

    private final int APP_BAR_HEIGHT = 310; /**TODO**/
    View ROOT = null;

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

    public FragmentResults() {
        // Required empty public constructor
    }

    public static FragmentResults newInstance(String param1, String param2) {
        FragmentResults fragment = new FragmentResults();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ROOT = inflater.inflate(R.layout.activity_in_game_results, container, false);
        init();
        return ROOT;
    }

    void init() {
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Config.currentGame().customizeInGameLandscape(getActivity(), ROOT.findViewById(R.id.inGameLayout));
        }
        else{
            Config.currentGame().customizeInGamePortrait(getActivity(), ROOT.findViewById(R.id.inGameLayout));
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels-200;
        screenWidth = displayMetrics.widthPixels;

        tvLayout = (LinearLayout) ROOT.findViewById(R.id.llTeam);

        tvNames.add((TextView) ROOT.findViewById(R.id.tvTeam1));
        tvNames.add((TextView) ROOT.findViewById(R.id.tvTeam2));

        tvFinal.add((TextView) ROOT.findViewById(R.id.tvFinal1));
        tvFinal.add((TextView) ROOT.findViewById(R.id.tvFinal2));

        turns = Config.currentGame().getTurns();
        refreshNames();
        refreshPoints();


        ((Button) ROOT.findViewById(R.id.addPointsBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEntry(-1);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        editLayout = getLayoutInflater().inflate(R.layout.popup_edit_entry, null);
        builder.setView(editLayout);
        editDialog = builder.create();
        Config.currentGame().customizeEditEntry(getActivity(), editLayout);

        builder = new AlertDialog.Builder(getActivity());
        deleteLayout = getLayoutInflater().inflate(R.layout.popup_delete_entry, null);
        builder.setView(deleteLayout);
        deleteDialog = builder.create();

        View.OnClickListener list = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namesDialog();
            }
        };
        ROOT.findViewById(R.id.tvTeam1).setOnClickListener(list);
        ROOT.findViewById(R.id.tvTeam2).setOnClickListener(list);

        System.out.println("120, Winner:" + Config.currentGame().winner );
        if(Config.currentGame().winner != Winner.NONE){
            ROOT.findViewById(R.id.addPointsBtn).setVisibility(View.INVISIBLE);
            ROOT.findViewById(R.id.tvFinished).setVisibility(View.VISIBLE);
        }

    }

    void namesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View customLayout = getLayoutInflater().inflate(R.layout.popup_teams_names_two, null);
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
                Toast.makeText(getActivity(),getResources().getText(R.string.toast_names_changed), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        Config.currentGame().customizeInGamePortrait(getActivity(), ROOT.findViewById(R.id.inGameLayout));
    }

    @SuppressLint("SetTextI18n")
    void editEntry(int id){
        Config.currentGame().initEntryPopup(editLayout, id);
        editDialog.show();
        ((Button) editLayout.findViewById(R.id.btnDelYes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Turn turn = Config.currentGame().extractEntry(editLayout, getActivity());
                if(turn!=null){
                    if(id==-1) Config.currentGame().addTurn(turn);
                    else Config.currentGame().setTurn(turn, id);
                    refreshPoints();
                    editDialog.hide();
                    Winner oldWin = Config.currentGame().winner;
                    Config.currentGame().checkForWinner();
                    System.out.println(Config.currentGame().winner);
                    if(Config.currentGame().winner != Winner.NONE){
                        Intent i = new Intent(getActivity(), WinScreen.class);
                        startActivity(i);
                        ROOT.findViewById(R.id.addPointsBtn).setVisibility(View.INVISIBLE);
                        ROOT.findViewById(R.id.tvFinished).setVisibility(View.VISIBLE);
                    }
                    if(oldWin != Winner.NONE && Config.currentGame().winner == Winner.NONE){
                        ROOT.findViewById(R.id.addPointsBtn).setVisibility(View.VISIBLE);
                        ROOT.findViewById(R.id.tvFinished).setVisibility(View.GONE);
                    }
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
        View editRibbon = ROOT.findViewById(R.id.cLEditEntry);
        View deleteRibbon = ROOT.findViewById(R.id.cLDeleteEntry);
        for(int j = 0; j < turns.size(); j++) {
            ConstraintLayout constraintLayout = new ConstraintLayout(getActivity());
            TextView textView1 = new TextView(getActivity());
            TextView textView2 = new TextView(getActivity());

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
                textView1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.dosis_bold));
            }
            constraintLayout.addView(textView1);

            ImageView imageView = new ImageView(getActivity());
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
                textView2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.dosis_bold));
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
                                        .y(startView[1]-APP_BAR_HEIGHT)
                                        .alpha(event.getRawX()/screenWidth)
                                        .setDuration(0)
                                        .start();
                            }
                            else {
                                deleteRibbon.animate()
                                        .x(event.getRawX() + dXd)
                                        .y(startView[1]-APP_BAR_HEIGHT)
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
                                    .y(startView[1]-APP_BAR_HEIGHT)
                                    .alpha(0)
                                    .setDuration(100)
                                    .start();
                            deleteRibbon.animate()
                                    .x(startDelete[0])
                                    .y(startView[1]-APP_BAR_HEIGHT)
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

    /*TODO

       @Override
    protected void onStop() {
        super.onStop();
        saveGames();
    }
    private void saveGames(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Config.games.get(0));
        myEdit.putString("game0", json);
        myEdit.apply();
    }*/
}