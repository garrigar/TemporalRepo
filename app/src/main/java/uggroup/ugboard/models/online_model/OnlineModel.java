package uggroup.ugboard.models.online_model;

import android.net.Uri;

import java.util.List;

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
     * Upload files to current directory
     * @param uriList - List with URIs of files to upload
     */
    void uploadFiles(List<Uri> uriList);

    /**
     * Upload photos to ... and recognize
     * @param uriList - List with URIs of files to upload
     */
    void uploadPhotosAndRecognize(List<Uri> uriList);

    /**
     * Upload photos to ... and merge to PDF file
     * @param uriList - List with URIs of files to upload
     */
    void uploadPhotosAndMergePDF(List<Uri> uriList);

}
