package com.krisi.belotcounter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    public GameType gameType;
    protected ArrayList<String> teamNames;
    protected ArrayList<Turn> turns;
    public Winner winner;


    Game(){
        teamNames = new ArrayList<>();
        turns = new ArrayList<>();
        winner = Winner.NONE;
    }

    abstract void initNames(Context context, LayoutInflater layoutInflater, Runnable runnable);
    abstract void checkForWinner();
    abstract Turn extractEntry(View editLayout, Context context);
    abstract ArrayList<LinearLayout> getGraphLayouts(Context context, View view);
    abstract void addToTurnCL(Context context, ConstraintLayout constraintLayout, int id);
    abstract List<Integer> addToGraph();
    abstract void fillInGameLandscape(Context context, View view);


    public ArrayList<String> getTeamNames() {
        return teamNames;
    }

    public void setTeamNames(ArrayList<String> teamNames) {
        this.teamNames = teamNames;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }
    public void addTurn(Turn turn) {
        turns.add(turn);
    }
    public void setTurn(Turn turn, int id) {
        turns.set(id, turn);
    }
    public void deleteTurn(int id){
        turns.remove(id);
    }

    public int getPoints(int team){
        int sum = 0;
        for(int i = 0; i < turns.size();i++){
            sum += turns.get(i).getPoints(team);
        }
        return sum;
    }


    @SuppressLint("ResourceType")
    public void customizeGameBtn(Context context, View view, boolean gradient){
        view.setClipToOutline(true);
        if (gradient)
            view.findViewById(R.id.viewBackground).setVisibility(View.GONE);
        else
            view.findViewById(R.id.viewBackgroundGrad).setVisibility(View.GONE);
        if(winner != Winner.NONE) {
            ((TextView) view.findViewById(R.id.edgeText)).setText(context.getResources().getString(gameType.name));
        }

        ((TextView) view.findViewById(R.id.tvTeam1)).setText(teamNames.get(0));
        ((TextView) view.findViewById(R.id.tvPts1)).setText(getPoints(0)+"");
        ((TextView) view.findViewById(R.id.tvTeam2)).setText(teamNames.get(1));
        ((TextView) view.findViewById(R.id.tvPts2)).setText(getPoints(1)+"");
        if(gameType.plCount == 2){
            view.findViewById(R.id.linearLayout3).setVisibility(View.GONE);
            view.findViewById(R.id.tvTeam3).setVisibility(View.GONE);
            view.findViewById(R.id.tvPts3).setVisibility(View.GONE);
        }
        else{
            ((TextView) view.findViewById(R.id.tvTeam3)).setText(teamNames.get(2));
            ((TextView) view.findViewById(R.id.tvPts3)).setText(getPoints(2)+"");
            view.findViewById(R.id.linearLayout3).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tvTeam3).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tvPts3).setVisibility(View.VISIBLE);
        }
    }
    public void customizeInGameLandscape(Context context, View view){
        System.out.println( context.getTheme());
//        TypedValue value = new TypedValue();
        ((TextView) view.findViewById(R.id.tvTeam1)).setTextColor(Color.WHITE);
        ((TextView) view.findViewById(R.id.tvFinal1)).setTextColor(Color.WHITE);
//        context.getTheme().resolveAttribute(R.attr.colorMain, value, true);
//        view.findViewById(R.id.llTeam1).setBackgroundColor(ContextCompat.getColor(context, value.data));
        ((TextView) view.findViewById(R.id.tvTeam2)).setTextColor(Color.WHITE);
        ((TextView) view.findViewById(R.id.tvFinal2)).setTextColor(Color.WHITE);
//        context.getTheme().resolveAttribute(R.attr.colorDark, value, true);
//        view.findViewById(R.id.llTeam2).setBackgroundColor(ContextCompat.getColor(context, value.data));
        ((TextView) view.findViewById(R.id.tvTeam3)).setTextColor(Color.WHITE);
        ((TextView) view.findViewById(R.id.tvFinal3)).setTextColor(Color.WHITE);
        view.findViewById(R.id.llTeam3).setBackgroundColor(ContextCompat.getColor(context, R.color.myDark));
    }
    public void customizeInGamePortrait(Context context, View view){
        if(gameType.plCount == 2){
            view.findViewById(R.id.tvTeam3).setVisibility(View.GONE);
            view.findViewById(R.id.view9).setVisibility(View.GONE);
            view.findViewById(R.id.tvFinal3).setVisibility(View.GONE);
        }
    }
    public void customizeGraph(Context context, View view){
        ArrayList<LinearLayout> llTeams = getGraphLayouts(context, view);
        for(int i = 0; i < gameType.plCount; i++){
            ((TextView) llTeams.get(i).findViewWithTag("tvGraphTeam")).setText(teamNames.get(i));
        }
        initGraph(context, view.findViewById(R.id.idGraphView));
    }

    @SuppressLint("DefaultLocale")
    public ConstraintLayout generateTurnCL(Context context, int id){
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        constraintLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                100
        ));

        ArrayList<Integer> textViewsIds = new ArrayList<>();
        ConstraintSet constraintSet = new ConstraintSet();
        for(int t = 0; t < gameType.plCount; t++) {
            TextView textView = new TextView(context);
            textView.setId(View.generateViewId());
            textView.setLayoutParams(new ConstraintLayout.LayoutParams(0, 100));
            textView.setGravity(Gravity.CENTER);
            textView.setText(turns.get(id).getPoints(t)+"");
            textView.setFreezesText(true);
            if(turns.get(id).getPoints(t)<0)
                textView.setTextColor(ContextCompat.getColor(context, R.color.myRed));
            else
                textView.setTextColor(ContextCompat.getColor(context, R.color.myDark));

            textView.setTextSize(18);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textView.setTypeface(ResourcesCompat.getFont(context, R.font.dosis_bold));
            }
            constraintLayout.addView(textView);
            textViewsIds.add(textView.getId());
        }

        constraintSet.clone(constraintLayout);
        for(int i = 0; i < gameType.plCount; i++) {
            if(i==0)
                constraintSet.connect(textViewsIds.get(i), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            else
                constraintSet.connect(textViewsIds.get(i), ConstraintSet.START, textViewsIds.get(i-1), ConstraintSet.END);
            if(i==gameType.plCount-1)
                constraintSet.connect(textViewsIds.get(i), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            else
                constraintSet.connect(textViewsIds.get(i), ConstraintSet.END, textViewsIds.get(i+1), ConstraintSet.START);
            constraintSet.connect(textViewsIds.get(i), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet.setHorizontalBias(textViewsIds.get(i), 0.5f);
        }
        constraintSet.applyTo(constraintLayout);
        addToTurnCL(context, constraintLayout, id);
        return constraintLayout;
    }
    void initGraph(Context context, GraphView graphView) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorMain, typedValue, true);
        @ColorInt int colorM = typedValue.data;
        context.getTheme().resolveAttribute(R.attr.colorDark, typedValue, true);
        @ColorInt int colorD = typedValue.data;
        graphView.removeAllSeries();
        LineGraphSeries<DataPoint> series = null;
        List<Integer> setNumbers = addToGraph();
        for(int line : setNumbers) {
            series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    new DataPoint(0, line),
                    new DataPoint(turns.size(), line)
            });
            series.setColor(ContextCompat.getColor(context, R.color.myDark));
            graphView.addSeries(series);
        }
        if(turns.size()==0) return;
        double max_y = setNumbers.get(1);
        for(int i = 0 ; i < gameType.plCount; i++) {
            DataPoint[] values = new DataPoint[turns.size() + 1];
            int s = 0;
            values[0] = new DataPoint(0, 0);
            for (int j = 0; j < turns.size(); j++) {
                s += turns.get(j).getPoints(i);
                values[j + 1] = new DataPoint(j + 1, s);
                max_y = Math.max(s, max_y);
            }
            series = new LineGraphSeries<DataPoint>(values);
            switch (i) {
                case 0:
                    series.setColor(colorM);
                    break;
                case 1:
                    series.setColor(colorD);
                    break;
                default:
                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(12);
                    paint.setColor(ContextCompat.getColor(context, R.color.myDark));
                    paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
                    series.setDrawAsPath(true);
                    series.setCustomPaint(paint);
            }
            series.setThickness(12);
            graphView.addSeries(series);
        }
        graphView.getGridLabelRenderer().setNumHorizontalLabels(turns.size()+1);
        double max_x = turns.size();
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(max_x);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(max_y);

    }


    public void initEntryPopup(View editLayout, int id){
        if(id!=-1 && Config.currentGame().getTurns().get(id).getPoints(0)!=0)
            ((EditText) editLayout.findViewById(R.id.etTeam1)).setText(turns.get(id).getPoints(0)+"");
        else
            ((EditText) editLayout.findViewById(R.id.etTeam1)).setText("");

        if(id!=-1 && Config.currentGame().getTurns().get(id).getPoints(1)!=0)
            ((EditText) editLayout.findViewById(R.id.etTeam2)).setText(turns.get(id).getPoints(1)+"");
        else
            ((EditText) editLayout.findViewById(R.id.etTeam2)).setText("");

        ((TextView)editLayout.findViewById(R.id.etAddPtsTeam1)).setText(teamNames.get(0));
        ((TextView)editLayout.findViewById(R.id.etAddPtsTeam2)).setText(teamNames.get(1));

        editLayout.findViewById(R.id.llBelot).setVisibility(View.GONE);

        if(id!=-1) {
            editLayout.findViewById(R.id.btnDel).setVisibility(View.VISIBLE);
        }
        else{
            editLayout.findViewById(R.id.btnDel).setVisibility(View.GONE);
        }
    }
}
