package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import net.validcat.fishing.db.Constants;

import java.io.File;

/**
 * Created by Denis on 07.07.2015.
 *
 * <code>CameraManager</code> works with default device camera. Call method
 * <code>startCameraForResult(Activity)</code> and when CameraManager call <code>Activity</code>'s method
 * <code>onActivityResult</code> - pass result through the method
 * <code>extractPhotoBitmapFromResult()</code>. If this method returns <code>null</code>,
 * <code>onActivityResult</code>  was invoked not from camera. In other case you will receive
 * <code>Bitmap</code>.
 *
 */
public class CameraManager {
    public static final String LOG_TAG = CameraManager.class.getSimpleName();
    private File directory;
    private Uri mUri;

    public void startCameraForResult(Activity activity) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            createDirectoryFromCard();
//				 else {
//					createDirectoryFromPhone();
//				}
        mUri = generateFileUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(Constants.KEY_DATA, mUri);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_PHOTO);
    }

    protected Bitmap extractPhotoBitmapFromResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Constants.REQUEST_CODE_PHOTO) {
            if (intent == null)
                Log.d(LOG_TAG, "Intent is null");
            return (Bitmap) intent.getExtras().get(Constants.KEY_DATA);
        }

        return null;
    }

    private Uri generateFileUri() {
        if (directory == null) createDirectoryFromCard();
        return Uri.fromFile(
                new File(directory.getPath() + "/" + "photo_"
                        + System.currentTimeMillis() + Constants.EXTENSION_JPG));
    }

    private void createDirectoryFromCard() {
        directory = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.FOLDER_NAME);
        if (!directory.exists())
            directory.mkdirs();
    }

}
