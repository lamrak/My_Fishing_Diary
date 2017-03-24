package net.validcat.fishing.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Fishing {

    public int starCount = 0;
    public String uid;
    public String author;
    public String place;
    public long date;
    public String weather;
    public String details;
    public String price;
    public String bait;
    public String fishFeed;
    public String temperature;
    public int weatherIcon;
    public String userAvatarUrl;
    public String photoUrl;

    public Map<String, Boolean> stars = new HashMap<>();

    public Fishing() {}

    public Fishing(String place, long date, String details, String price,
                   String bait, String fishFeed, String temperature, int weatherIcon,
                   String userAvatarUrl) {
        this.place = place;
        this.date = date;
        this.details = details;
        this.price = price;
        this.bait = bait;
        this.fishFeed = fishFeed;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
        this.userAvatarUrl = userAvatarUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUid() {
        return uid;
    }

    public String getAuthor() {
        return author;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("date", date);
        result.put("details", details);
        result.put("place", place);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("price", price);
        result.put("bait", bait);
        result.put("fishFeed", fishFeed);
        result.put("temperature", temperature);
        result.put("weatherIcon", weatherIcon);
        result.put("userAvatarUrl", userAvatarUrl);
        result.put("photoUrl", photoUrl);

        return result;
    }
}