package com.example.belotcounter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        System.out.println("screen width: " + screenWidth);

        tvLayout = (LinearLayout) ROOT.findViewById(R.id.llTeam);

        tvNames.add((TextView) ROOT.findViewById(R.id.tvTeam1));
        tvNames.add((TextView) ROOT.findViewById(R.id.tvTeam2));

        tvFinal.add((TextView) ROOT.findViewById(R.id.tvFinal1));
        tvFinal.add((TextView) ROOT.findViewById(R.id.tvFinal2));
        if(Config.currentGame().gameType.plCount == 3) {
            tvFinal.add((TextView) ROOT.findViewById(R.id.tvFinal3));
            tvNames.add((TextView) ROOT.findViewById(R.id.tvTeam3));
        }

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
        ROOT.findViewById(R.id.tvTeam3).setOnClickListener(list);

        if(Config.currentGame().winner != Winner.NONE){
            ROOT.findViewById(R.id.addPointsBtn).setVisibility(View.INVISIBLE);
            ROOT.findViewById(R.id.tvFinished).setVisibility(View.VISIBLE);
        }

    }

    void namesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
        editLayout.findViewById(R.id.btnDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry(id);
                editDialog.hide();
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
        for(int i = 0; i < Config.currentGame().gameType.plCount; i++) {
            tvNames.get(i).setText(Config.currentGame().getTeamNames().get(i));
        }
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    void refreshPoints(){
        teamsPoints = new int[Config.currentGame().gameType.plCount];
        tvLayout.removeAllViews();
        for(int j = 0; j < turns.size(); j++) {
            ConstraintLayout constraintLayout = Config.currentGame().generateTurnCL(getActivity(), j);
            int finalJ = j;
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editEntry(finalJ);
                }
            });
            tvLayout.addView(constraintLayout);
            for(int i = 0; i < Config.currentGame().gameType.plCount; i++) {
                teamsPoints[i] += turns.get(j).getPoints(i);
            }
        }
        for(int i = 0; i < Config.currentGame().gameType.plCount; i++){
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