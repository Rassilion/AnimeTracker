package com.denizkemal.animetracker.api.MALModels;

import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga;
import com.denizkemal.animetracker.api.BaseModels.Profile;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class History implements Serializable {

    /**
     * The anime/manga details.
     */
    @Getter
    @Setter
    private Series item;

    /**
     * The type of record.
     * <p/>
     * It indicates what kind of record the item is.
     * Example: "anime" or "manga"
     */
    @Getter
    @Setter
    private String type;

    /**
     * The time when the anime or manga was updated
     */
    @Getter
    @Setter
    @SerializedName("time_updated")
    private String timeUpdated;

    class Series implements Serializable {

        /**
         * The record of the ID.
         */
        @Getter
        @Setter
        private int id;

        /**
         * Total number of chapters of the manga.
         * <p/>
         * This value is the number of chapters of the manga, or null if unknown.
         */
        @Getter
        @Setter
        @SerializedName("chapters_read")
        private int chaptersRead;

        /**
         * Total number watched of episodes of the anime.
         * <p/>
         * This value is the number of episodes of the anime, or null if unknown.
         */
        @Getter
        @Setter
        @SerializedName("watched_episodes")
        private int watchedEpisodes;

        /**
         * Title of a record.
         **/
        @Getter
        @Setter
        private String title;

        /**
         * The time when the record has been updated.
         **/
        @Getter
        @Setter
        @SerializedName("time_updated")
        private String timeUpdated;
    }

    private com.denizkemal.animetracker.api.BaseModels.History createBaseModel(String username) {
        com.denizkemal.animetracker.api.BaseModels.History model = new com.denizkemal.animetracker.api.BaseModels.History();
        if (type.equals("anime")) {
            model.setAnime(new Anime());
            model.getAnime().setId(getItem().getId());
            model.setValue(String.valueOf(getItem().getWatchedEpisodes()));
            model.setStatus("watched episode");
            model.getAnime().setTitle(getItem().getTitle());
            model.getAnime().setImageUrl("http://i.imgur.com/H6W5lmv.png");
            model.setType("A");
        } else {
            model.setManga(new Manga());
            model.getManga().setId(getItem().getId());
            model.setValue(String.valueOf(getItem().getChaptersRead()));
            model.setStatus("read chapter");
            model.getManga().setTitle(getItem().getTitle());
            model.getManga().setImageUrl("http://i.imgur.com/QwKTy9M.png");
            model.setType("M");
        }
        model.setCreatedAt(getTimeUpdated());
        model.setActivityType("list");

        // set User
        ArrayList<Profile> users = new ArrayList<>();
        Profile user = new Profile();
        user.setUsername(username);
        users.add(user);
        model.setUsers(users);
        return model;
    }

    public static ArrayList<com.denizkemal.animetracker.api.BaseModels.History> convertBaseHistoryList(ArrayList<History> histories, String username) {
        ArrayList<com.denizkemal.animetracker.api.BaseModels.History> historyArrayList = new ArrayList<>();
        for (History history : histories) {
            historyArrayList.add(history.createBaseModel(username));
        }
        return historyArrayList;
    }
}
