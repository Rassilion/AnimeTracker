package com.denizkemal.animetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga;
import com.denizkemal.animetracker.api.MALApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sagop on 11.12.2017.
 */

public class MangaFragment extends Fragment  implements NetworkTask.NetworkTaskListener{
    TableLayout mangaTable;
    public static ArrayList<Manga> mangaList;
    private SwipeRefreshLayout swipeContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.manga_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        new NetworkTask(MALApi.ListType.MANGA,getContext(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,User.username);
        mangaTable = (TableLayout) getView().findViewById(R.id.mangaTable);
        mangaTable.setColumnShrinkable(1,true);

        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.mswipeContainer);
        swipeContainer.setRefreshing(true);
        final MangaFragment h=this;
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new NetworkTask(MALApi.ListType.MANGA,h.getContext(),h).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,User.username);
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
        int listLimit = 50;
        ArrayList<Manga> resultList = null;
        try {
            if (type == MALApi.ListType.MANGA)
                resultList = (ArrayList<Manga>) result;
        } catch (Exception e) {
            e.printStackTrace();
            resultList = null;
        }
        if (resultList != null) {
            mangaList = resultList;
            for (Manga anime: resultList) {
                listLimit --;
                if(listLimit < 0)
                {
                    break;   /// TODO : Remove listlimit
                }
                final TableRow tr = new TableRow(getContext());
                tr.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        int a = mangaTable.indexOfChild(v);

                        Intent animeIntent = new Intent(getContext(), DetailsActivity.class);
                        animeIntent.putExtra("object", mangaList.get(a));
                        animeIntent.putExtra("type", "manga");
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

                mangaTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            }
            swipeContainer.setRefreshing(false);

        }
    }
    @Override
    public void onNetworkTaskError(TaskJob job){

    }
}
