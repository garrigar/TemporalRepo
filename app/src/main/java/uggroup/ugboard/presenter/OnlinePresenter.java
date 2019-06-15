package uggroup.ugboard.presenter;

import android.content.Context;

import java.util.List;

public interface OnlinePresenter {
    void updateFileList(List<Object>fileList, String folderName);
    void showToast(String msg, int length);
}
