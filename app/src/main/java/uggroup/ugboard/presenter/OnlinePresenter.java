package uggroup.ugboard.presenter;

import android.content.Context;

import java.util.List;

import uggroup.ugboard.models.online_model.FileItem;

public interface OnlinePresenter extends BaseFileManagerPresenter {

    void updateContents(List<FileItem> newList, String newFolderName);

    //void showToast(String text, boolean isLong);

    //void showSnackbar(String text, int duration);

    //void setUpdatingState(boolean state);

    //Context getContext();

}
