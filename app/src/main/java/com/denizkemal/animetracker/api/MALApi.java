package com.denizkemal.animetracker.api;

import android.app.Activity;

import com.denizkemal.animetracker.User;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Anime;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.Manga;
import com.denizkemal.animetracker.api.BaseModels.AnimeManga.UserList;
import com.denizkemal.animetracker.api.BaseModels.Profile;
import com.denizkemal.animetracker.api.MALModels.AnimeManga.AnimeList;
import com.denizkemal.animetracker.api.MALModels.AnimeManga.MangaList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Credentials;
import retrofit2.Response;

public class MALApi {
    // Use version 2.1 of the API interface
    //https://young-peak-44942.herokuapp.com/2.1/
    //https://malapi.atarashiiapp.com/2.1/
    private static final String API_HOST = "https://young-peak-44942.herokuapp.com/2.1/";
    private static final String MAL_HOST = "https://myanimelist.net/";
    private Activity activity = null;

    private MALInterface APIservice;
    private MALInterface MALservice;

    public MALApi() {
        setupRESTService(User.username, User.pw);
    }

    public MALApi(Activity activity) {
        this.activity = activity;
        setupRESTService(User.username, User.pw);
    }


    /*
     * Only use for verifying.
     */
    public MALApi(String username, String password) {
        setupRESTService(username, password);
    }

    public static String getListTypeString(ListType type) {
        return type.name().toLowerCase();
    }

    private void setupRESTService(String username, String password) {
        APIservice = APIHelper.createClient(API_HOST, MALInterface.class, Credentials.basic(username, password));
        MALservice = APIHelper.createClient(MAL_HOST, MALInterface.class, Credentials.basic(username, password));
    }

    public boolean isAuth() {
        return APIHelper.isOK(MALservice.verifyAuthentication(), "isAuth");
    }

    public ArrayList<Anime> searchAnime(String query, int page) {

        HashMap<String, String> map = new HashMap<>();
        map.put("keyword", query);
        map.put("page", String.valueOf(page));
        map.put("genre_type", "1");
        map.put("genres", "Hentai");
        return getBrowseAnime(map);

    }

    public ArrayList<Manga> searchManga(String query, int page) {

        HashMap<String, String> map = new HashMap<>();
        map.put("keyword", query);
        map.put("page", String.valueOf(page));
        map.put("genre_type", "1");
        map.put("genres", "Hentai");
        return getBrowseManga(map);

    }

    public UserList getAnimeList(String username) {
        Response<AnimeList> response = null;
        try {
            response = APIservice.getAnimeList(username).execute();
            return AnimeList.createBaseModel(response.body());
        } catch (Exception e) {
            APIHelper.logE(activity, response, "MALApi", "getAnimeList", e);
            return null;
        }
    }

    public UserList getMangaList(String username) {
        Response<MangaList> response = null;
        try {
            response = APIservice.getMangaList(username).execute();
            return MangaList.createBaseModel(response.body());
        } catch (Exception e) {
            APIHelper.logE(activity, response, "MALApi", "getMangaList", e);
            return null;
        }
    }

    public Anime getAnime(int id, int mine) {
        Response<com.denizkemal.animetracker.api.MALModels.AnimeManga.Anime> response = null;
        try {
            response = APIservice.getAnime(id, mine).execute();
            return response.body().createBaseModel();
        } catch (Exception e) {
            APIHelper.logE(activity, response, "MALApi", "getAnime", e);
            return null;
        }
    }

    public Manga getManga(int id, int mine) {
        Response<com.denizkemal.animetracker.api.MALModels.AnimeManga.Manga> response = null;
        try {
            response = APIservice.getManga(id, mine).execute();
            return response.body().createBaseModel();
        } catch (Exception e) {
            APIHelper.logE(activity, response, "MALApi", "getManga", e);
            return null;
        }
    }

