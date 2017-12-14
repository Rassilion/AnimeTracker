package com.denizkemal.animetracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class myProfile extends Fragment {


    public myProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView usernameProfile = (TextView) view.findViewById(R.id.userProfileName);
        usernameProfile.setText(User.user.getUsername());
        TextView watchingAnime = (TextView) view.findViewById(R.id.watchingAnime);
        watchingAnime.setText(Integer.toString(User.user.getAnimeStats().getWatching()));
        TextView completed = (TextView) view.findViewById(R.id.CompletedAnime);
        completed.setText(Integer.toString(User.user.getAnimeStats().getCompleted()));
        TextView plantowatch = (TextView) view.findViewById(R.id.PlanToWatchAnime);
        plantowatch.setText(Integer.toString(User.user.getAnimeStats().getPlanToWatch()));
        TextView ondrop = (TextView) view.findViewById(R.id.DroppedAnime);
        ondrop.setText(Integer.toString(User.user.getAnimeStats().getDropped()));
        TextView ondhold = (TextView) view.findViewById(R.id.OnHoldAnime);
        ondhold.setText(Integer.toString(User.user.getAnimeStats().getOnHold()));

        ImageView image = (ImageView)view.findViewById(R.id.profilePhoto);
        Picasso.with(getContext())
                .load(User.user.getImageUrl())
                .transform(new RoundedTransformation(User.username)).fit()
                .error(R.drawable.ic_naruto)
                .into(image);
    }
}
