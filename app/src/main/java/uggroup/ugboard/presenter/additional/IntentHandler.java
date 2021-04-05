package uggroup.ugboard.presenter.additional;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import uggroup.ugboard.BuildConfig;
import uggroup.ugboard.models.online_model.OnlineModel;

import static uggroup.ugboard.models.offline_model.OfflineModel.LOCAL_FOLDER_NAME;

public class IntentHandler {

    private static final String LOG_TAG = "IntentHandler";

    private static final int UPLOAD_EXISTING_ARBITRARY = 0;
    private static final int UPLOAD_EXISTING_RECOGNIZE = 1;
    private static final int UPLOAD_EXISTING_MERGEPDF = 2;
    private static final int UPLOAD_EXISTING_RECOGNIZE_MERGEPDF = 3;
    private static final int UPLOAD_CAMERA_ARBITRARY = 4;
    private static final int UPLOAD_CAMERA_RECOGNIZE = 5;
    private static final int UPLOAD_CAMERA_MERGEPDF = 6;
    private static final int UPLOAD_CAMERA_RECOGNIZE_MERGEPDF = 7;

    private Activity activity;
    private OnlineModel onlineModel;

    private Intent selectMultipleFilesIntent;
    private Intent selectMultiplePhotosIntent;

    private Uri photoURI;
    private File photoFile;

    public IntentHandler(Activity activity, OnlineModel onlineModel) {
        this.activity = activity;
        this.onlineModel = onlineModel;

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

    public void onUploadExistingRecognizeMergePDFClicked() {
        Log.i(LOG_TAG, "onUploadExistingRecognizeMergePDFClicked call");

        activity.startActivityForResult(selectMultiplePhotosIntent, UPLOAD_EXISTING_RECOGNIZE_MERGEPDF);
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

    public void onUploadCameraRecognizeMergePDFClicked() {
        Log.i(LOG_TAG, "onUploadCameraRecognizeMergePDFClicked call");

        dispatchTakePictureIntent(UPLOAD_CAMERA_RECOGNIZE_MERGEPDF);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_"
                + (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date())) + "_";
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
            photoFile = null;
            photoURI = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(activity, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".fileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case UPLOAD_CAMERA_ARBITRARY: case UPLOAD_CAMERA_RECOGNIZE: case UPLOAD_CAMERA_MERGEPDF: case UPLOAD_CAMERA_RECOGNIZE_MERGEPDF:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<Uri> list = new ArrayList<>();
                    list.add(photoURI);
                    switch (requestCode){
                        case UPLOAD_CAMERA_ARBITRARY:
                            onlineModel.uploadFiles(list);
                            break;
                        case UPLOAD_CAMERA_RECOGNIZE:
                            onlineModel.uploadPhotosAndRecognize(list);
                            break;
                        case UPLOAD_CAMERA_MERGEPDF:
                            onlineModel.uploadPhotosAndMergePDF(list);
                            break;
                        case UPLOAD_CAMERA_RECOGNIZE_MERGEPDF:
                            onlineModel.uploadPhotosAndRecognizeAndMergePDF(list);
                            break;
                    }
                } else {
                    Toast.makeText(activity, "Upload cancelled", Toast.LENGTH_SHORT).show();
                    // need to delete temp image file
                    photoFile.delete();
                }
                break;
            case UPLOAD_EXISTING_ARBITRARY: case UPLOAD_EXISTING_RECOGNIZE: case UPLOAD_EXISTING_MERGEPDF: case UPLOAD_EXISTING_RECOGNIZE_MERGEPDF:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<Uri> list = new ArrayList<>();
                    ClipData clipData = data.getClipData();
                    if (clipData != null){
                        int count = clipData.getItemCount();
                        for (int i = 0; i < count; i++){
                            list.add(clipData.getItemAt(i).getUri());
                        }
                    } else if (data.getData() != null){
                        list.add(data.getData());
                    }
                    switch (requestCode){
                        case UPLOAD_EXISTING_ARBITRARY:
                            onlineModel.uploadFiles(list);
                            break;
                        case UPLOAD_EXISTING_RECOGNIZE:
                            onlineModel.uploadPhotosAndRecognize(list);
                            break;
                        case UPLOAD_EXISTING_MERGEPDF:
                            onlineModel.uploadPhotosAndMergePDF(list);
                            break;
                        case UPLOAD_EXISTING_RECOGNIZE_MERGEPDF:
                            onlineModel.uploadPhotosAndRecognizeAndMergePDF(list);
                            break;
                    }
                } else {
                    Toast.makeText(activity, "Upload cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
