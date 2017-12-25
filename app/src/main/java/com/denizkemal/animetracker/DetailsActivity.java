package com.denizkemal.animetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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


public class DetailsActivity extends AppCompatActivity implements NetworkTask.NetworkTaskListener {
    private SwipeRefreshLayout swipeContainer;
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
    public boolean isNew;
    String[] allStatus = {"Completed", "On-hold", "Dropped",
            "Watching", "Plan to watch", "Reading", "Plan to read"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        int a = intent.getIntExtra("index", 0);
        String s = Integer.toString(a);
        String type = intent.getStringExtra("type");
        currentType = type;
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainerDetails);
        swipeContainer.setRefreshing(true);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        if (type.equals("anime")) {
            currentAnime = (Anime) intent.getSerializableExtra("object");
            setTitle(currentAnime.getTitle());
            Bundle data = new Bundle();
            data.putInt("recordID", currentAnime.getId());
            new NetworkTask(TaskJob.GETDETAILS, MALApi.ListType.ANIME, this, data, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, User.username);


            final DetailsActivity h = this;
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Bundle data = new Bundle();
                    data.putInt("recordID", currentAnime.getId());
                    new NetworkTask(TaskJob.GETDETAILS, MALApi.ListType.ANIME, h, data, h).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, User.username);

                }
            });


            if (currentAnime.getStatus() == null) {
                isNew = true;
            } else {
                isNew = false;
            }
            Toast.makeText(this, currentAnime.getTitle(), Toast.LENGTH_SHORT).show();
            nameLabel = (TextView) findViewById(R.id.nameLabel);
            nameLabel.setText(currentAnime.getTitle());
            image = (ImageView) findViewById(R.id.logo);
            Picasso.with(this)
                    .load(currentAnime.getImageUrl())
                    .error(R.drawable.ic_naruto)
                    .into(image);


        } else if (type.equals("manga")) {
            currentManga = (Manga) intent.getSerializableExtra("object");
            setTitle(currentManga.getTitle());
            Bundle data = new Bundle();
            data.putInt("recordID", currentManga.getId());
            new NetworkTask(TaskJob.GETDETAILS, MALApi.ListType.MANGA, this, data, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, User.username);


            final DetailsActivity h = this;
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Bundle data = new Bundle();
                    data.putInt("recordID", currentManga.getId());
                    new NetworkTask(TaskJob.GETDETAILS, MALApi.ListType.MANGA, h, data, h).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, User.username);

                }
            });


            if (currentManga.getStatus() == null) {
                isNew = true;
            } else {
                isNew = false;
            }
            Toast.makeText(this, currentManga.getTitle(), Toast.LENGTH_SHORT).show();
            nameLabel = (TextView) findViewById(R.id.nameLabel);
            nameLabel.setText(currentManga.getTitle());
            image = (ImageView) findViewById(R.id.logo);
            Picasso.with(this)
                    .load(currentManga.getImageUrl())
                    .error(R.drawable.ic_naruto)
                    .into(image);


        }

    }

    public void deleteButtonClicked(View view) {
        swipeContainer.setRefreshing(true);
        if (currentType.equals("anime")) {
            currentAnime.setDeleteFlag();
            new UpdateTask(MALApi.ListType.ANIME, this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, currentAnime);
            Bundle data = new Bundle();
            data.putInt("recordID", currentAnime.getId());
            new NetworkTask(TaskJob.GETDETAILS, MALApi.ListType.ANIME, this, data, this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, User.username);

        } else {
            currentManga.setDeleteFlag();
            new UpdateTask(MALApi.ListType.MANGA, this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, currentManga);
            Bundle data = new Bundle();
            data.putInt("recordID", currentManga.getId());
            new NetworkTask(TaskJob.GETDETAILS, MALApi.ListType.MANGA, this, data, this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, User.username);

        }
    }

    public void updateButtonClicked(View view) {
        swipeContainer.setRefreshing(true);
        if (currentType.equals("anime")) {
            EditText epsWatched = (EditText) findViewById(R.id.epsSeen);
            Spinner yourScore = (Spinner) findViewById(R.id.yourScore);
            Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpiner);
            currentAnime.setWatchedEpisodes(Integer.parseInt(epsWatched.getText().toString()));
            currentAnime.setScore(Integer.parseInt(yourScore.getSelectedItem().toString()));
            currentAnime.setWatchedStatus(statusSpinner.getSelectedItem().toString());
            new UpdateTask(MALApi.ListType.ANIME, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentAnime);
            onNetworkTaskFinished(currentAnime, TaskJob.GETDETAILS, MALApi.ListType.ANIME);
        } else {
            EditText epsWatched = (EditText) findViewById(R.id.epsSeen);
            Spinner yourScore = (Spinner) findViewById(R.id.yourScore);
            Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpiner);
            currentManga.setChaptersRead(Integer.parseInt(epsWatched.getText().toString()));
            currentManga.setScore(Integer.parseInt(yourScore.getSelectedItem().toString()));
            currentManga.setReadStatus(statusSpinner.getSelectedItem().toString());
            new UpdateTask(MALApi.ListType.MANGA, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentManga);
            onNetworkTaskFinished(currentManga, TaskJob.GETDETAILS, MALApi.ListType.ANIME);
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

            avaregePoint = (TextView) findViewById(R.id.avaragePoint);
            avaregePoint.setText(currentAnime.getAverageScore() + " / 10");

            avarageCount = (TextView) findViewById(R.id.avarageCount);
            avarageCount.setText(currentAnime.getAverageScoreCount());

            typeOf = (TextView) findViewById(R.id.typeLabel);
            typeOf.setText(currentAnime.getType());

            String openingMusic = "";
            for (String items : currentAnime.getOpeningTheme()) {
                openingMusic += (items + "\n");
            }
            openingTheme = (TextView) findViewById(R.id.openingLabel);
            openingTheme.setText(openingMusic);

            statusLabel = (TextView) findViewById(R.id.statusLabel);
            statusLabel.setText(currentAnime.getStatus());

            Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpiner);


            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.animeStatus, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            statusSpinner.setAdapter(adapter);
            EditText epsWatched = (EditText) findViewById(R.id.epsSeen);
            epsWatched.setText("0");
            TextView epsTotal = (TextView) findViewById(R.id.totalEpisode);
            epsTotal.setText("/" + Integer.toString(currentAnime.getEpisodes()));
            Spinner yourScore = (Spinner) findViewById(R.id.yourScore);
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                    R.array.scores, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            yourScore.setAdapter(adapter1);
            if (currentAnime.getWatchedStatus() != null) {


                statusSpinner.setSelection(adapter.getPosition(currentAnime.getUserStatusString(this)));


                epsWatched.setText(Integer.toString(currentAnime.getWatchedEpisodes()));


                yourScore.setSelection(adapter1.getPosition(Integer.toString(currentAnime.getScore())));

                Button updateDetails = (Button) findViewById(R.id.updateDetails);
                updateDetails.setText("Update");

                Button deleteCurrent = (Button) findViewById(R.id.deleteBtn);
                deleteCurrent.setVisibility(View.VISIBLE);
            } else {
                currentAnime.setCreateFlag();
                Button updateDetails = (Button) findViewById(R.id.updateDetails);
                updateDetails.setText("Add");
                Button deleteCurrent = (Button) findViewById(R.id.deleteBtn);
                deleteCurrent.setVisibility(View.GONE);

            }


        } else {
            currentManga = (Manga) result;
            Synopsis = (TextView) findViewById(R.id.synopsis);
            Synopsis.setText(Html.fromHtml(currentManga.getSynopsisString()));

            episodes = (TextView) findViewById(R.id.episodes);
            episodes.setText(Integer.toString(currentManga.getChapters()));

            rankPoint = (TextView) findViewById(R.id.rankPoint);
            rankPoint.setText(Integer.toString(currentManga.getRank()));

            avaregePoint = (TextView) findViewById(R.id.avaragePoint);
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

            statusLabel = (TextView) findViewById(R.id.statusLabel);
            statusLabel.setText(currentManga.getStatus());


            Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpiner);


            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.mangaStatus, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            statusSpinner.setAdapter(adapter);
            EditText epsWatched = (EditText) findViewById(R.id.epsSeen);
            epsWatched.setText("0");
            TextView epsTotal = (TextView) findViewById(R.id.totalEpisode);
            epsTotal.setText("/" + Integer.toString(currentManga.getChapters()));
            Spinner yourScore = (Spinner) findViewById(R.id.yourScore);
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                    R.array.scores, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            yourScore.setAdapter(adapter1);
            if (currentManga.getReadStatus() != null) {
                statusSpinner.setSelection(adapter.getPosition(currentManga.getUserStatusString(this)));

                epsWatched.setText(Integer.toString(currentManga.getChaptersRead()));

                yourScore.setSelection(adapter1.getPosition(Integer.toString(currentManga.getScore())));

                Button updateDetails = (Button) findViewById(R.id.updateDetails);
                updateDetails.setText("Update");

                Button deleteCurrent = (Button) findViewById(R.id.deleteBtn);
                deleteCurrent.setVisibility(View.VISIBLE);

            } else {
                currentManga.setCreateFlag();
                Button updateDetails = (Button) findViewById(R.id.updateDetails);
                updateDetails.setText("Add");
                Button deleteCurrent = (Button) findViewById(R.id.deleteBtn);
                deleteCurrent.setVisibility(View.GONE);

            }
        }
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onNetworkTaskError(TaskJob job) {
    }

}
