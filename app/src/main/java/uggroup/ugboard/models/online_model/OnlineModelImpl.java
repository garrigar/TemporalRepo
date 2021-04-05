package uggroup.ugboard.models.online_model;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uggroup.ugboard.BuildConfig;
import uggroup.ugboard.models.online_model.retrofit.IUGDBackend;
import uggroup.ugboard.models.online_model.retrofit.RetrofitClient;
import uggroup.ugboard.presenter.OnlinePresenter;

import static uggroup.ugboard.models.offline_model.OfflineModel.LOCAL_FOLDER_NAME;

public class OnlineModelImpl implements OnlineModel {

    private static final int MAX_TIMEOUT_MS = 15000;

    private CompositeDisposable compositeDisposable;
    private IUGDBackend mService;

    private DownloadManager downloadManager;

    private OnlinePresenter presenter;

    private HashMap<String, FileItem> itemsMap;
    private HashMap<String, TreeSet<String>> kids;

    //private Stack<String> pathStack;
    private String currentItemId; // current item PATH actually, except of rootId = "1"

    private boolean updateItems_success = false;
    private boolean updateItems_error = false;
    private String updateItems_errMsg = "";

    private final UploadStatusDelegate delegate = new UploadStatusDelegate() {
        @Override
        public void onProgress(Context context, UploadInfo uploadInfo) {}

        @Override
        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
            presenter.showToast("Error during upload", true);
        }

