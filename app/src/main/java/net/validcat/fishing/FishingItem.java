package net.validcat.fishing;

import android.content.Intent;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class FishingItem {
    public static final String LOG_TAG = FishingItem.class.getSimpleName();
    public final static String PLACE = "place";
    public final static String DATE = "date";
    public final static String ID = "id";
    public final static String DISCRIPTION = "discription";
    public final static String PHOTO = "photo";

    long id;
    String place;
    String date; //Date
    //int weather
    String weather;
    String description;
    String catches;
    byte[] cameraPhoto;
    Bitmap myPhoto;

    // constructor bdObject
    public FishingItem(int id, String place, String date, String description, Bitmap myPhoto) {
        this.id = id;
        this.place = place;
        this.date = date;
        this.description = description;
        this.myPhoto = myPhoto;
    }

    // constructor
//    public FishingItem(String place, String date) {
//        // We obtain data in FishingList and initialize the field
//        this.place = place;
//        this.date = date;
//    }

    // constructor
    public FishingItem(Intent intent) {
        // obtain data in intent (come in ListActivity)
        place = intent.getStringExtra(FishingItem.PLACE);
        date = intent.getStringExtra(FishingItem.DATE);
        id = intent.getLongExtra(FishingItem.ID, -1L);
        description = intent.getStringExtra(FishingItem.DISCRIPTION);
        myPhoto = (Bitmap)intent.getParcelableExtra(FishingItem.PHOTO);
    }

    public FishingItem() {
        // TODO Auto-generated constructor stub
    }

    public static void packageIntent(Intent data, String place, String date,
                                     long idLong, String description, Bitmap myPhoto) {
        // Save data in intent
        // FishingItem.PLACE - key
        data.putExtra(FishingItem.PLACE, place);
        data.putExtra(FishingItem.DATE, date);
        data.putExtra(FishingItem.ID, idLong);
        data.putExtra(FishingItem.DISCRIPTION, description);
        data.putExtra(FishingItem.PHOTO, myPhoto);
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

    public byte[] getPhoto(){
//        try {
//            if (cameraPhoto != null) {
//                decompress(cameraPhoto);
//            }else {
//                Log.d(LOG_TAG,"getPhoto =" +cameraPhoto);
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch (DataFormatException e){
//            e.printStackTrace();
//        }
        return cameraPhoto;}

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

    public void setPhoto(byte[] photo) {
//        try {
//            compress(photo);
//            Log.d(LOG_TAG, "setPhoto = " + photo);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
        cameraPhoto = photo;}

    public static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
       // deflater.end();

        return output;
    }

    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
       // inflater.end();

        return output;
    }

}
