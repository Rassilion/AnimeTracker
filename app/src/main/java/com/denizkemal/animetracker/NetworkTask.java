package com.denizkemal.animetracker;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.denizkemal.animetracker.api.APIHelper;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga;
import com.denizkemal.animetracker.api.MALApi;

import java.util.ArrayList;
import java.util.Arrays;

public class NetworkTask extends AsyncTask<String, Void, Object> {
    private TaskJob job;
    private MALApi.ListType type;
    private Activity activity = null;
    private Context context;
    private Bundle data;
    private NetworkTaskListener callback;
    private Object taskResult;
    private final TaskJob[] arrayTasks = {TaskJob.GETFRIENDLIST, TaskJob.SEARCH,};


    public NetworkTask(TaskJob job, MALApi.ListType type, Activity activity, Bundle data, NetworkTaskListener callback) {
        if (job == null || type == null || activity == null)
            throw new IllegalArgumentException("job, type and context must not be null");
        this.job = job;
        this.type = type;
        this.activity = activity;
        this.data = data;
        this.callback = callback;
    }

    public NetworkTask(TaskJob job, MALApi.ListType type, Context context, NetworkTaskListener callback) {
        if (type == null || context == null)
            throw new IllegalArgumentException("job, type and context must not be null");
        this.job = job;
        this.type = type;
        this.context = context;
        this.data = new Bundle();
        this.callback = callback;
    }

    public NetworkTask(MALApi.ListType type, Context context, NetworkTaskListener callback) {
        if (type == null || context == null)
            throw new IllegalArgumentException("job, type and context must not be null");
        this.job = TaskJob.GETFRIENDLIST;
        this.type = type;
        this.context = context;
        this.data = new Bundle();
        this.callback = callback;
    }

    private Context getContext() {
        return context != null ? context : activity;
    }

    private boolean isAnimeTask() {
        return type.equals(MALApi.ListType.ANIME);
    }

    private boolean isArrayList() {
        return Arrays.asList(arrayTasks).contains(job);
    }

    @Override
    protected Object doInBackground(String... params) {
        boolean isNetworkAvailable = APIHelper.isNetworkAvailable(getContext());
        if (job == null) {
            return null;
        }

        if (!isNetworkAvailable && !job.equals(TaskJob.GETFRIENDLIST) && !job.equals(TaskJob.GETDETAILS)) {
            if (activity != null)
                Toast.makeText(activity, R.string.toast_error_noConnectivity, Toast.LENGTH_SHORT).show();
            return null;
        }

        int page = 1;
        if (data != null && data.containsKey("page"))
            page = data.getInt("page", 1);

        taskResult = null;
        MALApi cManager;
        if (activity != null) {
            cManager = new MALApi(activity);

        } else {
            cManager = new MALApi();
        }

        try {
            switch (job) {
                case GETPROFILE:
                    taskResult = cManager.getProfile(params[0]);
                    break;
                case GETFRIENDLIST:
                    if (params != null)
                        taskResult = isAnimeTask() ? cManager.getAnimeList(params[0]).getAnimeList() : cManager.getMangaList(params[0]).getMangaList();
                    break;
                case GETDETAILS:
                    if (data != null && data.containsKey("recordID"))
                        if (isAnimeTask()) {
                            // Get Anime from database
                            Anime record = null;

                            if (isNetworkAvailable) {
                                // Get records from the website
                                // Check for synopsis for relation.
                                if (record == null || record.getImageUrl() == null)
                                    record = cManager.getAnime(data.getInt("recordID", -1), 1);


                                taskResult = record;

                            } else if (record != null) {
                                taskResult = record;
                            } else {
                                Toast.makeText(activity, R.string.toast_error_noConnectivity, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Get Manga from database
                            Manga record = null;

                            if (isNetworkAvailable) {
                                // Get records from the website
                                if (record == null || record.getImageUrl() == null)
                                    record = cManager.getManga(data.getInt("recordID", -1), 1);


                                taskResult = record;

                            } else if (record != null) {
                                taskResult = record;
                            } else {
                                Toast.makeText(activity, R.string.toast_error_noConnectivity, Toast.LENGTH_SHORT).show();
                            }
                        }
                    break;
                case SEARCH:
                    if (params != null)
                        taskResult = isAnimeTask() ? cManager.searchAnime(params[0], page) : cManager.searchManga(params[0], page);
                    break;
                default:
                    break;//TODO error
            }
            /* if result is still null at this point there was no error but the API returned an empty result
             */
            if (taskResult == null)
                return isArrayList() ? new ArrayList<>() : null;
        } catch (Exception e) {
            return isArrayList() && !job.equals(TaskJob.GETFRIENDLIST) ? new ArrayList<>() : null;
        }
        return taskResult;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (callback != null) {
            try {
                if (result != null)
                    callback.onNetworkTaskFinished(taskResult, job, type);
                else
                    callback.onNetworkTaskError(job);
            }catch (NullPointerException e){
                System.out.println("Activity dead:"+ e.getMessage());
            }
        }
    }

    public interface NetworkTaskListener {
        void onNetworkTaskFinished(Object result, TaskJob job, MALApi.ListType type);

        void onNetworkTaskError(TaskJob job);
    }
}
