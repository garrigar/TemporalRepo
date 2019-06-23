package uggroup.ugboard.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uggroup.ugboard.R;
import uggroup.ugboard.fragments.FileManager;
import uggroup.ugboard.fragments.FileManagerFragment;
import uggroup.ugboard.models.online_model.FileItem;
import uggroup.ugboard.models.online_model.OnlineModel;
import uggroup.ugboard.models.online_model.OnlineModelImpl;
import uggroup.ugboard.models.online_model.retrofit.IUGDBackend;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

public class MainActivity extends AppCompatActivity implements
        OnlinePresenter, FileManager.FileClickListener, FileManager.OnOptionClickListener, FileManager.GetBackListener {


    // Keep the fragment inside though it's the same
    // thing as FileManager. We need it for convenient
    // fragment transactions
    FileManagerFragment fileManagerFragment;
    FileManager fileManager;
    OnlineModel onlineModel;

    // For later responses to the online model
    ArrayList<FileItem> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Add file manager via fragment transaction
        this.fileManagerFragment = new FileManagerFragment();
        this.fileManager = this.fileManagerFragment;
        this.fileManager.setFileClickListener(this);
        this.fileManager.setOnOptionClickListener(this);
        this.fileManager.setGetBackListener(this);
        ArrayList<String> options =  new ArrayList<>();
        options.add("Open");
        options.add("Rename");
        options.add("Delete");
        this.fileManager.setOptionsList(options);
        // We attach the file manager first by default.
        attachFileManager();
        setUpNavigationDrawer();

        this.onlineModel = OnlineModelImpl.getInstance();
        this.onlineModel.setOnlinePresenter(this);
       // this.onlineModel.startExploring();

        requestPermissions();

        // TESTING. DON'T BE AFRAID
        //for (int i = 1; i < 65; i++){
        //    uploadMultipart(this, 0);
        //}
    }

    @Override
    protected void onResumeFragments() {
        // We need to call startExploring method here because
        // the onlineModel calls updating the fileList too early,
        // e.c. when the view is not created yet.
        super.onResumeFragments();
        this.onlineModel.startExploring();
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
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Super Haker").withEmail("ebaniy_vrot@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withDisplayBelowStatusBar(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_cloud),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_local),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_camera),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_gallery),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.action_settings)
                )
                .build();
    }

    @Override
    public void updateContents(List<FileItem> fileList, String folderName) {
        Log.i("Online Presenter", "Update contents call.");
        this.fileList = new ArrayList<>(fileList);
        ArrayList<String> files = new ArrayList<>();
        for(FileItem a: fileList) {
            files.add(a.getName());
        }
        this.fileManager.setFileList(files);
        this.fileManager.setFolderName(folderName);

    }

    @Override
    public void showToast(String msg, boolean isLong) {
        // TODO
        Toast.makeText(this, msg, (isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
    }

    @Override
    public void onFileClicked(String fileName) {
        /*for(FileItem item: this.fileList)
            if(item.getName().equals(fileName))*/
        this.onlineModel.getAccess(fileName);
    }

    @Override
    public void onOptionClicked(String option, String fileName) {
        /*FileItem item = null;
        for(FileItem item_: this.fileList)
            if(item_.getName().equals(fileName))
                item = item_;*/

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

    }

    @Override
    public void onGetBack() {
        this.onlineModel.goUp();
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

    public void uploadMultipart(final Context context, int i_) {
        try {
            //String uploadId =
            MultipartUploadRequest mur = new MultipartUploadRequest(context, IUGDBackend.BASE_URL + "upload");
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
            for (int i = 0; i < 65; i++){
                mur.addFileToUpload("/sdcard/UGBoard/upload/" + i + ".JPG", "/" + (i + 100) + ".JPG");
            }


                            //.addFileToUpload("/sdcard/UGBoard/1.jpg", "/upload.jpg")
                            mur.setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }
}
