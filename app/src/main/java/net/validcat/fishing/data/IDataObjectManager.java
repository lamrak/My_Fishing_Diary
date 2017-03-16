package net.validcat.fishing.data;

import android.content.Context;
import android.net.Uri;

import net.validcat.fishing.camera.CameraManager;
import net.validcat.fishing.models.FishingItem;

/**
 * Created by Oleksii on 12/29/16.
 */

public interface IDataObjectManager {

     void storeNewFishing(Context context, CameraManager cm, FishingItem item,
                                String thumbPath, String photoPath, boolean isThingsListExists,
                                String thingsListReference, boolean isUpdate);

    FishingItem retrieveFishingItem(Context context, Uri uri);
}
