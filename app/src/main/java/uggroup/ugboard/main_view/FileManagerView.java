package uggroup.ugboard.main_view;

import java.util.List;

import uggroup.ugboard.GeneralView;

public interface FileManagerView extends GeneralView {
    void setFileList(List<String> fileNames);
    void setOptionsList(List<String> fileOptions);
    void setFolderName(String name);
    void showDialogOptionsMenu();

    void setFileClickListener(FileClickListener listener);
    void setFileLongClickListener(FileLongClickListener listener);


    interface FileClickListener {
        void onFileClicked(String fileName);
    }

    interface FileLongClickListener {
        void onFileLongClicked(String fileName);
    }
}
