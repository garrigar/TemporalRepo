package uggroup.ugboard.presenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import uggroup.ugboard.main_view.MainView;
import uggroup.ugboard.main_view.MainViewImpl;

public class MainActivity extends AppCompatActivity {

    MainView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainView = new MainViewImpl(this, null);
        setContentView(this.mainView.getRootView());

        // Hard code test
        ArrayList<String> list = new ArrayList<String>();
        list.add("File 1");
        list.add("File 2");
        list.add("File 3");
        this.mainView.setFileList(list);
    }
}
