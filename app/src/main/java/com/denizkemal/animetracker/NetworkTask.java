package com.denizkemal.animetracker;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import com.denizkemal.animetracker.R;
import com.denizkemal.animetracker.api.APIHelper;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga;
import com.denizkemal.animetracker.api.MALApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NetworkTask extends AsyncTask<String, Void, Object> {
    private TaskJob job;
    private MALApi.ListType type;
    private Activity activity = null;
    private Context context;
    private Bundle data;
    private NetworkTaskListener callback;
    private Object taskResult;
    private HashMap<String, String> Qdata;
    private final TaskJob[] arrayTasks = {TaskJob.GETLIST, TaskJob.FORCESYNC, TaskJob.GETMOSTPOPULAR, TaskJob.GETTOPRATED,
            TaskJob.GETJUSTADDED, TaskJob.GETUPCOMING, TaskJob.SEARCH, TaskJob.REVIEWS};


    public NetworkTask(TaskJob job, MALApi.ListType type, Activity activity, Bundle data, NetworkTaskListener callback) {
        if (job == null || type == null || activity == null)
            throw new IllegalArgumentException("job, type and context must not be null");
        this.job = job;
        this.type = type;
        this.activity = activity;
        this.data = data;
        this.callback = callback;
    }

    public NetworkTask(Activity activity, MALApi.ListType type, HashMap<String, String> Qdata, NetworkTaskListener callback) {
        this.job = TaskJob.BROWSE;
        this.type = type;
        this.activity = activity;
        this.Qdata = Qdata;
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

        if (!isNetworkAvailable && !job.equals(TaskJob.GETLIST) && !job.equals(TaskJob.GETDETAILS)) {
            if (activity != null)
                //Theme.Snackbar(activity, R.string.toast_error_noConnectivity);
            return null;
        }

        int page = 1;
        if (data != null && data.containsKey("page"))
            page = data.getInt("page", 1);

        taskResult = null;
        MALApi cManager = new MALApi(User.username,User.pw);

        try {
            switch (job) {
                case BROWSE:
                    taskResult = isAnimeTask() ? cManager.getBrowseAnime(Qdata) : cManager.getBrowseManga(Qdata);
                    break;
                case GETFRIENDLIST:
                    if (params != null)
                        taskResult = isAnimeTask() ? cManager.getAnimeList(params[0]).getAnimeList() : cManager.getMangaList(params[0]).getMangaList();
                    break;
                case GETMOSTPOPULAR:
                    taskResult = isAnimeTask() ? cManager.getMostPopularAnime(page) : cManager.getMostPopularManga(page);
                    break;
                case GETMOSTPOPULARS:
                    taskResult = isAnimeTask() ? cManager.getPopularSeasonAnime(page) : cManager.getPopularSeasonManga(page);
                    break;
                case GETMOSTPOPULARY:
                    taskResult = isAnimeTask() ? cManager.getPopularYearAnime(page) : cManager.getPopularYearManga(page);
                    break;
                case GETTOPRATED:
                    taskResult = isAnimeTask() ? cManager.getTopRatedAnime(page) : cManager.getTopRatedManga(page);
                    break;
                case GETTOPRATEDS:
                    taskResult = isAnimeTask() ? cManager.getTopSeasonAnime(page) : cManager.getTopSeasonManga(page);
                    break;
                case GETTOPRATEDY:
                    taskResult = isAnimeTask() ? cManager.getTopYearAnime(page) : cManager.getTopYearManga(page);
                    break;
                case GETJUSTADDED:
                    taskResult = isAnimeTask() ? cManager.getJustAddedAnime(page) : cManager.getJustAddedManga(page);
                    break;
                case GETUPCOMING:
                    taskResult = isAnimeTask() ? cManager.getUpcomingAnime(page) : cManager.getUpcomingManga(page);
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
                                    record = cManager.getAnime(data.getInt("recordID", -1), 0);


                                    taskResult = record;

                            } else if (record != null) {
                                taskResult = record;
                            } else {
                                //Theme.Snackbar(activity, R.string.toast_error_noConnectivity);
                            }
                        } else {
                            // Get Manga from database
                            Manga record = null;

                            if (isNetworkAvailable) {
                                // Get records from the website
                                if (record == null || record.getImageUrl() == null)
                                    record = cManager.getManga(data.getInt("recordID", -1),0);


                                    taskResult = record;

                            } else if (record != null) {
                                taskResult = record;
                            } else {
                                //Theme.Snackbar(activity, R.string.toast_error_noConnectivity);
                            }
                        }
                    break;
                case SEARCH:
                    if (params != null)
                        taskResult = isAnimeTask() ? cManager.searchAnime(params[0], page) : cManager.searchManga(params[0], page);
                    break;
                case REVIEWS:
                    if (params != null)
                        taskResult = isAnimeTask() ? cManager.getAnimeReviews(Integer.parseInt(params[0]), page) : cManager.getMangaReviews(Integer.parseInt(params[0]), page);
                    break;
                case RECOMMENDATION:
                    if (params != null)
                        taskResult = isAnimeTask() ? cManager.getAnimeRecs(Integer.parseInt(params[0])) : cManager.getMangaRecs(Integer.parseInt(params[0]));
                    break;
                default:
                    //AppLog.log(Log.ERROR, "Atarashii", "NetworkTask.doInBackground(): " + String.format("%s-task invalid job identifier %s", type.toString(), job.name()));
            }
            /* if result is still null at this point there was no error but the API returned an empty result
             * (e. g. an empty anime-/mangalist), so create an empty list to let the callback know that
             * there was no error
             */
            if (taskResult == null)
                return isArrayList() ? new ArrayList<>() : null;
        } catch (Exception e) {
            //AppLog.logTaskCrash("NetworkTask", "doInBackground(): " + String.format("%s-task error on job %s", type.toString(), job.name()), e);
            return isArrayList() && !job.equals(TaskJob.FORCESYNC) && !job.equals(TaskJob.GETLIST) ? new ArrayList<>() : null;
        }
        return taskResult;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (callback != null) {
            if (result != null)
                callback.onNetworkTaskFinished(taskResult, job, type);
            else
                callback.onNetworkTaskError(job);
        }
    }

    public interface NetworkTaskListener {
        void onNetworkTaskFinished(Object result, TaskJob job, MALApi.ListType type);

        void onNetworkTaskError(TaskJob job);
    }
}
