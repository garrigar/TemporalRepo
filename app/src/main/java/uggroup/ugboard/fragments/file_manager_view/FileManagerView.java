package uggroup.ugboard.fragments.file_manager_view;

import java.util.List;

import uggroup.ugboard.CoreView;
import uggroup.ugboard.fragments.file_manager_view.option_menu_dialog.OptionsMenuDialog;

public interface FileManagerView extends CoreView, OptionsMenuDialog {
    void setFileList(List<String> fileNames);
    void setOptionsList(List<String> fileOptions);
    void setFolderName(String name);

    void setFileClickListener(FileClickListener listener);
    void setFileLongClickListener(FileLongClickListener listener);
    void setRefreshRequestListener(RefreshRequestListener listener);
    void setGetBackListener(GetBackListener listener);


    interface FileClickListener {
        void onFileClicked(String fileName);
    }

    interface FileLongClickListener {
        void onFileLongClicked(String fileName);
    }

    interface RefreshRequestListener {
        void onRefreshRequested();
    }

    interface GetBackListener {
        void onGetBack();
    }
}
