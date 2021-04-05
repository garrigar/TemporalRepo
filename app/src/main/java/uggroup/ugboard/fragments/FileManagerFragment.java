package uggroup.ugboard.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import uggroup.ugboard.fragments.file_manager_view.FileManagerView;
import uggroup.ugboard.fragments.file_manager_view.FileManagerViewImpl;
import uggroup.ugboard.fragments.file_manager_view.option_menu_dialog.OptionsMenuDialog;


public class FileManagerFragment extends Fragment implements FileManagerView.FileLongClickListener, FileManagerView.FileClickListener, OptionsMenuDialog.OnOptionClickListener,
 FileManager, FileManagerView.RefreshRequestListener, FileManagerView.GetBackListener {

    FileManagerView mainView;
    FileLongClickListener fileLongClickListener;
    FileClickListener fileClickListener;
    OnOptionClickListener optionClickListener;
    RefreshRequestListener refreshRequestListener;
    GetBackListener getBackListener;

    List<String> fileOptions;

    // This is a stupid fix for longClick and optionClick methods.
    // The only moment when we want to use longClick is to invoke
    // options menu, thus we don't need to listen for this click
    // outside the fragment/fileManagerView. It's better to return
    // the fileName and the chosen option inside optionClicked method,
    // rather than remember the `long clicked file` and then
    // perform the action chosen in options menu.
    // So we do this here just not to do this in presenter.
    String longClickedFile; // Name of the `long clicked file`

    public FileManagerFragment() {
        // Required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(this.mainView != null)
            return this.mainView.getRootView();
        this.mainView = new FileManagerViewImpl(getContext(), container);
        this.mainView.setFileClickListener(this);
        this.mainView.setFileLongClickListener(this);
        this.mainView.setOnOptionClickListener(this);
        this.mainView.setRefreshRequestListener(this);
        this.mainView.setGetBackListener(this);
        if(this.fileOptions != null) {
            this.mainView.setOptionsList(this.fileOptions);
        }
        Log.i("UGBoard", "FileManager fragment created the view!");
        return this.mainView.getRootView();
    }

    @Override
    public void onFileLongClicked(String fileName) {
        this.longClickedFile = fileName;
        this.mainView.showOptionsMenu(getFragmentManager(), "Tag");
        Log.i("UGBoard", "Long click!");
        if(this.fileLongClickListener != null)
            this.fileLongClickListener.onFileLongClicked(fileName);
    }

    @Override
    public void onFileClicked(String fileName) {
        String msg = "File '" + fileName + "' is clicked!";
        this.mainView.showToast(msg, Toast.LENGTH_LONG);
        if(this.fileClickListener != null)
            this.fileClickListener.onFileClicked(fileName);
    }

    @Override
    public void onOptionClicked(String option) {
        String msg = "Options '" + option + "' is clicked!";
        this.mainView.showToast(msg, Toast.LENGTH_LONG);
        if(this.optionClickListener != null)
            this.optionClickListener.onOptionClicked(option, this.longClickedFile);
    }

    @Override
    public void setFileList(List<String> fileNames) {
        if(this.mainView == null)
            return;
        this.mainView.setFileList(fileNames);
    }

    @Override
    public void setFileTypesList(List<String> fileTypes) {
        if (this.mainView == null)
            return;
        this.mainView.setFileTypesList(fileTypes);
    }

    @Override
    public void setOptionsList(List<String> fileOptions) {
        // mainView might not be initialized yet, so we
        // save the file options in order to set them later
        if(this.mainView != null) {
            this.mainView.setOptionsList(fileOptions);
            return;
        }
        this.fileOptions = fileOptions;
    }

    @Override
    public void setFolderName(String name) {
        this.mainView.setFolderName(name);
    }

    @Override
    public void setFileClickListener(FileClickListener listener) {
        this.fileClickListener = listener;
    }

    @Override
    public void setFileLongClickListener(FileLongClickListener listener) {
        this.fileLongClickListener = listener;
    }

    @Override
    public void setOnOptionClickListener(OnOptionClickListener listener) {
        this.optionClickListener = listener;
    }

    @Override
    public void setRefreshRequestListener(RefreshRequestListener listener) {
        this.refreshRequestListener = listener;
    }

    @Override
    public void setGetBackListener(GetBackListener listener) {
        this.getBackListener = listener;
    }

    @Override
    public void onRefreshRequested() {
        if(this.refreshRequestListener != null)
            this.refreshRequestListener.onRefreshRequested();
    }

    @Override
    public void onGetBack() {
        if(this.getBackListener != null)
            this.getBackListener.onGetBack();
    }

    @Override
    public void setUpdatingState(boolean state) {
        this.mainView.setUpdatingState(state);
    }
}