    public boolean addOrUpdateAnime(Anime anime) {
        if (anime.getCreateFlag())
            return APIHelper.isOK(APIservice.addAnime(anime.getId(), anime.getWatchedStatus(), anime.getWatchedEpisodes(), anime.getScore()), "addOrUpdateAnime");
        else {
            if (anime.isDirty()) {
                // map anime property names to api field names
                HashMap<String, String> nameMap = new HashMap<>();
                nameMap.put("watchedStatus", "status");
                nameMap.put("watchedEpisodes", "episodes");
                nameMap.put("score", "score");
                nameMap.put("watchingStart", "start");
                nameMap.put("watchingEnd", "end");
                nameMap.put("priority", "priority");
                nameMap.put("personalTags", "tags");
                nameMap.put("notes", "comments");
                nameMap.put("fansubGroup", "fansubber");
                nameMap.put("storage", "storage_type");
                nameMap.put("storageValue", "storage_amt");
                nameMap.put("epsDownloaded", "downloaded_eps");
                nameMap.put("rewatchCount", "rewatch_count");
                nameMap.put("rewatchValue", "rewatch_value");
                HashMap<String, String> fieldMap = new HashMap<>();
                for (String dirtyField : anime.getDirty()) {
                    if (nameMap.containsKey(dirtyField)) {
                        if (anime.getPropertyType(dirtyField) == String.class) {
                            fieldMap.put(nameMap.get(dirtyField), anime.getStringPropertyValue(dirtyField));
                        } else if (anime.getPropertyType(dirtyField) == int.class) {
                            fieldMap.put(nameMap.get(dirtyField), anime.getIntegerPropertyValue(dirtyField).toString());
                        } else if (anime.getPropertyType(dirtyField) == ArrayList.class) {
                            fieldMap.put(nameMap.get(dirtyField), anime.getArrayPropertyValue(dirtyField));
                        }
                    }
                }
                return APIHelper.isOK(APIservice.updateAnime(anime.getId(), fieldMap), "addOrUpdateAnime");
            }
        }
        return false;
    }

    public boolean addOrUpdateManga(Manga manga) {
        if (manga.getCreateFlag())
            return APIHelper.isOK(APIservice.addManga(manga.getId(), manga.getReadStatus(), manga.getChaptersRead(), manga.getVolumesRead(), manga.getScore()), "addOrUpdateManga");
        else {
            if (manga.isDirty()) {
                // map manga property names to api field names
                HashMap<String, String> nameMap = new HashMap<>();
                nameMap.put("readStatus", "status");
                nameMap.put("chaptersRead", "chapters");
                nameMap.put("volumesRead", "volumes");
                nameMap.put("score", "score");
                nameMap.put("readingStart", "start");
                nameMap.put("readingEnd", "end");
                nameMap.put("priority", "priority");
                nameMap.put("personalTags", "tags");
                nameMap.put("rereadValue", "reread_value");
                nameMap.put("rereadCount", "reread_count");
                nameMap.put("notes", "comments");
                HashMap<String, String> fieldMap = new HashMap<>();
                for (String dirtyField : manga.getDirty()) {
                    if (nameMap.containsKey(dirtyField)) {
                        if (manga.getPropertyType(dirtyField) == String.class) {
                            fieldMap.put(nameMap.get(dirtyField), manga.getStringPropertyValue(dirtyField));
                        } else if (manga.getPropertyType(dirtyField) == int.class) {
                            fieldMap.put(nameMap.get(dirtyField), manga.getIntegerPropertyValue(dirtyField).toString());
                        } else if (manga.getPropertyType(dirtyField) == ArrayList.class) {
                            fieldMap.put(nameMap.get(dirtyField), manga.getArrayPropertyValue(dirtyField));
                        }
                    }
                }
                return APIHelper.isOK(APIservice.updateManga(manga.getId(), fieldMap), "addOrUpdateManga");
            }
        }
        return false;
    }

    public boolean deleteAnimeFromList(int id) {
        return APIHelper.isOK(APIservice.deleteAnime(id), "deleteAnimeFromList");
    }

    public boolean deleteMangaFromList(int id) {
        return APIHelper.isOK(APIservice.deleteManga(id), "deleteMangaFromList");
    }

    public ArrayList<Anime> getBrowseAnime(Map<String, String> queries) {
        retrofit2.Response<ArrayList<com.denizkemal.animetracker.api.MALModels.AnimeManga.Anime>> response = null;
        try {
            response = APIservice.getBrowseAnime(queries).execute();
            return AnimeList.convertBaseArray(response.body());
        } catch (Exception e) {
            APIHelper.logE(activity, response, "MALApi", "getBrowseAnime: " + queries.toString(), e);
            return null;
        }
    }

    public ArrayList<Manga> getBrowseManga(Map<String, String> queries) {
        retrofit2.Response<ArrayList<com.denizkemal.animetracker.api.MALModels.AnimeManga.Manga>> response = null;
        try {
            response = APIservice.getBrowseManga(queries).execute();
            return MangaList.convertBaseArray(response.body());
        } catch (Exception e) {
            APIHelper.logE(activity, response, "MALApi", "getBrowseManga: " + queries.toString(), e);
            return null;
        }
    }

    public Profile getProfile(String user) {
        Response<com.denizkemal.animetracker.api.MALModels.Profile> response = null;
        try {
            response = APIservice.getProfile(user).execute();
            return response.body().createBaseModel();
        } catch (Exception e) {
            APIHelper.logE(activity, response, "MALApi", "getProfile", e);
            return null;
        }
    }

    public enum ListType {
        ANIME,
        MANGA
    }
}
