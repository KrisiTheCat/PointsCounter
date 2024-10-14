package com.krisi.rescounter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGraph#newInstance} factory method to
 * create an instance of getActivity() fragment.
 */
public class FragmentGraph extends Fragment {
    View ROOT;
    public FragmentGraph() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ROOT = inflater.inflate(R.layout.activity_in_game_graph, container, false);
        refreshGraph();
        return ROOT;
    }

    public void refreshGraph(){
        Config.currentGame().customizeGraph(getActivity(), ROOT);
    }
}