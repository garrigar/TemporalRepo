package uggroup.ugboard.presenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import uggroup.ugboard.main_view.FileManagerView;
import uggroup.ugboard.main_view.FileManagerViewImpl;
import uggroup.ugboard.main_view.option_menu_dialog.OptionsMenuDialog;
import uggroup.ugboard.model.Server;

public class MainActivity extends AppCompatActivity implements
        FileManagerView.FileLongClickListener, OptionsMenuDialog.OnOptionClickListener, FileManagerView.FileClickListener {

    FileManagerView mainView;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainView = new FileManagerViewImpl(this, null);
        setContentView(this.mainView.getRootView());


        this.server = Server.getInstance();
        this.mainView.setFolderName(this.server.getCurrentFolder());
        this.mainView.setFileList(this.server.getFileNames());
        this.mainView.setOptionsList(this.server.getFileActions());

        this.mainView.setFileLongClickListener(this);
        this.mainView.setFileClickListener(this);
        this.mainView.setOnOptionClickListener(this);
        Log.i("UGBoard", "App started!");
    }

    @Override
    public void onFileLongClicked(String fileName) {
        this.mainView.showOptionsMenu(getSupportFragmentManager(), "Tag");
        Log.i("UGBoard", "Long click!");
    }

    @Override
    public void onOptionClicked(String option) {
        String msg = "Options '" + option + "' is clicked!";
        this.mainView.showToast(msg, Toast.LENGTH_LONG);
    }

    @Override
    public void onFileClicked(String fileName) {
        String msg = "File '" + fileName + "' is clicked!";
        this.mainView.showToast(msg, Toast.LENGTH_LONG);
    }
}
