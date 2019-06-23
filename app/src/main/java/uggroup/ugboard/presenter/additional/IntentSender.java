package uggroup.ugboard.presenter.additional;

import android.util.Log;

public class IntentSender {
    public static final String LOG_TAG = "IntentSender";
    public static final int UPLOAD_ARBITRARY_FILES = 0;
    public static final int UPLOAD_PHOTOS_AND_RECOGNIZE = 1;
    public static final int UPLOAD_PHOTOS_AND_MERGE = 2;


    public void onUploadArbitraryFilesClicked() {
        Log.i(LOG_TAG, "onUploadArbitraryFilesClicked call");
    }

    public void onUploadPhotosAndRecognizeClicked() {
        Log.i(LOG_TAG, "onUploadPhotosAndRecognizeClicked call");

    }

    public void onUploadPhotosAndMergeClicked() {
        Log.i(LOG_TAG, "onUploadPhotosAndMergeClicked call");

    }
}
