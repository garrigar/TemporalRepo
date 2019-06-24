package uggroup.ugboard.presenter.additional;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.FileProvider;
import uggroup.ugboard.BuildConfig;

import static uggroup.ugboard.models.offline_model.OfflineModel.LOCAL_FOLDER_NAME;

public class IntentSender {

    private static final String LOG_TAG = "IntentSender";

    public static final int UPLOAD_EXISTING_ARBITRARY = 0;
    public static final int UPLOAD_EXISTING_RECOGNIZE = 1;
    public static final int UPLOAD_EXISTING_MERGEPDF = 2;
    public static final int UPLOAD_CAMERA_ARBITRARY = 3;
    public static final int UPLOAD_CAMERA_RECOGNIZE = 4;
    public static final int UPLOAD_CAMERA_MERGEPDF = 5;

    private Activity activity;
    private Intent selectMultipleFilesIntent;
    private Intent selectMultiplePhotosIntent;

    public IntentSender(Activity activity) {
        this.activity = activity;

        selectMultipleFilesIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("*/*")
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        selectMultiplePhotosIntent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    }

    public void onUploadExistingArbitraryClicked(){
        Log.i(LOG_TAG, "onUploadExistingArbitraryClicked call");

        activity.startActivityForResult(selectMultipleFilesIntent, UPLOAD_EXISTING_ARBITRARY);
    }

    public void onUploadExistingRecognizeClicked(){
        Log.i(LOG_TAG, "onUploadExistingRecognizeClicked call");

        activity.startActivityForResult(selectMultiplePhotosIntent, UPLOAD_EXISTING_RECOGNIZE);
    }

    public void onUploadExistingMergePDFClicked(){
        Log.i(LOG_TAG, "onUploadExistingMergePDFClicked call");

        activity.startActivityForResult(selectMultiplePhotosIntent, UPLOAD_EXISTING_MERGEPDF);
    }

    public void onUploadCameraArbitraryClicked(){
        Log.i(LOG_TAG, "onUploadCameraArbitraryClicked call");

        dispatchTakePictureIntent(UPLOAD_CAMERA_ARBITRARY);
    }

    public void onUploadCameraRecognizeClicked(){
        Log.i(LOG_TAG, "onUploadCameraRecognizeClicked call");

        dispatchTakePictureIntent(UPLOAD_CAMERA_RECOGNIZE);
    }

    public void onUploadCameraMergePDFClicked(){
        Log.i(LOG_TAG, "onUploadCameraMergePDFClicked call");

        dispatchTakePictureIntent(UPLOAD_CAMERA_MERGEPDF);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + LOCAL_FOLDER_NAME);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(activity, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".fileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }
}
