package com.denizkemal.animetracker.api.BaseModels;

import android.database.Cursor;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

public class Profile implements Serializable {
    /**
     * List of developers.
     */
    private static final String[] developersMAL = {"ratan12", "motoko"};

    /**
     * The username of the requested profile.
     */
    @Getter
    @Setter
    private String username;

    /**
     * The profile image of the user
     */
    @Getter
    @Setter
    private String imageUrl;

    /**
     * The names of the custom anime list
     * <p/>
     * Website: AniList
     */
    @Getter
    @Setter
    private ArrayList<String> customAnime;


    /**
     * The names of the custom manga list.
     * <p/>
     * Website: AniList
     */
    @Getter
    @Setter
    private ArrayList<String> customManga;


    /**
     * The profile banner.
     * <p/>
     * Website: AniList
     */
    @Getter
    @Setter
    private String imageUrlBanner;

    /**
     * The number of notifications
     * <p/>
     * Website: AniList
     */
    @Getter
    @Setter
    private int notifications;

    /**
     * A MangaStats object containing Stats of the user
     */
    @Getter
    @Setter
    private com.denizkemal.animetracker.api.MALModels.Profile.MangaStats mangaStats;

    /**
     * A MangaStats object containing Stats of the user
     */
    @Getter
    @Setter
    private com.denizkemal.animetracker.api.MALModels.Profile.AnimeStats animeStats;

    /**
     * A ProfileDetails object containing general information on the user
     * <p/>
     * Website: MyAnimeList
     */
    @Getter
    @Setter
    private com.denizkemal.animetracker.api.MALModels.Profile.Details details;

    /**
     * The score type that the users is using for displaying info.
     * <p/>
     * Website: AniList
     */
    @Getter
    @Setter
    private int scoreType;

    /**
     * Text about the users.
     * <p/>
     * Website: AniList
     */
    @Getter
    @Setter
    private String about;

    /**
     * Activity
     */
    @Getter
    @Setter
    ArrayList<History> activity;

    public static boolean isDeveloper(String username) {
        if (username == null)
            return false;
        String[] developers =developersMAL;
        return Arrays.asList(developers).contains(username.toLowerCase(Locale.US));
    }

    public static Profile friendFromCursor(Cursor cursor) {
        List<String> columnNames = Arrays.asList(cursor.getColumnNames());
        Profile profile = new Profile();
        profile.setDetails(new com.denizkemal.animetracker.api.MALModels.Profile.Details());

        profile.setUsername(cursor.getString(columnNames.indexOf("username")));
        profile.setImageUrl(cursor.getString(columnNames.indexOf("imageUrl")));
        profile.getDetails().setLastOnline(cursor.getString(columnNames.indexOf("lastOnline")));
        return profile;
    }

    public static Profile fromCursor(Cursor cursor) {
        List<String> columnNames = Arrays.asList(cursor.getColumnNames());
        Profile profile = new Profile();
        profile.setDetails(new com.denizkemal.animetracker.api.MALModels.Profile.Details());
        profile.setAnimeStats(new com.denizkemal.animetracker.api.MALModels.Profile.AnimeStats());
        profile.setMangaStats(new com.denizkemal.animetracker.api.MALModels.Profile.MangaStats());

        profile.setUsername(cursor.getString(columnNames.indexOf("username")));
        profile.setImageUrl(cursor.getString(columnNames.indexOf("imageUrl")));
        profile.setImageUrlBanner(cursor.getString(columnNames.indexOf("imageUrlBanner")));
        profile.setNotifications(cursor.getInt(columnNames.indexOf("notifications")));
        profile.setAbout(cursor.getString(columnNames.indexOf("about")));
        profile.getDetails().setLastOnline(cursor.getString(columnNames.indexOf("lastOnline")));
        profile.getDetails().setStatus(cursor.getString(columnNames.indexOf("status")));
        profile.getDetails().setGender(cursor.getString(columnNames.indexOf("gender")));
        profile.getDetails().setBirthday(cursor.getString(columnNames.indexOf("birthday")));
        profile.getDetails().setLocation(cursor.getString(columnNames.indexOf("location")));
        profile.getDetails().setWebsite(cursor.getString(columnNames.indexOf("website")));
        profile.getDetails().setJoinDate(cursor.getString(columnNames.indexOf("joinDate")));
        profile.getDetails().setAccessRank(cursor.getString(columnNames.indexOf("accessRank")));

        profile.getDetails().setAnimeListViews(cursor.getInt(columnNames.indexOf("animeListViews")));
        profile.getDetails().setMangaListViews(cursor.getInt(columnNames.indexOf("mangaListViews")));
        profile.getDetails().setForumPosts(cursor.getInt(columnNames.indexOf("forumPosts")));
        profile.getDetails().setComments(cursor.getInt(columnNames.indexOf("comments")));

        profile.getAnimeStats().setTimeDays(cursor.getDouble(columnNames.indexOf("AnimetimeDays")));
        profile.getAnimeStats().setWatching(cursor.getInt(columnNames.indexOf("Animewatching")));
        profile.getAnimeStats().setCompleted(cursor.getInt(columnNames.indexOf("Animecompleted")));
        profile.getAnimeStats().setOnHold(cursor.getInt(columnNames.indexOf("AnimeonHold")));
        profile.getAnimeStats().setDropped(cursor.getInt(columnNames.indexOf("Animedropped")));
        profile.getAnimeStats().setPlanToWatch(cursor.getInt(columnNames.indexOf("AnimeplanToWatch")));
        profile.getAnimeStats().setTotalEntries(cursor.getInt(columnNames.indexOf("AnimetotalEntries")));

        profile.getMangaStats().setTimeDays(cursor.getDouble(columnNames.indexOf("MangatimeDays")));
        profile.getMangaStats().setReading(cursor.getInt(columnNames.indexOf("Mangareading")));
        profile.getMangaStats().setCompleted(cursor.getInt(columnNames.indexOf("Mangacompleted")));
        profile.getMangaStats().setOnHold(cursor.getInt(columnNames.indexOf("MangaonHold")));
        profile.getMangaStats().setDropped(cursor.getInt(columnNames.indexOf("Mangadropped")));
        profile.getMangaStats().setPlanToRead(cursor.getInt(columnNames.indexOf("MangaplanToRead")));
        profile.getMangaStats().setTotalEntries(cursor.getInt(columnNames.indexOf("MangatotalEntries")));

        return profile;
    }
}
