package net.validcat.fishing.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import net.validcat.fishing.data.Constants;

import java.io.File;
import java.io.IOException;

/**
 * Created by Denis on 07.07.2015.
 */
public class CameraManager {
    public static final String LOG_TAG = CameraManager.class.getSimpleName();
//    private File directory;
    private Uri imageUri;

    public void startCameraForResult(Activity activity) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            imageUri = generateUriWithFileInStorage();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activity.startActivityForResult(intent, Constants.REQUEST_CODE_PHOTO);
        }
    }

    private Uri generateUriWithFileInStorage() {
        File directory = new File(Environment
                .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), Constants.FOLDER_NAME);
        if (!directory.exists())
            directory.mkdirs();

        return Uri.fromFile(new File(directory.getPath() + "/" + "photo_"
                + System.currentTimeMillis() + Constants.EXTENSION_JPG));
    }

    public Bitmap getCameraPhoto(Context context, int requestCode) {
        if (requestCode == Constants.REQUEST_CODE_PHOTO) {
            try {
                return  MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context){
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h = (int) (newHeight*densityMultiplier);
        int w = (int) (h*photo.getWidth()/((double)photo.getHeight()));
        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

}