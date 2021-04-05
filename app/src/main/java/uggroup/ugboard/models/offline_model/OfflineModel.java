package uggroup.ugboard.models.offline_model;

import uggroup.ugboard.presenter.OfflinePresenter;

public interface OfflineModel {

    String LOCAL_FOLDER_NAME = "UGBoard"; // keep in mind also an entry in file_paths.xml

    /**
     * Sets OnlinePresenter
     * @param onlinePresenter - OnlinePresenter to set
     */
    void setOfflinePresenter(OfflinePresenter onlinePresenter);

    /**
     * Beginning exploring
     */
    void startExploring();

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


}
