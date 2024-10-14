package com.krisi.rescounter;

import static androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAwards#newInstance} factory method to
 * create an instance of getActivity() fragment.
 */
public class FragmentAwards extends Fragment {

    View ROOT = null;

    public FragmentAwards() {
        // Required empty public constructor
    }

    public static FragmentAwards newInstance(String param1, String param2) {
        FragmentAwards fragment = new FragmentAwards();
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
        ROOT = inflater.inflate(R.layout.activity_in_game_awards, container, false);
        init();
        return ROOT;
    }

    @SuppressLint("SetTextI18n")
    void init() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorMain, typedValue, true);
        @ColorInt int colorM = typedValue.data;
        theme.resolveAttribute(R.attr.colorDark, typedValue, true);
        @ColorInt int colorD = typedValue.data;

        ArrayList<Turn> turns = Config.currentGame().getTurns();
        if(!turns.isEmpty())
            ROOT.findViewById(R.id.tvNotEnough).setVisibility(View.GONE);
        else {
            ROOT.findViewById(R.id.svAwards).setVisibility(View.GONE);
            return;
        }
        int team = -1, res = -1, turn = 0;
        for(int i = 0; i < turns.size(); i++){
            for(int t = 0; t < Config.currentGame().gameType.plCount; t++){
                if(turns.get(i).getPoints(t) > res){
                    res = turns.get(i).getPoints(t);
                    team = t;
                    turn = i;
                }
            }
        }
        ConstraintLayout clAward = ROOT.findViewById(R.id.clAward1);
        if(team == -1)
                clAward.setVisibility(View.GONE);
        else {
            ((TextView) clAward.findViewWithTag("teamName")).setText(Config.currentGame().getTeamNames().get(team));
            if (team == 0)
                clAward.setBackgroundTintList(ColorStateList.valueOf(colorM));
            else if (team == 1)
                clAward.setBackgroundTintList(ColorStateList.valueOf(colorD));
            else
                clAward.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.myDark)));

            ((TextView) clAward.findViewWithTag("additionalInfo")).setText(res + " " +
                    getContext().getString(R.string.points) + " " +
                    getContext().getString(R.string.on) + " " +
                    (turn + 1) + " " +
                    getContext().getString(R.string.turn));
        }

        team = -1; res = 10000; turn = 10000;
        for(int i = 0; i < turns.size(); i++){
            for(int t = 0; t < Config.currentGame().gameType.plCount; t++){
                if(turns.get(i).getPoints(t) < res){
                    res = turns.get(i).getPoints(t);
                    team = t;
                    turn = i;
                }
            }
        }
        clAward = ROOT.findViewById(R.id.clAward2);
        if(team == -1)
            clAward.setVisibility(View.GONE);
        else {
            ((TextView) clAward.findViewWithTag("teamName")).setText(Config.currentGame().getTeamNames().get(team));
            if (team == 0)
                clAward.setBackgroundTintList(ColorStateList.valueOf(colorM));
            else if (team == 1)
                clAward.setBackgroundTintList(ColorStateList.valueOf(colorD));
            else
                clAward.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.myDark)));
            ((TextView) clAward.findViewWithTag("additionalInfo")).setText(res + " " +
                    getContext().getString(R.string.points) + " " +
                    getContext().getString(R.string.on) + " " +
                    (turn + 1) + " " +
                    getContext().getString(R.string.turn));
        }

        team = turns.get(0).getWinner();
        int streakFrom = 0;
        int longestStreak = 0;
        int currentStreak = 1;
        for(int i = 1; i < turns.size(); i++){
            if(turns.get(i).getWinner() != turns.get(i-1).getWinner()){
                if(longestStreak < currentStreak){
                    team = turns.get(i-1).getWinner();
                    streakFrom = i-currentStreak;
                    longestStreak = currentStreak;
                    currentStreak = 0;
                }
            }
            else currentStreak++;
        }
        if(longestStreak < currentStreak){
            team = turns.get(turns.size()-1).getWinner();
            longestStreak = currentStreak;
            streakFrom = turns.size()-currentStreak;
        }

        clAward = ROOT.findViewById(R.id.clAward3);
        if(team == -1)
            clAward.setVisibility(View.GONE);
        else {
            ((TextView) clAward.findViewWithTag("teamName")).setText(Config.currentGame().getTeamNames().get(team));
            if (team == 0)
                clAward.setBackgroundTintList(ColorStateList.valueOf(colorM));
            else if (team == 1)
                clAward.setBackgroundTintList(ColorStateList.valueOf(colorD));
            else
                clAward.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.myDark)));
            ((TextView) clAward.findViewWithTag("additionalInfo")).setText(
                    getContext().getString(R.string.from) + " " +
                            (streakFrom + 1) + " " +
                            getContext().getString(R.string.turn) + " " +
                            getContext().getString(R.string.to) + " " +
                            (streakFrom + longestStreak) + " " +
                            getContext().getString(R.string.turn));
        }
    }
}