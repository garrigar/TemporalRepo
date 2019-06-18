package uggroup.ugboard.models.online_model;

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uggroup.ugboard.models.online_model.retrofit.IUGDBackend;
import uggroup.ugboard.models.online_model.retrofit.RetrofitClient;
import uggroup.ugboard.presenter.OnlinePresenter;

public class OnlineModelImpl implements OnlineModel {

    private static final int MAX_TIMEOUT_MS = 30000;

    private CompositeDisposable compositeDisposable;
    private IUGDBackend mService;

    private OnlinePresenter presenter;
    private SparseArray<FileItem> itemsMap;
    private SparseArray<HashSet<Integer>> kids;

    private Stack<String> pathStack;
    private int currentItemId;

    private boolean success = false;
    private boolean error = false;
    private String errMsg = "";

    private static final OnlineModelImpl ourInstance = new OnlineModelImpl();
    public static OnlineModelImpl getInstance() { return ourInstance; }

    private OnlineModelImpl() {
        compositeDisposable = new CompositeDisposable();
        mService = RetrofitClient.getInstance().create(IUGDBackend.class);

        itemsMap = new SparseArray<>();
        kids = new SparseArray<>();

        pathStack = new Stack<>();

        updateItems();
    }

    @Override
    public void setOnlinePresenter(OnlinePresenter onlinePresenter) {
        presenter = onlinePresenter;
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
        if (currentItemId == 1) return;
        currentItemId = itemsMap.get(currentItemId).getParentId();
        pathStack.pop();
        presenter.updateContents(getCurrentFiles(), getCurrentPath());
    }

    @Override
    public void getAccess(FileItem fileItem) {
        if (fileItem.getType().equals("root")) {
            Log.d("UGD", "WTF? Trying to go into root?");
        } else if (fileItem.getType().equals("folder")){
            currentItemId = fileItem.getId();
            pathStack.push(fileItem.getName());
            presenter.updateContents(getCurrentFiles(), getCurrentPath());
        } else {
            presenter.showToast("GOTTA be downloading this file in future", true);
        }
    }

    @Override
    public void delete(FileItem fileItem) {
        // TODO
        presenter.showToast("Not yet implemented", true);
    }

    @Override
    public void rename(FileItem fileItem) {
        // NOT YET IMPLEMENTED
        presenter.showToast("Not yet implemented", true);
    }

    @Override
    public void update() {
        int id = currentItemId;
        updateItems();
        if (waitForSuccess()){
            pathStack.clear();
            if (itemsMap.get(id) != null) {
                currentItemId = id;
                FileItem item = itemsMap.get(currentItemId);
                ArrayList<String> temp = new ArrayList<>();
                while (item.getId() != 1){
                    temp.add(item.getName());
                    item = itemsMap.get(item.getParentId());
                }
                for (int i = temp.size() - 1; i >= 0; i--) {
                    pathStack.push(temp.get(i));
                }
            }
            presenter.updateContents(getCurrentFiles(), getCurrentPath());
        }
    }

    public void dispose(){
        compositeDisposable.dispose();
    }

    private void updateItems() {
        compositeDisposable.add(
                mService.getFileList()
                        .subscribeOn(Schedulers.io())
                        //.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<FileItem>>() {
                            @Override
                            public void accept(List<FileItem> fileItems) throws Exception {
                                success = false;
                                error = false;
                                errMsg = "";
                                if (fileItems == null) {
                                    //presenter.showToast("Server response error", true);
                                    errMsg = "Server response error";
                                    error = true;
                                    return;
                                } else {
                                    itemsMap.clear();
                                    kids.clear();
                                    for (int i = 0; i < fileItems.size(); i++) {
                                        FileItem fi = fileItems.get(i);
                                        if (i == 0) {
                                            if (!fi.getType().equals("root")) {
                                                //presenter.showToast("Server response format error", true);
                                                errMsg = "Server response format error";
                                                error = true;
                                                return;
                                            } else {
                                                kids.append(fi.getId(), new HashSet<Integer>());
                                            }
                                        } else {
                                            if (kids.get(fi.getParentId()) == null) {
                                                kids.append(fi.getParentId(), new HashSet<Integer>());
                                            }
                                            kids.get(fi.getParentId()).add(fi.getId());
                                            if (fi.getType().equals("folder") && kids.get(fi.getId()) == null) {
                                                kids.append(fi.getId(), new HashSet<Integer>());
                                            }
                                        }
                                        itemsMap.append(fi.getId(), fi);
                                    }
                                }
                                currentItemId = 1;
                                success = true;
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                // onError
                                //presenter.showToast(throwable.getMessage(), true);
                                errMsg = throwable.getMessage();
                                error = true;
                            }
                        })
        );
    }

    private String getCurrentPath(){
        StringBuilder sb = new StringBuilder("/");
        for (String s: pathStack) {
            sb.append(s).append('/');
        }
        return sb.toString();
    }

    private List<FileItem> getCurrentFiles() {
        if (!itemsMap.get(currentItemId).getType().equals("root") &&
                !itemsMap.get(currentItemId).getType().equals("folder") ||
                kids.get(currentItemId) == null){
            presenter.showToast("WTF?", true);
            return null;
        }
        ArrayList<FileItem> ans = new ArrayList<>();
        ans.ensureCapacity(kids.get(currentItemId).size());
        for (Integer kidId : kids.get(currentItemId) ) {
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
