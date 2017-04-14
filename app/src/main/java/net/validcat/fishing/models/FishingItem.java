package net.validcat.fishing.models;

import android.database.Cursor;
import android.text.TextUtils;

import com.google.firebase.database.IgnoreExtraProperties;

import net.validcat.fishing.data.FishingContract;

@IgnoreExtraProperties
public class FishingItem {
    private long id = -1;
    private String place;
    private long date;
    private String weather;
    private String description;
    private String price;
    private String thumb;
//    private String photoList;
    private int weatherIcon;
    private String tackle;
    private int tackleIcon;
    private String bait;
    private String fishFeed;
    private String catches;
    private double latitude;
    private double longitude;
    private String photoPath;
    private int weatherTemp;

    public FishingItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FishingItem(String place, long date, String weather, String description) {
        this.place = place;
        this.date = date;
        this.weather = weather;
        this.description = description;
    }

    // data which are set in the formation of a list item
    public long getId() {
        return id;
    }
    public long getDate() {
        return date;
    }
    public String getPlace() {
        return place;
    }
    public String getWeather() {
        return weather;
    }
    public String getDescription() {
        return description;
    }
    public String getPrice() {
        return price;
    }
//    public String getPhotoList(){
//        return photoList;
//    }
    public int getWeatherIcon(){
        return weatherIcon;
    }
    public void addPhoto(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getThumb() {
        return thumb;
    }

    public String getBait() {
        return bait;
    }

    public String getFishFeed() {
        return fishFeed;
    }

    public String getCatches() {
        return catches;
    }

    public String getTackle() {
        return tackle;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

//    public void setPhotoList(List<String> photoList) {
//        this.photoList = photoList;
//    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setTackle(String tackle) {
        this.tackle = tackle;
    }

    public void setTackleIcon(int tackleIcon) {
        this.tackleIcon = tackleIcon;
    }

    public void setBait(String bait) {
        this.bait = bait;
    }

    public void setFishFeed(String fishFeed) {
        this.fishFeed = fishFeed;
    }

    public void setCatches(String catches) {
        this.catches = catches;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Item[id:" + id + "]";
    }

    public static FishingItem createFishingItemFromCursor(Cursor data) {
        FishingItem item = new FishingItem();
        item.id = data.getLong(FishingContract.FishingEntry.INDEX_ID);
        item.place = data.getString(FishingContract.FishingEntry.INDEX_PLACE);
        item.date = data.getLong(FishingContract.FishingEntry.INDEX_DATE);
        item.weather = data.getString(FishingContract.FishingEntry.INDEX_WEATHER);
        item.description = data.getString(FishingContract.FishingEntry.INDEX_DESCRIPTION);
        item.price = data.getString(FishingContract.FishingEntry.INDEX_PRICE);
        String photoStr = data.getString(FishingContract.FishingEntry.INDEX_IMAGE);
        if (!TextUtils.isEmpty(photoStr))
            item.setPhotoPath(photoStr);
//        if (!TextUtils.isEmpty(photoStr))
//            item.photoList = Arrays.asList(photoStr.split(Constants.SPLIT_IMAGE_PATH_PATTERN));
        item.weatherIcon = data.getInt(FishingContract.FishingEntry.INDEX_WEATHER_ICON);
        item.thumb  = data.getString(FishingContract.FishingEntry.INDEX_THUMB);
        item.tackle  = data.getString(FishingContract.FishingEntry.INDEX_TACKLE_ICON);
        item.bait  = data.getString(FishingContract.FishingEntry.INDEX_BAIT);
        item.fishFeed  = data.getString(FishingContract.FishingEntry.INDEX_FISH_FEED);
        item.catches  = data.getString(FishingContract.FishingEntry.INDEX_CATCH);

        item.latitude = data.getDouble(FishingContract.FishingEntry.INDEX_LATITUDE);
        item.longitude = data.getDouble(FishingContract.FishingEntry.INDEX_LONGITUDE);

        return item;
    }

//    public String convertPhotoArrToString() {
////        if (photoList == null)
////            return "";
////
////        StringBuilder sb = new StringBuilder();
////        for (String n : photoList) {
////            if (sb.length() > 0) sb.append(',');
////            sb.append("'").append(n).append("'");
////        }
//
//        return sb.toString();
//    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getWeatherTemp() {
        return weatherTemp;
    }

    public void setWeatherTemp(int weatherTemp) {
        this.weatherTemp = weatherTemp;
    }
}
