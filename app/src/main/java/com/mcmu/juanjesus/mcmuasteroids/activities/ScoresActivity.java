package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mcmu.juanjesus.mcmuasteroids.R;
import com.mcmu.juanjesus.mcmuasteroids.adapters.ScoreListAdapter;

import java.util.Objects;

public class ScoresActivity extends ListActivity {

    //region Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        /* Default List
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                MainActivity.scoreStorage.scoreList(10)));
        */

        /* Custom Layout List
        setListAdapter(new ArrayAdapter<String>(this,
                R.layout.activity_scores_list_item,
                R.id.txtv_score_list_item_title,
                MainActivity.scoreStorage.scoreList(10)));
        */

        // Custom Layout And Custom Adapter List
        setListAdapter(new ScoreListAdapter(this, MainActivity.scoreStorage.scoreList(10)));
    }
    //endregion


    //region Click Listener

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Object o = getListAdapter().getItem(position);
        Toast.makeText(this,
                getString(R.string.selection) + ": " + Integer.toString(position) + " - " + o.toString(),
                Toast.LENGTH_SHORT).show();
    }

    //endregion
}
