package com.denizkemal.animetracker;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.denizkemal.animetracker.api.MALModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.MALApi;

import java.util.ArrayList;


public class AnimeFragment extends Fragment implements NetworkTask.NetworkTaskListener {
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

        new NetworkTask(MALApi.ListType.ANIME,getContext(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"rassilion");
        animeTable = (TableLayout) getView().findViewById(R.id.animeTable);
        animeTable.setColumnShrinkable(1,true);


    }

    @Override
    public void onNetworkTaskFinished(Object result, TaskJob job, MALApi.ListType type) {
        int listLimit = 50;
        ArrayList<Anime> resultList = null;
        try {
            if (type == MALApi.ListType.ANIME)
                resultList = (ArrayList<Anime>) result;
        } catch (Exception e) {
            e.printStackTrace();
            resultList = null;
        }
        if (resultList != null) {
            for (Anime anime: resultList) {
                listLimit --;
                if(listLimit < 0)
                {
                    break;   /// TODO : Remove listlimit
                }
                TableRow tr = new TableRow(getContext());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                ImageView image = new ImageView(getContext());
                image.setBackgroundResource(R.drawable.ic_naruto);
                TableLayout.LayoutParams lp =
                        new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                image.setLayoutParams(new TableRow.LayoutParams(400,400));
                tr.addView(image);
                TextView tw = new TextView(getContext());
                tw.setText(anime.getTitle());
                tw.setSingleLine(false);
                TableRow.LayoutParams tableRowParams=
                        new TableRow.LayoutParams
                                (TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tableRowParams.setMargins(30,150,30,150);
                tw.setLayoutParams(tableRowParams);

                tr.addView(tw);
                animeTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            }

        }
    }
    @Override
    public void onNetworkTaskError(TaskJob job){

    }
}
