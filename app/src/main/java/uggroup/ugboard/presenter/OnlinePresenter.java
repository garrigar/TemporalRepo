package uggroup.ugboard.presenter;

import android.content.Context;

import java.util.List;

import uggroup.ugboard.models.online_model.FileItem;

public interface OnlinePresenter {

    void updateContents(List<FileItem> newList, String newFolderName);

    void showToast(String text, boolean isLong);

    Context getContext();

}
