package uggroup.ugboard.models.online_model;

import uggroup.ugboard.models.online_model.FileItem;
import uggroup.ugboard.presenter.OnlinePresenter;

public interface OnlineModel {

    void setOnlinePresenter(OnlinePresenter onlinePresenter);

    void startExploring();

    void goUp();

    void getAccess(FileItem fileItem);

    void delete(FileItem fileItem);

    void rename(FileItem fileItem);

    void update();

}
