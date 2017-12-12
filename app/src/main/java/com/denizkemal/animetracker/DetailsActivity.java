package com.denizkemal.animetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public TextView nameLabel;
    public ImageView image;
    public TextView Synopsis;
    public Anime currentAnime;
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
        currentAnime =  AnimeFragment.animeList.get(a);
        Toast.makeText(this, currentAnime.getTitle(),Toast.LENGTH_SHORT).show();
        nameLabel = (TextView)findViewById(R.id.nameLabel);
        nameLabel.setText(currentAnime.getTitle());
        image = (ImageView)findViewById(R.id.logo);
        Picasso.with(this)
                .load(currentAnime.getImageUrl())
                .error(R.drawable.ic_naruto)
                .into(image);
        Synopsis = (TextView)findViewById(R.id.synopsis);
        Synopsis.setText(currentAnime.getSynopsisString());
    }

}
