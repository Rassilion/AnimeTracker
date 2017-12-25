package com.denizkemal.animetracker.api.MALModels;

import com.denizkemal.animetracker.api.BaseModels.Forum;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class ForumMain implements Serializable {

    /**
     * The MyAnimeList category.
     */
    @Setter
    @Getter
    private ArrayList<Forum> MyAnimeList;

    /**
     * The Anime & Manga category.
     */
    @Setter
    @Getter
    @SerializedName("Anime & Manga")
    private ArrayList<Forum> AnimeManga;

    /**
     * The General category.
     */
    @Setter
    @Getter
    private ArrayList<Forum> General;

    /**
     * A general list for multi use.
     */
    @Setter
    @Getter
    private ArrayList<Forum> list;

    /**
     * Amount of pages.
     */
    @Setter
    @Getter
    private int pages;


    public ArrayList<Forum> createBaseModel() {
        ArrayList<Forum> model = new ArrayList<>();
        if (getMyAnimeList() != null)
            model.addAll(getMyAnimeList());
        if (getAnimeManga() != null)
            model.addAll(getAnimeManga());
        if (getGeneral() != null)
            model.addAll(getGeneral());
        if (getList() != null) {
            model.addAll(getList());
            model.get(0).setMaxPages(getPages());
        }
        return model;
    }
}
