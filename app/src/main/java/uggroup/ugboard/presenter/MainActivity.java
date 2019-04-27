package uggroup.ugboard.presenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import uggroup.ugboard.main_view.MainView;
import uggroup.ugboard.main_view.MainViewImpl;
import uggroup.ugboard.model.Server;

public class MainActivity extends AppCompatActivity implements MainView.FileLongClickListener {

    MainView mainView;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainView = new MainViewImpl(this, null, getSupportFragmentManager());
        setContentView(this.mainView.getRootView());


        this.server = Server.getInstance();
        this.mainView.setFileList(this.server.getFileNames());
    }

    @Override
    public void onFileLongClicked(String fileName) {
        this.mainView.showDialogOptionsMenu();
    }
}
