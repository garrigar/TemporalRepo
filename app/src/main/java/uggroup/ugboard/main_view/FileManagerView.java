package uggroup.ugboard.main_view;

import java.util.List;

import uggroup.ugboard.CoreView;
import uggroup.ugboard.MVPView;
import uggroup.ugboard.main_view.option_menu_dialog.OptionsMenuDialog;

public interface FileManagerView extends CoreView, OptionsMenuDialog {
    void setFileList(List<String> fileNames);
    void setOptionsList(List<String> fileOptions);
    void setFolderName(String name);

    void setFileClickListener(FileClickListener listener);
    void setFileLongClickListener(FileLongClickListener listener);


    interface FileClickListener {
        void onFileClicked(String fileName);
    }

    interface FileLongClickListener {
        void onFileLongClicked(String fileName);
    }
}
