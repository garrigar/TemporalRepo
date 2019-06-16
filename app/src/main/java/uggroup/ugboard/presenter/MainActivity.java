package uggroup.ugboard.presenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import uggroup.ugboard.R;
import uggroup.ugboard.fragments.FileManager;
import uggroup.ugboard.fragments.FileManagerFragment;
import uggroup.ugboard.models.online_model.FileItem;
import uggroup.ugboard.models.online_model.OnlineModel;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends AppCompatActivity implements
        OnlinePresenter, FileManager.FileClickListener, FileManager.OnOptionClickListener {

    // Keep the fragment inside though it's the same
    // thing as FileManager. We need it for convenient
    // fragment transactions
    FileManagerFragment fileManagerFragment;
    FileManager fileManager;
    OnlineModel onlineModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Add file manager via fragment transaction
        this.fileManagerFragment = new FileManagerFragment();
        this.fileManager = this.fileManagerFragment;
        this.fileManager.setFileClickListener(this);
        this.fileManager.setOnOptionClickListener(this);

        // We attach the file manager first by default.
        attachFileManager();
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
        // TODO
        this.fileManager.setFolderName(folderName);
    }

    @Override
    public void showToast(String msg, boolean isLong) {
        // TODO
    }

    @Override
    public void onFileClicked(String fileName) {
        // TODO
        //this.onlineModel.getAccess();
    }

    @Override
    public void onOptionClicked(String option, String fileName) {
        // TODO
    }
}
