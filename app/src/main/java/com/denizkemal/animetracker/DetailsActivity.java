package com.denizkemal.animetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga;
import com.denizkemal.animetracker.api.MALApi;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class DetailsActivity extends AppCompatActivity implements NetworkTask.NetworkTaskListener {

    public TextView nameLabel;
    public ImageView image;
    public TextView Synopsis;
    public TextView episodes;
    public TextView rankPoint;
    public TextView avaregePoint;
    public TextView avarageCount;
    public TextView typeOf;
    public TextView openingTheme;
    public TextView statusLabel;
    public Anime currentAnime;
    public Manga currentManga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent intent = getIntent();
        int a = intent.getIntExtra("index",0);
        String s = Integer.toString(a);
        String type = intent.getStringExtra("type");
        if (type.equals("anime")){

            currentAnime =  AnimeFragment.animeList.get(a);
            Bundle data =new Bundle();
            data.putInt("recordID",currentAnime.getId());
            new NetworkTask(TaskJob.GETDETAILS,MALApi.ListType.ANIME,this,data,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,User.username);

            Toast.makeText(this, currentAnime.getTitle(),Toast.LENGTH_SHORT).show();
            nameLabel = (TextView)findViewById(R.id.nameLabel);
            nameLabel.setText(currentAnime.getTitle());
            image = (ImageView)findViewById(R.id.logo);
            Picasso.with(this)
                    .load(currentAnime.getImageUrl())
                    .error(R.drawable.ic_naruto)
                    .into(image);

        }
        else if (type.equals("manga")){
            currentManga =  MangaFragment.mangaList.get(a);
            Bundle data =new Bundle();
            data.putInt("recordID",currentManga.getId());
            new NetworkTask(TaskJob.GETDETAILS,MALApi.ListType.MANGA,this,data,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,User.username);

            Toast.makeText(this, currentManga.getTitle(),Toast.LENGTH_SHORT).show();
            nameLabel = (TextView)findViewById(R.id.nameLabel);
            nameLabel.setText(currentManga.getTitle());
            image = (ImageView)findViewById(R.id.logo);
            Picasso.with(this)
                    .load(currentManga.getImageUrl())
                    .error(R.drawable.ic_naruto)
                    .into(image);

        }

    }
    @Override
    public void onNetworkTaskFinished(Object result, TaskJob job, MALApi.ListType type) {
        if (type.equals(MALApi.ListType.ANIME)) {
            currentAnime = (Anime) result;
            Synopsis = (TextView) findViewById(R.id.synopsis);
            Synopsis.setText(Html.fromHtml(currentAnime.getSynopsisString()));


            episodes = (TextView) findViewById(R.id.episodes);
            episodes.setText(Integer.toString(currentAnime.getEpisodes()));

            rankPoint = (TextView) findViewById(R.id.rankPoint);
            rankPoint.setText(Integer.toString(currentAnime.getRank()));

            avaregePoint =(TextView) findViewById(R.id.avaragePoint);
            avaregePoint.setText(currentAnime.getAverageScore() + " / 10");

            avarageCount = (TextView) findViewById(R.id.avarageCount);
            avarageCount.setText(currentAnime.getAverageScoreCount());

            typeOf = (TextView) findViewById(R.id.typeLabel);
            typeOf.setText(currentAnime.getType());

            String openingMusic="";
            for (String items:currentAnime.getOpeningTheme()) {
                openingMusic +=(items + "\n");
            }
            openingTheme = (TextView) findViewById(R.id.openingLabel);
            openingTheme.setText(openingMusic);

            statusLabel = (TextView)findViewById(R.id.statusLabel);
            statusLabel.setText(currentAnime.getStatus());


        }else{
            currentManga = (Manga) result;
            Synopsis = (TextView) findViewById(R.id.synopsis);
            Synopsis.setText(Html.fromHtml(currentManga.getSynopsisString()));

            episodes = (TextView) findViewById(R.id.episodes);
            episodes.setText(Integer.toString(currentManga.getChapters()));

            rankPoint = (TextView) findViewById(R.id.rankPoint);
            rankPoint.setText(Integer.toString(currentManga.getRank()));

            avaregePoint =(TextView) findViewById(R.id.avaragePoint);
            avaregePoint.setText(currentManga.getAverageScore() + " / 10");

            avarageCount = (TextView) findViewById(R.id.avarageCount);
            avarageCount.setText(currentManga.getAverageScoreCount());

            typeOf = (TextView) findViewById(R.id.typeLabel);
            typeOf.setText(currentManga.getType());
        /*
            String openingMusic="";
            for (String items:currentManga.getOpeningTheme()) {
                openingMusic +=(items + "\n");
            }
            openingTheme = (TextView) findViewById(R.id.openingLabel);
            openingTheme.setText(openingMusic);*/

            statusLabel = (TextView)findViewById(R.id.statusLabel);
            statusLabel.setText(currentManga.getStatus());
        }
    }

    @Override
    public void onNetworkTaskError(TaskJob job){
    }

}
