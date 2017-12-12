package com.denizkemal.animetracker;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.MALApi;
import com.denizkemal.animetracker.api.MALModels.AnimeManga.AnimeList;
import com.denizkemal.animetracker.api.MALModels.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AnimeFragment extends Fragment implements NetworkTask.NetworkTaskListener {
    TableLayout animeTable;
    public static ArrayList<Anime> animeList;


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
            animeList = resultList;
            for (Anime anime: resultList) {
                listLimit --;
                if(listLimit < 0)
                {
                    break;   /// TODO : Remove listlimit
                }
                final TableRow tr = new TableRow(getContext());
                tr.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        int a = animeTable.indexOfChild(v);

                        Intent animeIntent = new Intent(getContext(), DetailsActivity.class);
                        animeIntent.putExtra("index", a);
                        getContext().startActivity(animeIntent);
                    }
                } );
                TableRow.LayoutParams marginsRows=
                        new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                marginsRows.setMargins(0,30,0,30);
                tr.setLayoutParams(marginsRows);

                ImageView image = new ImageView(getContext());
                Picasso.with(getContext())
                        .load(anime.getImageUrl())
                        .error(R.drawable.ic_naruto)
                        .into(image);


                TableRow.LayoutParams lp = new TableRow.LayoutParams(400,400);
                lp.setMargins(0,0,20,0);
                image.setLayoutParams(lp);

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
