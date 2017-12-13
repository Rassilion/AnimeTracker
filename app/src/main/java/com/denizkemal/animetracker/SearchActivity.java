package com.denizkemal.animetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga;
import com.denizkemal.animetracker.api.MALApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements NetworkTask.NetworkTaskListener  {
    private TableLayout table;
    private SwipeRefreshLayout swipeContainer;
    public String query;
    public int tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        tab = intent.getIntExtra("tab",0);

        SearchView search = (SearchView) findViewById(R.id.searchbar);
        search.setIconified(false);
        final SearchActivity h=this;
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                h.query=query;
                swipeContainer.setRefreshing(true);
                if(h.tab==0){
                    new NetworkTask(TaskJob.SEARCH,MALApi.ListType.ANIME,h,h).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
                }else{
                    new NetworkTask(TaskJob.SEARCH,MALApi.ListType.MANGA,h,h).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        table = (TableLayout) findViewById(R.id.table);
        table.setColumnShrinkable(1,true);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainerSearch);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(h.tab==0){
                    new NetworkTask(TaskJob.SEARCH,MALApi.ListType.ANIME,h,h).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
                }else{
                    new NetworkTask(TaskJob.SEARCH,MALApi.ListType.MANGA,h,h).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
                }
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
        if(type.equals(MALApi.ListType.ANIME)) {
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
                for (Anime anime : resultList) {
                    listLimit--;
                    if (listLimit < 0) {
                        break;   /// TODO : Remove listlimit
                    }
                    final TableRow tr = new TableRow(this);
                    final SearchActivity h = this;
                    final ArrayList<Anime> finalResultList = resultList;
                    tr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int a = table.indexOfChild(v);

                            Intent animeIntent = new Intent(h, DetailsActivity.class);
                            animeIntent.putExtra("object", finalResultList.get(a));
                            animeIntent.putExtra("type", "anime");
                            h.startActivity(animeIntent);
                        }
                    });
                    TableRow.LayoutParams marginsRows =
                            new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    marginsRows.setMargins(0, 30, 0, 30);
                    tr.setLayoutParams(marginsRows);

                    ImageView image = new ImageView(this);
                    Picasso.with(this)
                            .load(anime.getImageUrl())
                            .error(R.drawable.ic_naruto)
                            .into(image);


                    TableRow.LayoutParams lp = new TableRow.LayoutParams(400, 400);
                    lp.setMargins(0, 0, 20, 0);
                    image.setLayoutParams(lp);

                    tr.addView(image);
                    TextView tw = new TextView(this);
                    tw.setText(anime.getTitle());
                    tw.setSingleLine(false);
                    TableRow.LayoutParams tableRowParams =
                            new TableRow.LayoutParams
                                    (TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    tableRowParams.setMargins(30, 150, 30, 150);
                    tw.setLayoutParams(tableRowParams);

                    tr.addView(tw);

                    table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                }
                swipeContainer.setRefreshing(false);
            }
        }else{
            int listLimit = 50;
            ArrayList<Manga> resultList = null;
            try {
                if (type == MALApi.ListType.ANIME)
                    resultList = (ArrayList<Manga>) result;
            } catch (Exception e) {
                e.printStackTrace();
                resultList = null;
            }
            if (resultList != null) {
                for (Manga anime : resultList) {
                    listLimit--;
                    if (listLimit < 0) {
                        break;   /// TODO : Remove listlimit
                    }
                    final TableRow tr = new TableRow(this);
                    final SearchActivity h = this;
                    final ArrayList<Manga> finalResultList = resultList;
                    tr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int a = table.indexOfChild(v);

                            Intent animeIntent = new Intent(h, DetailsActivity.class);
                            animeIntent.putExtra("object", finalResultList.get(a));
                            animeIntent.putExtra("type", "manga");
                            h.startActivity(animeIntent);
                        }
                    });
                    TableRow.LayoutParams marginsRows =
                            new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    marginsRows.setMargins(0, 30, 0, 30);
                    tr.setLayoutParams(marginsRows);

                    ImageView image = new ImageView(this);
                    Picasso.with(this)
                            .load(anime.getImageUrl())
                            .error(R.drawable.ic_naruto)
                            .into(image);


                    TableRow.LayoutParams lp = new TableRow.LayoutParams(400, 400);
                    lp.setMargins(0, 0, 20, 0);
                    image.setLayoutParams(lp);

                    tr.addView(image);
                    TextView tw = new TextView(this);
                    tw.setText(anime.getTitle());
                    tw.setSingleLine(false);
                    TableRow.LayoutParams tableRowParams =
                            new TableRow.LayoutParams
                                    (TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    tableRowParams.setMargins(30, 150, 30, 150);
                    tw.setLayoutParams(tableRowParams);

                    tr.addView(tw);

                    table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                }
                swipeContainer.setRefreshing(false);
            }
        }
    }
    @Override
    public void onNetworkTaskError(TaskJob job){

    }
}
