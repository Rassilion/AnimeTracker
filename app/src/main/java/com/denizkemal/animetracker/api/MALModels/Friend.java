package com.denizkemal.animetracker.api.MALModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Friend implements Serializable {

    /**
     * The username of the friend.
     */
    @Getter
    @Setter
    private String name;

    /**
     * The date since you accepted him as your friend.
     * <p/>
     * note: sometimes MAL doesn't provide this
     */
    @Getter
    @Setter
    @SerializedName("friend_since")
    private String friendSince;

    /**
     * The profile of the friend.
     */
    @Getter
    @Setter
    private Profile profile;

    private com.denizkemal.animetracker.api.BaseModels.Profile createBaseModel() {
        com.denizkemal.animetracker.api.BaseModels.Profile model = new com.denizkemal.animetracker.api.BaseModels.Profile();
        model.setUsername(getName());
        model.setImageUrl(getProfile().getAvatarUrl());
        model.setDetails(getProfile().getDetails());
        return model;
    }

    public static ArrayList<com.denizkemal.animetracker.api.BaseModels.Profile> convertBaseFriendList(ArrayList<Friend> follows) {
        ArrayList<com.denizkemal.animetracker.api.BaseModels.Profile> profiles = new ArrayList<>();
        for (Friend follower : follows) {
            profiles.add(follower.createBaseModel());
        }
        return profiles;
    }
}
