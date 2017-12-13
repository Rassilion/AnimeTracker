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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
    String currentType;
    String[] allStatus = { "Completed" ,"On-hold", "Dropped",
            "Watching", "Plan to watch", "Reading","Plan to read" };
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
        currentType = type;
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

            Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpiner);


            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.animeStatus, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            statusSpinner.setAdapter(adapter);

            statusSpinner.setSelection(adapter.getPosition(currentAnime.getWatchedStatus()));


            EditText epsWatched = (EditText)findViewById(R.id.epsSeen);
            epsWatched.setText( Integer.toString(currentAnime.getWatchedEpisodes()));

            TextView epsTotal = (TextView)findViewById(R.id.totalEpisode);
            epsTotal.setText("/" +Integer.toString(currentAnime.getEpisodes()));

            Spinner yourScore = (Spinner) findViewById(R.id.yourScore);
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                    R.array.scores, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            yourScore.setAdapter(adapter1);

            yourScore.setSelection(adapter1.getPosition(Integer.toString(currentAnime.getScore())));

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
            Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpiner);


            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.mangaStatus, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            statusSpinner.setAdapter(adapter);

            statusSpinner.setSelection(adapter.getPosition(currentManga.getReadStatus()));

           EditText epsWatched = (EditText)findViewById(R.id.epsSeen);
            epsWatched.setText( Integer.toString(currentManga.getChaptersRead()));

           TextView epsTotal = (TextView)findViewById(R.id.totalEpisode);
           epsTotal.setText("/" +Integer.toString(currentManga.getChapters()));

            Spinner yourScore = (Spinner) findViewById(R.id.yourScore);
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                    R.array.scores, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            yourScore.setAdapter(adapter1);

            yourScore.setSelection(adapter1.getPosition(Integer.toString(currentManga.getScore())));


        }

    }
    public void updateButtonClicked(View view)
    {
        Toast.makeText(this, "Updated",Toast.LENGTH_SHORT).show();
        System.out.println("Updated");
        if(currentType.equals("anime"))
        {
            EditText epsWatched = (EditText)findViewById(R.id.epsSeen);
            Spinner yourScore = (Spinner) findViewById(R.id.yourScore);
            Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpiner);
            currentAnime.setWatchedEpisodes(Integer.parseInt(epsWatched.getText().toString()));
            currentAnime.setScore(Integer.parseInt(yourScore.getSelectedItem().toString()));
            currentAnime.setWatchedStatus(statusSpinner.getSelectedItem().toString());
        }
        else
        {
            EditText epsWatched = (EditText)findViewById(R.id.epsSeen);
            Spinner yourScore = (Spinner) findViewById(R.id.yourScore);
            Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpiner);
           currentManga.setChaptersRead(Integer.parseInt(epsWatched.getText().toString()));
            currentManga.setScore(Integer.parseInt(yourScore.getSelectedItem().toString()));
            currentManga.setReadStatus(statusSpinner.getSelectedItem().toString());
        }

    }
    @Override
    public void onNetworkTaskFinished(Object result, TaskJob job, MALApi.ListType type) {
        if (type.equals(MALApi.ListType.ANIME)) {
            Anime anime = (Anime) result;
            Synopsis = (TextView) findViewById(R.id.synopsis);
            Synopsis.setText(Html.fromHtml(anime.getSynopsisString()));


            episodes = (TextView) findViewById(R.id.episodes);
            episodes.setText(Integer.toString(anime.getEpisodes()));

            rankPoint = (TextView) findViewById(R.id.rankPoint);
            rankPoint.setText(Integer.toString(anime.getRank()));

            avaregePoint =(TextView) findViewById(R.id.avaragePoint);
            avaregePoint.setText(anime.getAverageScore() + " / 10");

            avarageCount = (TextView) findViewById(R.id.avarageCount);
            avarageCount.setText(anime.getAverageScoreCount());

            typeOf = (TextView) findViewById(R.id.typeLabel);
            typeOf.setText(anime.getType());

            String openingMusic="";
            for (String items:anime.getOpeningTheme()) {
                openingMusic +=(items + "\n");
            }
            openingTheme = (TextView) findViewById(R.id.openingLabel);
            openingTheme.setText(openingMusic);

            statusLabel = (TextView)findViewById(R.id.statusLabel);
            statusLabel.setText(anime.getStatus());


        }else{
            Manga manga = (Manga) result;
            Synopsis = (TextView) findViewById(R.id.synopsis);
            Synopsis.setText(Html.fromHtml(manga.getSynopsisString()));

            episodes = (TextView) findViewById(R.id.episodes);
            episodes.setText(Integer.toString(manga.getChapters()));

            rankPoint = (TextView) findViewById(R.id.rankPoint);
            rankPoint.setText(Integer.toString(manga.getRank()));

            avaregePoint =(TextView) findViewById(R.id.avaragePoint);
            avaregePoint.setText(manga.getAverageScore() + " / 10");

            avarageCount = (TextView) findViewById(R.id.avarageCount);
            avarageCount.setText(manga.getAverageScoreCount());

            typeOf = (TextView) findViewById(R.id.typeLabel);
            typeOf.setText(manga.getType());
        /*
            String openingMusic="";
            for (String items:currentManga.getOpeningTheme()) {
                openingMusic +=(items + "\n");
            }
            openingTheme = (TextView) findViewById(R.id.openingLabel);
            openingTheme.setText(openingMusic);*/

            statusLabel = (TextView)findViewById(R.id.statusLabel);
            statusLabel.setText(manga.getStatus());
        }
    }

    @Override
    public void onNetworkTaskError(TaskJob job){
    }

}
