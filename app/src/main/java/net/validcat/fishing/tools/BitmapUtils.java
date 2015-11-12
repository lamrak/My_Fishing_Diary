package net.validcat.fishing.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Denis on 10.11.2015.
 */
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
}
