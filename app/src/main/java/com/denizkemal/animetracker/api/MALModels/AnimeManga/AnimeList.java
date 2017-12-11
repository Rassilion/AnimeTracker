package com.denizkemal.animetracker.api.MALModels.AnimeManga;

import com.denizkemal.animetracker.api.BaseModels.AnimeManga.UserList;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class AnimeList implements Serializable {
    @Setter
    @Getter
    private ArrayList<Anime> anime;

    @Setter
    @Getter
    private Statistics statistics;

    public class Statistics implements Serializable {
        @Setter
        @Getter
        private float days;
    }

    public static UserList createBaseModel(AnimeList MALArray) {
        UserList userList = new UserList();
        ArrayList<com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime> animeList = new ArrayList<>();
        if (MALArray != null)
            animeList = convertBaseArray(MALArray.getAnime());
        userList.setAnimeList(animeList);
        return userList;
    }

    public static ArrayList<com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime> convertBaseArray(ArrayList<Anime> MALArray) {
        ArrayList<com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime> base = new ArrayList<>();
        if (MALArray != null)
            for (Anime anime : MALArray) {
                base.add(anime.createBaseModel());
            }
        return base;
    }
}
