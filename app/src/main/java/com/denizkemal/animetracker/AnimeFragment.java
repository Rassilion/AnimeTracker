package com.denizkemal.animetracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AnimeFragment extends Fragment implements NetworkTask.NetworkTaskListener {
    TableLayout animeTable;
    private SwipeRefreshLayout swipeContainer;

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

        new NetworkTask(MALApi.ListType.ANIME,getContext(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,User.username);
        animeTable = (TableLayout) getView().findViewById(R.id.animeTable);
        animeTable.setColumnShrinkable(1,true);

        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
        swipeContainer.setRefreshing(true);
        final AnimeFragment h=this;
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new NetworkTask(MALApi.ListType.ANIME,h.getContext(),h).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,User.username);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);




    }

    @Override
    public void onNetworkTaskFinished(Object result, TaskJob job, MALApi.ListType type) {
        animeTable.removeAllViewsInLayout();
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
                        animeIntent.putExtra("object", animeList.get(a));
                        animeIntent.putExtra("type", "anime");
                        getContext().startActivity(animeIntent);
                    }
                } );
                TableLayout.LayoutParams marginsRows=
                        new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                marginsRows.setMargins(10,30,10,30);
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

                animeTable.addView(tr);

            }
            swipeContainer.setRefreshing(false);
        }
    }
    @Override
    public void onNetworkTaskError(TaskJob job){

    }
}
