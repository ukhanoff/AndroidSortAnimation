package com.ukhanoff.bubblesort.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ukhanoff.bubblesort.R;

/**
 * Created by ukhanoff on 2/2/17.
 */

public class SortingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sorting_main_fragment_view, container, false);
    }
}
