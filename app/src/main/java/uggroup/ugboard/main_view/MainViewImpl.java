package uggroup.ugboard.main_view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import uggroup.ugboard.R;
import uggroup.ugboard.main_view.file_list.FileListAdapter;
import uggroup.ugboard.main_view.fragments.OptionsMenuDialog;
import uggroup.ugboard.main_view.fragments.OptionsMenuDialogImpl;

public class MainViewImpl implements MainView {

    private Context context;
    private View rootView;
    private TextView folderName;
    private ListView fileList;
    private FileListAdapter fileListAdapter;
    private OptionsMenuDialog optionsMenuDialog;
    // For the OptionsMenuDialog because it is fragment.
    private FragmentManager manager;

    public MainViewImpl(Context context, ViewGroup container, FragmentManager manager) {
        this.context = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.activity_main, container);
        this.folderName = this.rootView.findViewById(R.id.folderNameTxt);
        this.fileList = this.rootView.findViewById(R.id.fileListListView);
        this.fileListAdapter = new FileListAdapter(context);
        this.fileList.setAdapter(this.fileListAdapter);
        this.manager = manager;
        this.optionsMenuDialog = new OptionsMenuDialogImpl();
    }

    @Override
    public void setFileList(ArrayList<String> fileNames) {
        this.fileListAdapter.setFileNames(fileNames);
        this.fileListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFolderName(String name) {
        this.folderName.setText(name);
    }

    @Override
    public void setFileClickListener(final FileClickListener listener) {
        Log.i("UGBoard", "setFileClickListener");
        this.fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                     @Override
                                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                         TextView fileNameTxt = view.findViewById(R.id.fileNameTxt);
                                                         listener.onFileClicked(fileNameTxt.getText().toString());

                                                         Log.i("UGBoard", "onItemClick");
                                                     }
                                                 }
        );
    }

    @Override
    public void setFileLongClickListener(final FileLongClickListener listener) {
        Log.i("UGBoard", "setFileLongClickListener");
        this.fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                     @Override
                                                     public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                         TextView fileNameTxt = view.findViewById(R.id.fileNameTxt);
                                                         listener.onFileLongClicked(fileNameTxt.getText().toString());

                                                         Log.i("UGBoard", "onItemLongClick");
                                                         return false;
                                                     }
                                                 }
        );
    }

    @Override
    public void showDialogOptionsMenu() {
        this.optionsMenuDialog.showMenu(manager, "Test");
        Log.i("UGBoard", "showDialogOptionsMenu");
    }

    @Override
    public View getRootView() {
        return this.rootView;
    }
}
