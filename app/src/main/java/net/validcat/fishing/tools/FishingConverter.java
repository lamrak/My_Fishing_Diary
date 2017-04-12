package net.validcat.fishing.tools;

import net.validcat.fishing.models.Fishing;
import net.validcat.fishing.models.FishingItem;

public class FishingConverter {
    // TODO: 10.04.17 remove this class after sink all data from db to firebase.
    
    public static Fishing convertFishingItemToFishing(FishingItem item) {
        Fishing result = new Fishing();
        result.place = item.getPlace();
        result.date = item.getDate();
        result.details = item.getDescription();
        result.price = item.getPrice();
        result.bait = item.getBait();
        result.fishFeed = item.getFishFeed();
        result.temperature = item.getWeather();
        result.weatherIcon = item.getWeatherIcon();
        result.latitude = item.getLatitude();
        result.longitude = item.getLongitude();
        result.tacklesBag = item.getTackle();
        result.photoUrl = item.getThumb();

        return result;
    }
}
