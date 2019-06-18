package uggroup.ugboard.fragments;

import java.util.List;

import uggroup.ugboard.fragments.file_manager_view.FileManagerView;

// This interface is made to uncouple the code.
// We don't want to have a reference to FileManagerView,
// it's better to delegate all the necessary invocations
// rather than keep only parts of the objects we need (like keeping
// the view of the fragment outside the fragment).

// Simple words: no one must know about the FileManagerView but the class
// uses it as main layout.
public interface FileManager {
    void setFileList(List<String> fileNames);
    void setOptionsList(List<String> fileOptions);
    void setFolderName(String name);

    void setFileClickListener(FileClickListener listener);

    @Deprecated
    void setFileLongClickListener(FileLongClickListener listener);
    void setOnOptionClickListener(OnOptionClickListener listener);
    void setRefreshRequestListener(RefreshRequestListener listener);
    void setGetBackListener(GetBackListener listener);


    interface OnOptionClickListener {
        void onOptionClicked(String option, String fileName);
    }

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
