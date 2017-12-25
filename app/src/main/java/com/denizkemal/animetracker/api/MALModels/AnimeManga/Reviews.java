package com.denizkemal.animetracker.api.MALModels.AnimeManga;

import com.denizkemal.animetracker.api.BaseModels.Profile;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Reviews implements Serializable {

    /**
     * The creation date of the review
     */
    @Setter
    @Getter
    private String date;

    /**
     * The rating given by the user
     */
    @Setter
    @Getter
    private int rating;

    /**
     * The username of the review creator
     */
    @Setter
    @Getter
    private String username;

    /**
     * The the number of the max episodes
     */
    @Setter
    @Getter
    private int episodes;

    /**
     * The number of watched episodes of the review creator
     */
    @Setter
    @Getter
    @SerializedName("watched_episodes")
    private int watchedEpisodes;

    /**
     * The number of the max chapters
     */
    @Setter
    @Getter
    private int chapters;

    /**
     * The number of read chapters of the review creator
     */
    @Setter
    @Getter
    @SerializedName("chapters_read")
    private int chaptersRead;

    /**
     * The number of users who marked this review helpful
     */
    @Setter
    @Getter
    private int helpful;

    /**
     * The number of users who voted for helpful & not helpful
     */
    @Setter
    @Getter
    @SerializedName("helpful_total")
    private int helpfulTotal;

    /**
     * The avatar URL of the review creator
     */
    @Setter
    @Getter
    @SerializedName("avatar_url")
    private String avatarUrl;

    /**
     * The review content
     */
    @Setter
    @Getter
    private String review;

    private com.denizkemal.animetracker.api.BaseModels.AnimeManga.Reviews createBaseModel() {
        com.denizkemal.animetracker.api.BaseModels.AnimeManga.Reviews model = new com.denizkemal.animetracker.api.BaseModels.AnimeManga.Reviews();
        model.setDate(getDate());
        model.setRating(getRating());
        model.setReview(getReview());
        model.setEpisodesSeen(getWatchedEpisodes());
        model.setChaptersRead(getChaptersRead());
        Profile profile = new Profile();
        profile.setUsername(getUsername());
        profile.setImageUrl(getAvatarUrl() == null ? "http://cdn.myanimelist.net/images/na_series.gif" : getAvatarUrl());
        model.setUser(profile);
        return model;
    }

    public static ArrayList<com.denizkemal.animetracker.api.BaseModels.AnimeManga.Reviews> convertBaseArray(ArrayList<Reviews> MALArray) {
        ArrayList<com.denizkemal.animetracker.api.BaseModels.AnimeManga.Reviews> base = new ArrayList<>();
        if (MALArray != null) {
            for (Reviews reviews : MALArray) {
                base.add(reviews.createBaseModel());
            }
        }
        return base;
    }
}
