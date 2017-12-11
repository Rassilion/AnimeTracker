package com.denizkemal.animetracker.api.MALModels.AnimeManga;

import com.denizkemal.animetracker.api.BaseModels.AnimeManga.UserList;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class MangaList implements Serializable {
    @Setter
    @Getter
    private ArrayList<Manga> manga;

    @Setter
    @Getter
    private Statistics statistics;

    public class Statistics implements Serializable {
        @Setter
        @Getter
        private float days;
    }

    public static UserList createBaseModel(MangaList MALArray) {
        UserList userList = new UserList();
        ArrayList<com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga> MangaList = new ArrayList<>();
        if (MALArray != null)
            MangaList = convertBaseArray(MALArray.getManga());
        userList.setMangaList(MangaList);
        return userList;
    }

    public static ArrayList<com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga> convertBaseArray(ArrayList<Manga> MALArray) {
        ArrayList<com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga> base = new ArrayList<>();
        if (MALArray != null)
            for (Manga manga : MALArray) {
                base.add(manga.createBaseModel());
            }
        return base;
    }
}
