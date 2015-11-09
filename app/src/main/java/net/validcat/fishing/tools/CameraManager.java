package net.validcat.fishing.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import net.validcat.fishing.db.Constants;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Denis on 07.07.2015.
 */
public class CameraManager {
    public static final String LOG_TAG = CameraManager.class.getSimpleName();
    private File directory;
    private Uri mUri;
    private Bitmap myPhoto;
    private String path;

    public void startCameraForResult(Activity activity) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            createDirectoryFromCard();
        mUri = generateFileUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.putExtra(Constants.KEY_DATA, mUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        //  Log.d(LOG_TAG, "Uri = " + mUri);
        //  Log.d(LOG_TAG, "Intent ==" + intent);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_PHOTO);
    }

//    public Bitmap extractPhotoBitmapFromResult(Activity activity, int requestCode, int resultCode, Intent intent) {
//        if (resultCode == activity.RESULT_OK) {
//            if (requestCode == Constants.REQUEST_CODE_PHOTO) {
//                if (intent == null) {
//                    Log.d(LOG_TAG, "Intent is null");
//                } else {
//                    return (Bitmap) intent.getExtras().get(Constants.KEY_DATA);
//                    // return (Bitmap) intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
//                }
//            }
//        }
//
//        return null;
//    }

    private Uri generateFileUri() {
        if (directory == null) createDirectoryFromCard();
        return Uri.fromFile(new File(directory.getPath() + "/" + "photo_"
                + System.currentTimeMillis() + Constants.EXTENSION_JPG));
    }

    private void createDirectoryFromCard() {
        directory = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.FOLDER_NAME);
        if (!directory.exists())
            directory.mkdirs();
    }

    public Bitmap getCameraPhoto() {
        InputStream is = null;
        BufferedInputStream bis = null;
        path = mUri.toString();
        try {
            URLConnection conn = new URL(path).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 4096);
            myPhoto = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return myPhoto;
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context){
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h = (int) (newHeight*densityMultiplier);
        int w = (int) (h*photo.getWidth()/((double)photo.getHeight()));
        photo = Bitmap.createScaledBitmap(photo,w,h,true);

        return photo;
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 ,bos);

        return bos.toByteArray();
    }
}