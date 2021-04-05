package uggroup.ugboard.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import uggroup.ugboard.R;
import uggroup.ugboard.fragments.FileManager;
import uggroup.ugboard.fragments.FileManagerFragment;
import uggroup.ugboard.models.offline_model.OfflineModel;
import uggroup.ugboard.models.offline_model.OfflineModelImpl;
import uggroup.ugboard.models.online_model.FileItem;
import uggroup.ugboard.models.online_model.OnlineModel;
import uggroup.ugboard.models.online_model.OnlineModelImpl;
import uggroup.ugboard.presenter.additional.IntentHandler;

public class MainActivity extends AppCompatActivity implements
        OnlinePresenter, OfflinePresenter, FileManager.FileClickListener, FileManager.OnOptionClickListener,
        FileManager.GetBackListener, FileManager.RefreshRequestListener {


    // Keep the fragment inside though it's the same
    // thing as FileManager. We need it for convenient
    // fragment transactions
    FileManagerFragment fileManagerFragment;
    FileManager fileManager;
    OnlineModel onlineModel;

    OfflineModel offlineModel;

    // For later responses to the online model
    //ArrayList<FileItem> fileList;
    IntentHandler intentHandler;

    private boolean IS_ONLINE_NOT_OFFLINE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        this.onlineModel = OnlineModelImpl.getInstance();
        this.onlineModel.setOnlinePresenter(this);

        this.offlineModel = OfflineModelImpl.getInstance();
        this.offlineModel.setOfflinePresenter(this);

        // Add file manager via fragment transaction
        this.fileManagerFragment = new FileManagerFragment();
        this.fileManager = this.fileManagerFragment;
        this.fileManager.setFileClickListener(this);
        this.fileManager.setOnOptionClickListener(this);
        this.fileManager.setGetBackListener(this);
        this.fileManager.setRefreshRequestListener(this);
        ArrayList<String> options =  new ArrayList<>();
        options.add("Open");
        options.add("Rename");
        options.add("Delete");
        this.fileManager.setOptionsList(options);
        // We attach the file manager first by default.
        attachFileManager();
        setUpNavigationDrawer();

        this.intentHandler = new IntentHandler(this, this.onlineModel);
        //this.onlineModel.init();

        requestPermissions();

    }

    @Override
    protected void onResumeFragments() {
        // We need to call init method here because
        // the onlineModel calls updating the fileList too early,
        // e.c. when the view is not created yet.
        super.onResumeFragments();
        this.onlineModel.init();

        if (IS_ONLINE_NOT_OFFLINE) {
            this.onlineModel.startExploring();
        } else {
            this.offlineModel.startExploring();
        }
    }

    private void attachFileManager() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, this.fileManagerFragment)
                .commit();

    }

    private void detachFileManager() {
        getSupportFragmentManager()
                .beginTransaction()
                .detach(this.fileManagerFragment)
                .commit();
    }

    private void attachSettings() {
        // TODO
    }

    private void detachSettings() {
        // TODO
    }

    private void setUpNavigationDrawer(){

        // Setup account header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Super Hacker")
                                .withEmail("CENSORED@gmail.com")
                                .withIcon(R.drawable.profile),
                        new ProfileDrawerItem()
                                .withName("Der kluge Kopf")
                                .withEmail("myemail@gmail.com")
                                .withIcon(R.drawable.profile_rocket),
                        new ProfileDrawerItem()
                                .withName("King of Rock")
                                .withEmail("showmustgoon@gmail.com")
                                .withIcon(R.drawable.profile_freddie)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        // Setup all the buttons
        new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withDisplayBelowStatusBar(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName(R.string.drawer_item_cloud),
                        new PrimaryDrawerItem()
                                .withIdentifier(2)
                                .withName(R.string.drawer_item_local),
                        new DividerDrawerItem(),
                        new ExpandableDrawerItem()
                                .withName(getString(R.string.drawer_item_upload))
                                .withSubItems(
                                        new ExpandableDrawerItem()
                                                .withName(getString(R.string.drawer_item_upload_existing))
                                                .withSubItems(
                                                        new SecondaryDrawerItem()
                                                                .withName(getString(R.string.drawer_item_upload_existing_arbitrary))
                                                                .withIdentifier(101),
                                                        new SecondaryDrawerItem()
                                                                .withName(getString(R.string.drawer_item_upload_existing_recognize))
                                                                .withIdentifier(102),
                                                        new SecondaryDrawerItem()
                                                                .withName(getString(R.string.drawer_item_upload_existing_mergepdf))
                                                                .withIdentifier(103),
                                                        new SecondaryDrawerItem()
                                                                .withName(getString(R.string.drawer_item_upload_existing_recognize_mergepdf))
                                                                .withIdentifier(104)
                                                ),
                                        new ExpandableDrawerItem()
                                                .withName(getString(R.string.drawer_item_upload_camera))
                                                .withSubItems(
                                                        new SecondaryDrawerItem()
                                                                .withName(getString(R.string.drawer_item_upload_camera_arbitrary))
                                                                .withIdentifier(201),
                                                        new SecondaryDrawerItem()
                                                                .withName(getString(R.string.drawer_item_upload_camera_recognize))
                                                                .withIdentifier(202),
                                                        new SecondaryDrawerItem()
                                                                .withName(getString(R.string.drawer_item_upload_camera_mergepdf))
                                                                .withIdentifier(203),
                                                        new SecondaryDrawerItem()
                                                                .withName(getString(R.string.drawer_item_upload_camera_recognize_mergepdf))
                                                                .withIdentifier(204)
                                                )
                                ),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.action_settings)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    long id = drawerItem.getIdentifier();
                    if (id == 1){
                        if (IS_ONLINE_NOT_OFFLINE) return true;
                        IS_ONLINE_NOT_OFFLINE = true;
                        this.onlineModel.startExploring();
                    }
                    if (id == 2){
                        if (!IS_ONLINE_NOT_OFFLINE) return true;
                        IS_ONLINE_NOT_OFFLINE = false;
                        this.offlineModel.startExploring();
                    }
                    if (id == 101){
                        this.intentHandler.onUploadExistingArbitraryClicked();
                        return false;
                    }
                    if (id == 102){
                        this.intentHandler.onUploadExistingRecognizeClicked();
                        return false;
                    }
                    if (id == 103){
                        this.intentHandler.onUploadExistingMergePDFClicked();
                        return false;
                    }
                    if (id == 104){
                        this.intentHandler.onUploadExistingRecognizeMergePDFClicked();
                        return false;
                    }
                    if (id == 201){
                        this.intentHandler.onUploadCameraArbitraryClicked();
                        return false;
                    }
                    if (id == 202){
                        this.intentHandler.onUploadCameraRecognizeClicked();
                        return false;
                    }
                    if (id == 203){
                        this.intentHandler.onUploadCameraMergePDFClicked();
                        return false;
                    }
                    if (id == 204){
                        this.intentHandler.onUploadCameraRecognizeMergePDFClicked();
                        return false;
                    }
                    return false;
                })
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.intentHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateContents(List<FileItem> fileList, String folderName) {
        Log.i("Online Presenter", "Update contents call.");
        //this.fileList = new ArrayList<>(fileList);

        // fileItems are already sorted by name
        // we want folders to lead this list
        ArrayList<String> files = new ArrayList<>();
        files.ensureCapacity(fileList.size());
        ArrayList<String> types = new ArrayList<>();
        types.ensureCapacity(fileList.size());
        fileList.stream().filter(fileItem -> "dir".equals(fileItem.getType()))
                .forEach(fileItem -> {
                    files.add(fileItem.getName());
                    types.add("dir");
                });
        fileList.stream().filter(fileItem -> !"dir".equals(fileItem.getType()))
                .forEach(fileItem -> {
                    files.add(fileItem.getName());
                    types.add(fileItem.getType());
                });
        this.fileManager.setFileList(files);
        this.fileManager.setFileTypesList(types);
        this.fileManager.setFolderName(folderName);

    }

    @Override
    public void showToast(String msg, boolean isLong) {
        // TODO
        Toast.makeText(this, msg, (isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
    }

    @Override
    public void showSnackbar(String text, int duration) {
        Snackbar.make(findViewById(R.id.fragment_container), text, duration).show();
    }

    @Override
    public void onFileClicked(String fileName) {
        /*for(FileItem item: this.fileList)
            if(item.getName().equals(fileName))*/
        if (IS_ONLINE_NOT_OFFLINE) {
            this.onlineModel.getAccess(fileName);
        } else {
            this.offlineModel.getAccess(fileName);
        }
    }

    @Override
    public void onOptionClicked(String option, String fileName) {
        /*FileItem item = null;
        for(FileItem item_: this.fileList)
            if(item_.getName().equals(fileName))
                item = item_;*/

        if (IS_ONLINE_NOT_OFFLINE) {
            switch (option){
                case "Open":
                    this.onlineModel.getAccess(fileName);
                    break;
                case "Delete":
                    this.onlineModel.delete(fileName);
                    break;
                case "Rename":
                    this.onlineModel.rename(fileName, "That's one small step for a man, one giant leap for mankind.");
                    break;
            }
        } else {
            switch (option){
                case "Open":
                    this.offlineModel.getAccess(fileName);
                    break;
                case "Delete":
                    this.offlineModel.delete(fileName);
                    break;
                case "Rename":
                    this.offlineModel.rename(fileName, "That's one small step for a man, one giant leap for mankind.");
                    break;
            }
        }

    }

    @Override
    public void onGetBack() {
        if (IS_ONLINE_NOT_OFFLINE) {
            this.onlineModel.goUp();
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext(); // exactly application context, not other else context
    }

    // Requests WRITE_EXTERNAL_STORAGE permission
    private void requestPermissions() {
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {*/
           int PERMISSION_REQUEST_CODE = 1;
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_REQUEST_CODE);
                        }
            }
        /*}*/
    }

    @Override
    public void onRefreshRequested() {
        if (IS_ONLINE_NOT_OFFLINE) {
            this.onlineModel.update();
        } else {
            this.offlineModel.update();
        }
    }

    @Override
    public void setUpdatingState(boolean state) {
        this.fileManager.setUpdatingState(state);
    }

    @Override
    public void onBackPressed() {
        if (IS_ONLINE_NOT_OFFLINE) {
            if (!this.onlineModel.goUp())
                super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateContentsLocal(List<String> fileNames, List<String> fileTypes) {
        this.fileManager.setFileList(fileNames);
        this.fileManager.setFileTypesList(fileTypes);
        this.fileManager.setFolderName(OfflineModel.LOCAL_FOLDER_NAME);
    }
}
