package uggroup.ugboard.main_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import uggroup.ugboard.R;
import uggroup.ugboard.main_view.file_list.FileListAdapter;

public class MainViewImpl implements MainView {

    private Context context;
    private View rootView;
    private TextView folderName;
    private ListView fileList;
    private FileListAdapter fileListAdapter;

    public MainViewImpl(Context context, ViewGroup container) {
        this.context = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.activity_main, container);
        this.folderName = this.rootView.findViewById(R.id.folderNameTxt);
        this.fileList = this.rootView.findViewById(R.id.fileListListView);
        this.fileListAdapter = new FileListAdapter(context);
        this.fileList.setAdapter(this.fileListAdapter);
        this.fileList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
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
    public View getRootView() {
        return this.rootView;
    }
}
