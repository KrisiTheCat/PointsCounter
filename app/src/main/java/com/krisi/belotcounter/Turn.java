package com.krisi.belotcounter;

import java.util.ArrayList;

public class Turn implements Cloneable{
    private ArrayList<Integer> points;
    private boolean kapo;
    private boolean inside;

    Turn(){
        points = new ArrayList<>();
        kapo = false;
        inside = false;
    }

    public Turn(ArrayList<Integer> points, boolean kapo, boolean inside) {  // belot
        this.points = points;
        this.kapo = kapo;
        this.inside = inside;
    }

    public Turn(ArrayList<Integer> points) { //hilqda
        this.points = points;
        this.kapo = false;
        this.inside = false;
    }

    public boolean isKapo() {
        return kapo;
    }

    public void setKapo(boolean kapo) {
        this.kapo = kapo;
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }

    public ArrayList<Integer> getPoints() {
        return points;
    }
    public int getPoints(int team){
        return points.get(team);
    }
    public int getWinner(){
        int id = 0;
        for(int j = 1; j < points.size(); j++){
            if(points.get(j)>points.get(id)) id = j;
        }
        return id;
    }
}
