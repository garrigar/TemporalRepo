package uggroup.ugboard.presenter;

import android.content.Context;

public interface BaseFileManagerPresenter {

    void showToast(String text, boolean isLong);

    void showSnackbar(String text, int duration);

    Context getContext();

    void setUpdatingState(boolean state);

}
