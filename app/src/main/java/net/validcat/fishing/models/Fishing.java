package net.validcat.fishing.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Fishing {

    public String uid;
    public String author;
    public String place;
    public long date;
    public String weather;
    public String details;
    public String price;
    public String thumb;
    public String tackle;
    public int tackleIcon;
    public String bait;
    public String fishFeed;
    public String catches;
    public double latitude;
    public double longitude;
    public String photoPath;
    public int weatherIcon;
    public int weatherTemp;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Fishing() {}

    public Fishing(String uid, String author, String place, long date, String details) {
        this.uid = uid;
        this.author = author;
        this.place = place;
        this.date = date;
        this.details = details;
    }

//        public Fishing(String uid, String author, String place, long date, String weather,
//                   String details, String price, String thumb, String tackle,
//                   int tackleIcon, String bait, String fishFeed, String catches,
//                   double latitude, double longitude, String photoPath, int weatherIcon,
//                   int weatherTemp) {
//        this.uid = uid;
//        this.author = author;
//        this.place = place;
//        this.date = date;
//        this.weather = weather;
//        this.details = details;
//        this.price = price;
//        this.thumb = thumb;
//        this.tackle = tackle;
//        this.tackleIcon = tackleIcon;
//        this.bait = bait;
//        this.fishFeed = fishFeed;
//        this.catches = catches;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.photoPath = photoPath;
//        this.weatherIcon = weatherIcon;
//        this.weatherTemp = weatherTemp;
//    }


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

//        result.put("price", price);
//        result.put("weather", weather);
//        result.put("weatherIcon", weatherIcon);
//        result.put("thumb", thumb);
//        result.put("tackle", tackle);
//        result.put("bait", bait);
//        result.put("fishFeed", fishFeed);
//        result.put("catches", catches);
//        result.put("latitude", latitude);
//        result.put("longitude", longitude);



        return result;
    }
}
