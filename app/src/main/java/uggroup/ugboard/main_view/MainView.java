package uggroup.ugboard.main_view;

import java.util.ArrayList;

import uggroup.ugboard.GeneralView;

public interface MainView extends GeneralView {
    void setFileList(ArrayList<String> fileNames);
    void setFolderName(String name);
}
