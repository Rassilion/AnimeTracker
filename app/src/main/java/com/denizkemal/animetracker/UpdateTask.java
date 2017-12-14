package com.denizkemal.animetracker;

import android.app.Activity;
import android.os.AsyncTask;

import com.denizkemal.animetracker.api.APIHelper;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.GenericRecord;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga;
import com.denizkemal.animetracker.api.MALApi;
import com.denizkemal.animetracker.api.MALApi.ListType;

public class UpdateTask extends AsyncTask<GenericRecord, Void, Boolean> {
    private ListType type = ListType.ANIME;
    private final Activity activity;

    public UpdateTask(ListType type, Activity activity) {
        this.type = type;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(GenericRecord... gr) {
        boolean error = false;
        boolean isNetworkAvailable = APIHelper.isNetworkAvailable(activity);
        MALApi manager = new MALApi(activity);

        if (isNetworkAvailable)
            error = !manager.isAuth();

        try {
            // Sync details if there is network connection
            if (isNetworkAvailable && !error) {
                if (type.equals(ListType.ANIME)) {
                    error = !manager.addOrUpdateAnime((Anime) gr[0]);
                } else {
                    error = !manager.addOrUpdateManga((Manga) gr[0]);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            error = true;
        }

        // Records updated successfully and will be marked as done if it hasn't been removed
        if (isNetworkAvailable && !error && !gr[0].getDeleteFlag()) {
            gr[0].clearDirty();
        }

        if (gr[0].getDeleteFlag()) {
            // Delete record
            if (ListType.ANIME.equals(type)) {
                manager.deleteAnimeFromList(((Anime) gr[0]).getId());
            } else {
                manager.deleteMangaFromList(((Manga) gr[0]).getId());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        System.out.println("1");
    }
}