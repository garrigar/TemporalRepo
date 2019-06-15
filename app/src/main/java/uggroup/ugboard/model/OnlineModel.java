package uggroup.ugboard.model;

import uggroup.ugboard.presenter.OnlinePresenter;

public interface OnlineModel {
    // Set the presenter for later communication
    void setOnlinePresenter(OnlinePresenter presenter);
    // Go higher in the file hierarchy
    void getOut();
    // Request access to the file/folder
    void getAccess(Object file);
    // Delete file
    void delete(Object file);
    // Rename the file
    void rename(Object file);
    // Request updated file list from the server
    void updateFileList();
}
