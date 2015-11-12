package net.validcat.fishing;

import android.content.Intent;
import android.graphics.Bitmap;

public class FishingItem {
    public final static String PLACE = "place";
    public final static String DATE = "date";
    public final static String ID = "id";
    public final static String DESCRIPTION = "description";
    public final static String PHOTO = "photo";


    long id = -1;
    String place;
    String date; //Date
    //int weather
    String weather;
    String description;
    String catches;
//    byte[] cameraPhoto;
    Bitmap photoBitmap;

    public FishingItem() {}
    // constructor bdObject
    public FishingItem(int id, String place, String date, String description, Bitmap photoBitmap) {
        this.id = id;
        this.place = place;
        this.date = date;
        this.description = description;
        this.photoBitmap = photoBitmap;
    }

    // constructor
    public FishingItem(Intent intent) {
        place = intent.getStringExtra(FishingItem.PLACE);
        date = intent.getStringExtra(FishingItem.DATE);
        id = intent.getLongExtra(FishingItem.ID, -1L);
        description = intent.getStringExtra(FishingItem.DESCRIPTION);
        photoBitmap = intent.getParcelableExtra(FishingItem.PHOTO);
    }

//    public static void packageIntent(Intent data, String place, String date,
//                                     long id, String description, Bitmap myPhoto) {
//        data.putExtra(FishingItem.PLACE, place);
//        data.putExtra(FishingItem.DATE, date);
//        data.putExtra(FishingItem.ID, id);
//        data.putExtra(FishingItem.DESCRIPTION, description);
//        data.putExtra(FishingItem.PHOTO, myPhoto);
//    }

    public static void packageIntentFromItem(Intent data, FishingItem item) {
        data.putExtra(FishingItem.PLACE, item.getPlace());
        data.putExtra(FishingItem.DATE, item.getDate());
        data.putExtra(FishingItem.ID, item.getId());
        data.putExtra(FishingItem.DESCRIPTION, item.getDescription());
//        data.putExtra(FishingItem.PHOTO, item.getPhoto());

    }

    // data which are set in the formation of a list item
    public long getId() {
        return id;
    }
    public String getDate() {
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
        return catches;
    }
//    public byte[] getPhoto(){
//        return cameraPhoto;
//    }
    public Bitmap getBitmap(){
            return photoBitmap;
    }
    public byte[] getNull(){
        byte [] bytes = null;
        return bytes;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(String catches) {
        this.catches = catches;
    }
//    public void setPhoto(byte[] photo) {
//        cameraPhoto = photo;
//    }
    public void setBitmap (Bitmap photo){
        photoBitmap = photo;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Item[id:" + id + "]";
    }
}
