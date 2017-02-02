package com.ukhanoff.bubblesort;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ukhanoff.bubblesort.fragments.SortingFragment;

public class SortingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);

        if (savedInstanceState == null) {
            SortingFragment fragment = new SortingFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.sort_fragment_container, fragment)
                    .commit();
        }
    }
}
