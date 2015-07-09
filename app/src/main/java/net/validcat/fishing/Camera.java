package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;

/**
 * Created by Denis on 07.07.2015.
 *
 * <code>CameraManager</code> works with default device camera. Call method
 * <code>startCameraForResult(Activity)</code> and when Camera call <code>Activity</code>'s method
 * <code>onActivityResult</code> - pass result through the method
 * <code>extractPhotoBitmapFromResult()</code>. If this method returns <code>null</code>,
 * <code>onActivityResult</code>  was invoked not from camera. In other case you will receive
 * <code>Bitmap</code>.
 *
 */
public class Camera { //TODO CameraManager is class name extends CameraManager
    public static final String LOG_TAG = "myLogs";
    private static final String FOLDER_NAME = "MyFishing";
    private static final String KEY_DATA = "data";
    private static final String EXCTANTION_JPG = ".jpg";
    private final int REQUEST_CODE_FOTO = 1;
    private File directory;
    private Uri mUri;
    //TODO this field not needed

    public void startCameraForResult(Activity activity) { //TODO rename startCameraForResult(Activity)
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            createDirectoryFromCard();
//				 else {
//					createDirectoryFromPhone();
//				}
        mUri = generateFileUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(KEY_DATA, mUri);
        activity.startActivityForResult(intent, REQUEST_CODE_FOTO);
    }

    //TODO rename method extractPhotoBitmapFromResult()
    protected Bitmap extractPhotoBitmapFromResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_FOTO) {
            if (intent == null)
                Log.d(LOG_TAG, "Intent is null");
            Bitmap foto = (Bitmap) intent.getExtras().get(KEY_DATA);
            return foto;
        } else if (resultCode == 1) { //TODO RESULT_CANCELED get from activity
            Log.d(LOG_TAG, "Canceled");
            return null;
        }

        //TODO method returns Bitmap ot null (in case if this is not REQUEST_CODE_FOTO)
        return null;
    }

    private Uri generateFileUri() {
        if (directory == null) createDirectoryFromCard();
        return Uri.fromFile(
                new File(directory.getPath() + "/" + "photo_"
                        + System.currentTimeMillis() + EXCTANTION_JPG));
    }

    private void createDirectoryFromCard() {
        directory = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME);
        if (!directory.exists())
            directory.mkdirs();
    }

}
