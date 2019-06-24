package uggroup.ugboard.fragments.file_manager_view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uggroup.ugboard.R;
import uggroup.ugboard.fragments.file_manager_view.file_list.FileListAdapter;
import uggroup.ugboard.fragments.file_manager_view.option_menu_dialog.OptionsMenuDialog;
import uggroup.ugboard.fragments.file_manager_view.option_menu_dialog.OptionsMenuDialogImpl;

public class FileManagerViewImpl implements FileManagerView {

    private String defaultLogTag = "FileManagerViewImpl";
    private String logTag = this.defaultLogTag;

    private Context context;
    private Toolbar folderNameBar;
    private View rootView;
    private ListView fileList;
    private FileListAdapter fileListAdapter;
    private OptionsMenuDialog optionsMenuDialog;
    private Toast toast;
    private SwipeRefreshLayout swipeRefresh;

    @SuppressLint("ShowToast")
    public FileManagerViewImpl(Context context, ViewGroup container) {
        this.context = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.file_manager, container, false);
        this.folderNameBar = this.rootView.findViewById(R.id.folderNameBar);
        this.folderNameBar.setNavigationIcon(R.drawable.ic_menu_revert);

        // Set default value to folderName so that we can see it works.
        this.folderNameBar.setTitle("No folder name");
        this.fileList = this.rootView.findViewById(R.id.fileListListView);
        this.fileListAdapter = new FileListAdapter(context);
        this.fileListAdapter.setLogTag(
                this.logTag+"."+this.fileListAdapter.getDefaultLogTag()
        );
        this.fileList.setAdapter(this.fileListAdapter);
        this.optionsMenuDialog = new OptionsMenuDialogImpl();
        this.toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        this.optionsMenuDialog.setLogTag(
                this.logTag+"."+this.optionsMenuDialog.getDefaultLogTag()
        );
        this.swipeRefresh = this.rootView.findViewById(R.id.swiperefresh);


        Log.i(this.logTag, "FileManagerViewImpl is instantiated");
    }

    @Override
    public void setFileList(List<String> fileNames) {
        this.fileListAdapter.setFileNames(fileNames);
        this.fileListAdapter.notifyDataSetChanged();

        Log.i(this.logTag, "setFileList call");
    }

    @Override
    public void setOptionsList(List<String> fileOptions) {
        this.optionsMenuDialog.setOptionsList(fileOptions);

        Log.i(this.logTag, "setOptionsList call");
    }

    @Override
    public void setOptionsTitle(String title) {
        this.optionsMenuDialog.setOptionsTitle(title);

        Log.i(this.logTag, "setOptionsTitle call");
    }

    @Override
    public void setOnOptionClickListener(OnOptionClickListener listener) {
        this.optionsMenuDialog.setOnOptionClickListener(listener);

        Log.i(this.logTag, "setOnOptionClickListener call");
    }

    @Override
    public void showOptionsMenu(FragmentManager manager, String tag) {
        this.optionsMenuDialog.showOptionsMenu(manager, tag);

        Log.i(this.logTag, "showOptionsMenu call");
    }

    @Override
    public void setFolderName(String name) {

        this.folderNameBar.setTitle(name);
        Log.i(this.logTag, "setFolderName call");
    }

    @Override
    public void setFileClickListener(final FileClickListener listener) {
        this.fileList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView fileNameTxt = view.findViewById(R.id.fileNameTxt);
                        listener.onFileClicked(fileNameTxt.getText().toString());

                        Log.i(FileManagerViewImpl.this.logTag + ".FileClickListener",
                                "onItemClick call");
                    }
                }
        );
        Log.i(this.logTag, "setFileClickListener call");
    }

    @Override
    public void setFileLongClickListener(final FileLongClickListener listener) {
        this.fileList.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView fileNameTxt = view.findViewById(R.id.fileNameTxt);
                        listener.onFileLongClicked(fileNameTxt.getText().toString());

                        Log.i(FileManagerViewImpl.this.logTag + ".FileLongClickListener",
                                "onItemLongClick call");
                        return false;
                    }
                }
        );
        Log.i(this.logTag, "setFileLongClickListener call");
    }

    @Override
    public void setRefreshRequestListener(final RefreshRequestListener listener) {
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FileManagerViewImpl.this.swipeRefresh.setRefreshing(false);
                listener.onRefreshRequested();
            }
        });
    }

    @Override
    public void setGetBackListener(final GetBackListener listener) {
        this.folderNameBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGetBack();
            }
        });
    }

    @Override
    public View getRootView() {
        Log.i(this.logTag, "getRootView call");
        return this.rootView;
    }

    @Override
    public void showToast(String msg, int length) {
        // If there is another toast is visible, we cancel it and show new one
        if(this.toast.getView().getWindowVisibility() == View.VISIBLE) {
            Log.i(this.logTag, "There is a toast is still visible");
            this.toast.cancel();
        }
        this.toast.setDuration(length);
        this.toast.setText(msg);
        this.toast.show();

        Log.i(this.logTag, "showToast call");
    }

    @Override
    public String getDefaultLogTag() {
        Log.i(this.logTag, "getDefaultLogTag call");
        return this.defaultLogTag;
    }

    @Override
    public void setLogTag(String tag) {
        this.logTag = tag;
        Log.i(this.logTag, "setLogTag call");
    }
}