        @Override
        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
            int number = uploadInfo.getSuccessfullyUploadedFiles().size();
            presenter.showToast("Successfully uploaded " + number
                    + " file" + (number == 1 ? "" : "s"), false);
            update();
        }

        @Override
        public void onCancelled(Context context, UploadInfo uploadInfo) {
            presenter.showToast("Upload cancelled", false);
        }
    };

    private boolean INITIALIZED = false;

    private static final OnlineModelImpl ourInstance = new OnlineModelImpl();
    public static OnlineModelImpl getInstance() { return ourInstance; }

    private OnlineModelImpl() {
        compositeDisposable = new CompositeDisposable();
        mService = RetrofitClient.getInstance().create(IUGDBackend.class);

        itemsMap = new HashMap<>();
        kids = new HashMap<>();
        //pathStack = new Stack<>();

        //updateItems();
    }

    @Override
    public void setOnlinePresenter(OnlinePresenter onlinePresenter) {
        presenter = onlinePresenter;
        downloadManager = (DownloadManager) presenter.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public void init() {
        if (INITIALIZED) return;
        INITIALIZED = true;

        if (presenter == null) return;

        presenter.setUpdatingState(true);
        presenter.showSnackbar("Loading, please wait", Snackbar.LENGTH_INDEFINITE);

        init(5);
    }

    private void init(int retries){

        updateItems();
        waitForResultAndAct(() -> {
                    /*if (id != null && itemsMap.get(id) != null) {
                        currentItemId = id;
                    }*/
                    //presenter.updateContents(getCurrentFiles(), getCurrentPath());
                    presenter.setUpdatingState(false);
                    presenter.showSnackbar("Loading completed", Snackbar.LENGTH_SHORT);
                },
                () -> {
                    if (retries > 0) {
                        //presenter.showToast(updateItems_errMsg + "\nRetrying...", false);
                        init(retries - 1);
                    } else {
                        presenter.showToast(updateItems_errMsg, true);
                        presenter.setUpdatingState(false);
                        presenter.showSnackbar("Loading failed, try to refresh manually", Snackbar.LENGTH_INDEFINITE);
                    }
                }
        );
    }

    @Override
    public void startExploring() {
        waitForResultAndAct(() -> presenter.updateContents(getCurrentFiles(), getCurrentPath()),
                () -> presenter.updateContents(new ArrayList<>(), "Error"));
    }

    @Override
    public boolean goUp() {
        if ("1".equals(currentItemId) || currentItemId == null) return false;
        currentItemId = itemsMap.get(currentItemId).getParentId();
        //pathStack.pop();
        presenter.updateContents(getCurrentFiles(), getCurrentPath());
        return true;
    }

    @Override
    public void getAccess(String itemName) {
        String selectedItemId = ("1".equals(currentItemId) ? "disk:" : currentItemId) + "/" + itemName;
        FileItem fileItem = itemsMap.get(selectedItemId);
        if (fileItem.getType().equals("dir")){
            currentItemId = selectedItemId;
            //pathStack.push(fileItem.getName());
            presenter.updateContents(getCurrentFiles(), getCurrentPath());
        } else {
            //presenter.showToast("GOTTA be downloading this file in future", true);

            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Log.d("DownloadMgr", "Memory not available");
                presenter.showToast("Memory not available", true);
                return;
            }

            File workDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + LOCAL_FOLDER_NAME);
            String filename = itemName;
            File outputFile = new File(workDir, filename);
            int k = 0;
            while (outputFile.exists()){
                k++;
                filename = renameWithNumber(itemName, k);
                outputFile = new File(workDir, filename);
            }
            downloadManager.enqueue(
                    new DownloadManager.Request(
                            Uri.parse(IUGDBackend.DOWNLOAD_BASE_URL + selectedItemId)
                    )
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(filename)
                            .setDescription("Downloading")
                            .setMimeType(fileItem.getType())
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationUri(Uri.parse("file://" + outputFile.getAbsolutePath()))
            );
        }
    }

    private String renameWithNumber(String name, int number) {
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1){
            return name + '-' + number;
        }
        return new StringBuffer(name).insert(lastDot, "-" + number).toString();
    }

    @Override
    public void delete(String filename) {
        String selectedItemId = ("1".equals(currentItemId) ? "disk:" : currentItemId) + "/" + filename;
        mService.deleteFileItem(selectedItemId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try (ResponseBody body = response.body()) {
                        if (body != null && Boolean.parseBoolean(body.string())) {
                            presenter.showToast("Deleted \"" + filename + "\" successfully", false);
                            update();
                        } else {
                            presenter.showToast("Delete failed", true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        presenter.showToast("Delete failed", true);
                    }
                } else {
                    presenter.showToast("Delete failed", true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                presenter.showToast("Delete failed\n" + t.getMessage(), true);
            }
        });
    }

    @Override
    public void rename(String filename, String newName) {
        // NOT YET IMPLEMENTED
        presenter.showToast("Not yet implemented\n"+
                "GOTTA be renaming file \"" + filename + "\" to \"" + newName + "\" in future!", true);
    }

    @Override
    public void update() {
        presenter.setUpdatingState(true);
        presenter.showSnackbar("Updating, please wait", Snackbar.LENGTH_INDEFINITE);
        String id = currentItemId;
        updateItems();

        waitForResultAndAct(() -> {
                    //pathStack.clear();
                    if (id != null && itemsMap.get(id) != null) {
                        currentItemId = id;
                    /*FileItem item = itemsMap.get(currentItemId);
                    ArrayList<String> temp = new ArrayList<>();
                    while (item.getId() != 1){
                        temp.add(item.getName());
                        item = itemsMap.get(item.getParentId());
                    }
                    for (int i = temp.size() - 1; i >= 0; i--) {
                        pathStack.push(temp.get(i));
                    }*/
                    }
                    presenter.updateContents(getCurrentFiles(), getCurrentPath());
                    presenter.setUpdatingState(false);
                    presenter.showSnackbar("Update completed", Snackbar.LENGTH_SHORT);
                },
                () -> {
                    presenter.showToast(updateItems_errMsg, true);
                    presenter.setUpdatingState(false);
                    presenter.showSnackbar("Update failed", Snackbar.LENGTH_LONG);
                }
        );
    }

    private void uploadFilesRequest(List<Uri> uriList, String urlModif, String filenamePrefix, boolean enableCounter){
        if (currentItemId == null) {
            presenter.showToast("No upload destination!", true);
            return;
        }
        try {
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(presenter.getContext(),
                    IUGDBackend.UPLOAD_BASE_URL + urlModif);
            String firstName = null;
            int listSize = uriList.size();
            int i = 1;
            for (Uri uri : uriList) {
                if (firstName == null) firstName = getFilenameFromUri(uri);
                multipartUploadRequest.addFileToUpload(uri.toString(), getCurrentPath()
                                + filenamePrefix + (enableCounter ? (i++ + "_") : "")
                                + getFilenameFromUri(uri));
            }
            multipartUploadRequest.setDelegate(delegate)
                    .setNotificationConfig(
                            new UploadNotificationConfig().setTitleForAllStatuses(
                                    listSize == 1 ? firstName : (String.valueOf(listSize) + " files")
                            ).setClearOnActionForAllStatuses(true)
                    )
                    .setMaxRetries(2)
                    .startUpload();
        } catch (MalformedURLException e) {
            presenter.showToast("MalformedURL exception", true);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            presenter.showToast("FileNotFound exception", true);
            e.printStackTrace();
        }
    }

    @Override
    public void uploadFiles(List<Uri> uriList) {
        uploadFilesRequest(uriList, "", "", false);
    }

    @Override
    public void uploadPhotosAndRecognize(List<Uri> uriList) {
        uploadFilesRequest(uriList, "?method=2", "", false);
    }

    @Override
    public void uploadPhotosAndMergePDF(List<Uri> uriList) {
        String timeStamp = getTimeStamp();
        uploadFilesRequest(uriList,
                "?method=1&pdfName=" + getCurrentPath() + "PDF_" + timeStamp + ".pdf",
                "IMG_" + timeStamp + "_", true);
    }

    @Override
    public void uploadPhotosAndRecognizeAndMergePDF(List<Uri> uriList) {
        String timeStamp = getTimeStamp();
        uploadFilesRequest(uriList,
                "?method=3&pdfName=" + getCurrentPath() + "PDF_" + timeStamp + ".pdf",
                "IMG_" + timeStamp + "_", true);
    }

    public void onDestroy(){
        compositeDisposable.dispose();
    }

    private void updateItems() {
        updateItems_success = false;
        updateItems_error = false;
        updateItems_errMsg = "";
        compositeDisposable.add(
                mService.getFileList()
                        .subscribeOn(Schedulers.io())
                        //.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(fileItems -> {
                            // onNext (on successful response)
                            if (fileItems == null) {
                                //presenter.showToast("Server response updateItems_error", true);
                                updateItems_errMsg = "Server response updateItems_error";
                                updateItems_error = true;
                                return;
                            }
                            itemsMap.clear();
                            kids.clear();
                            for (int i = 0; i < fileItems.size(); i++) {
                                FileItem fi = fileItems.get(i);
                                if (fi == null) {
                                    continue;
                                }
                                if (i == 0) {
                                    if (!fi.getType().equals("root")) {
                                        //presenter.showToast("Server response format updateItems_error", true);
                                        updateItems_errMsg = "Server response format updateItems_error";
                                        updateItems_error = true;
                                        return;
                                    } else {
                                        kids.put(fi.getId(), new TreeSet<>());
                                    }
                                } else {
                                    kids.computeIfAbsent(fi.getParentId(), k -> new TreeSet<>());
                                    kids.get(fi.getParentId()).add(fi.getId());
                                    if (fi.getType().equals("dir")) {
                                        kids.computeIfAbsent(fi.getId(), k -> new TreeSet<>());
                                    }
                                }
                                itemsMap.put(fi.getId(), fi);
                            }

                            currentItemId = "1";
                            updateItems_success = true;
                        }, throwable -> {
                            // onError
                            //presenter.showToast(throwable.getMessage(), true);
                            updateItems_errMsg = throwable.getMessage();
                            updateItems_error = true;
                        })
        );
    }

    private String getCurrentPath(){
        /*StringBuilder sb = new StringBuilder("/");
        for (String s: pathStack) {
            sb.append(s).append('/');
        }
        return sb.toString();*/
        if ("1".equals(currentItemId)){
            return "/";
        }
        return currentItemId.substring("disk:".length()) + "/";
    }

    private List<FileItem> getCurrentFiles() {
        if (!itemsMap.get(currentItemId).getType().equals("root") &&
                !itemsMap.get(currentItemId).getType().equals("dir") ||
                kids.get(currentItemId) == null){
            Log.i("OnlineModelImpl", "WTF? getCurrentFiles not from folder???");
            return null;
        }
        ArrayList<FileItem> ans = new ArrayList<>();
        ans.ensureCapacity(kids.get(currentItemId).size());
        for (String kidId : kids.get(currentItemId) ) {
            ans.add(itemsMap.get(kidId));
        }
        return ans;
    }

    private boolean sleepCurrentThreadForUpdateSuccess() {
        int timeout = 0;
        while (!updateItems_success && !updateItems_error){
            if (timeout >= MAX_TIMEOUT_MS){
                updateItems_errMsg = "Max timeout passed";
                return false;
            }
            timeout += 100;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                updateItems_errMsg = e.getMessage();
                return false;
            }
        }
        return !updateItems_error;
    }

    private void waitForResultAndAct(Runnable onSuccess, Runnable onError){
        final int OK = 0;
        final int ERROR = 1;
        Handler onResult = new Handler(msg -> {
            switch (msg.what){
                case OK:
                    onSuccess.run();
                    break;
                case ERROR:
                    onError.run();
                    break;
            }
            return true;
        });
        new Thread(() -> {
            if (sleepCurrentThreadForUpdateSuccess()){
                onResult.sendEmptyMessage(OK);
            } else {
                onResult.sendEmptyMessage(ERROR);
            }
        }).start();
    }

    private String getFilenameFromUri(Uri uri){
        Cursor returnCursor = presenter.getContext().getContentResolver()
                .query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private String getTimeStamp(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }
}
