package uggroup.ugboard.models.online_model;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import uggroup.ugboard.models.online_model.retrofit.IUGDBackend;
import uggroup.ugboard.models.online_model.retrofit.RetrofitClient;
import uggroup.ugboard.presenter.OnlinePresenter;

public class OnlineModelImpl implements OnlineModel {

    private static final int MAX_TIMEOUT_MS = 15000;

    private static final String LOCAL_FOLDER_NAME = "UGBoard";

    private CompositeDisposable compositeDisposable;
    private IUGDBackend mService;

    private DownloadManager downloadManager;

    private OnlinePresenter presenter;

    private HashMap<String, FileItem> itemsMap;
    private HashMap<String, HashSet<String>> kids;

    //private Stack<String> pathStack;
    private String currentItemId; // current item PATH actually, except of rootId = "1"

    private boolean success = false;
    private boolean error = false;
    private String errMsg = "";

    private static final OnlineModelImpl ourInstance = new OnlineModelImpl();
    public static OnlineModelImpl getInstance() { return ourInstance; }

    private OnlineModelImpl() {
        compositeDisposable = new CompositeDisposable();
        mService = RetrofitClient.getInstance().create(IUGDBackend.class);

        itemsMap = new HashMap<>();
        kids = new HashMap<>();

        //pathStack = new Stack<>();

        updateItems();
    }

    @Override
    public void setOnlinePresenter(OnlinePresenter onlinePresenter) {
        presenter = onlinePresenter;
        downloadManager = (DownloadManager) presenter.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public void startExploring() {
        //if (presenter == null) return;
        if (waitForSuccess()) {
            // if success
            presenter.updateContents(getCurrentFiles(), getCurrentPath());
        }
    }

    @Override
    public void goUp() {
        if ("1".equals(currentItemId) || currentItemId == null) return;
        currentItemId = itemsMap.get(currentItemId).getParentId();
        //pathStack.pop();
        presenter.updateContents(getCurrentFiles(), getCurrentPath());
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
                            .setDestinationUri(Uri.parse("file:" + outputFile.getAbsolutePath()))
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
        // TODO
        presenter.showToast("Not yet implemented\n"+
                "GOTTA be deleting file " + filename + " in future!", true);
    }

    @Override
    public void rename(String filename, String newName) {
        // NOT YET IMPLEMENTED
        presenter.showToast("Not yet implemented\n"+
                "GOTTA be renaming file " + filename + " to " + newName + " in future!", true);
    }

    @Override
    public void update() {
        String id = currentItemId;
        updateItems();
        if (waitForSuccess()){
            //pathStack.clear();
            if (itemsMap.get(id) != null) {
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
        }
    }

    @Override
    public void uploadFile(String path) {
        // TODO
    }

    public void onDestroy(){
        compositeDisposable.dispose();
    }

    private void updateItems() {
        success = false;
        error = false;
        errMsg = "";
        compositeDisposable.add(
                mService.getFileList()
                        .subscribeOn(Schedulers.io())
                        //.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(fileItems -> {
                            // onNext (on successful response)
                            if (fileItems == null) {
                                //presenter.showToast("Server response error", true);
                                errMsg = "Server response error";
                                error = true;
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
                                        //presenter.showToast("Server response format error", true);
                                        errMsg = "Server response format error";
                                        error = true;
                                        return;
                                    } else {
                                        kids.put(fi.getId(), new HashSet<>());
                                    }
                                } else {
                                    kids.computeIfAbsent(fi.getParentId(), k -> new HashSet<>());
                                    kids.get(fi.getParentId()).add(fi.getId());
                                    if (fi.getType().equals("dir")) {
                                        kids.computeIfAbsent(fi.getId(), k -> new HashSet<>());
                                    }
                                }
                                itemsMap.put(fi.getId(), fi);
                            }

                            currentItemId = "1";
                            success = true;
                        }, throwable -> {
                            // onError
                            //presenter.showToast(throwable.getMessage(), true);
                            errMsg = throwable.getMessage();
                            error = true;
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
        return currentItemId.substring("data:".length()) + "/";
    }

    private List<FileItem> getCurrentFiles() {
        if (!itemsMap.get(currentItemId).getType().equals("root") &&
                !itemsMap.get(currentItemId).getType().equals("dir") ||
                kids.get(currentItemId) == null){
            presenter.showToast("WTF?", true);
            return null;
        }
        ArrayList<FileItem> ans = new ArrayList<>();
        ans.ensureCapacity(kids.get(currentItemId).size());
        for (String kidId : kids.get(currentItemId) ) {
            ans.add(itemsMap.get(kidId));
        }
        return ans;
    }

    private boolean waitForSuccess() {
        int timeout = 0;
        while (!success && !error){
            if (timeout >= MAX_TIMEOUT_MS){
                presenter.showToast("Max timeout passed", true);
                return false;
            }
            timeout += 10;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                presenter.showToast(e.getMessage(), true);
                return false;
            }
        }
        if (error){
            presenter.showToast(errMsg, true);
            return false;
        }
        // if success
        return true;
    }
}
