package uggroup.ugboard.models.online_model;

import uggroup.ugboard.presenter.OnlinePresenter;

public interface OnlineModel {

    /**
     * Sets OnlinePresenter
     * @param onlinePresenter - OnlinePresenter to set
     */
    void setOnlinePresenter(OnlinePresenter onlinePresenter);

    /**
     * Beginning exploring
     */
    void startExploring();

    /**
     * Go up in file hierarchy
     */
    void goUp();

    /**
     * Go into folder or download file with name <>filename</> in current folder
     * @param filename - name of folder or file to get access to
     */
    void getAccess(String filename);

    /**
     * Delete folder or file with name <>filename</> in current folder
     * @param filename - name of folder or file to delete
     */
    void delete(String filename);

    /**
     * Rename folder or file with name <>filename</> in current folder
     * @param filename - name of folder or file to rename
     * @param newName - new name
     */
    void rename(String filename, String newName);

    /**
     * Update list of files in current directory
     */
    void update();

    /**
     * Upload file to current directory
     * @param path - absolute path to file to upload
     */
    void uploadFile(String path);

}
