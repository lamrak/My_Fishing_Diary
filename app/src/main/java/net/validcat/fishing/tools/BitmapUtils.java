package net.validcat.fishing.tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.validcat.fishing.data.Constants;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    public static Bitmap convertByteToBitmap(byte[] photo) {
        Bitmap photoBitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);

        return photoBitmap;
    }

    public static byte[] convertBitmapToBiteArray(Bitmap bitmap) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            return bos.toByteArray();
    }

    public static void startGalleryPhotoWithResult(Activity act) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        act.startActivityForResult(photoPickerIntent, Constants.PICK_PHOTO);
    }
}
