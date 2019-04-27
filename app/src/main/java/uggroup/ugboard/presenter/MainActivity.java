package uggroup.ugboard.presenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import uggroup.ugboard.main_view.FileManagerView;
import uggroup.ugboard.main_view.FileManagerViewImpl;
import uggroup.ugboard.model.Server;

public class MainActivity extends AppCompatActivity implements FileManagerView.FileLongClickListener {

    FileManagerView mainView;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainView = new FileManagerViewImpl(this, null, getSupportFragmentManager());
        setContentView(this.mainView.getRootView());


        this.server = Server.getInstance();
        this.mainView.setFileList(this.server.getFileNames());
        this.mainView.setOptionsList(this.server.getFileActions());
        this.mainView.setFileLongClickListener(this);
        Log.i("UGBoard", "App started!");
    }

    @Override
    public void onFileLongClicked(String fileName) {
        this.mainView.showDialogOptionsMenu();
        Log.i("UGBoard", "Long click!");
    }
}
