package com.denizkemal.animetracker;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
/**
 * Created by sagop on 11.12.2017.
 */

public class AnimeFragment extends Fragment {
    TableLayout animeTable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.anime_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        animeTable = (TableLayout) getView().findViewById(R.id.animeTable);
        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
/* Create a Button to be the row-content. */
        Button b = new Button(getContext());
        b.setText("Dynamic Button");
        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
/* Add Button to row. */
        tr.addView(b);
/* Add row to TableLayout. */
//tr.setBackgroundResource(R.drawable.sf_gradient_03);
        animeTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

}
