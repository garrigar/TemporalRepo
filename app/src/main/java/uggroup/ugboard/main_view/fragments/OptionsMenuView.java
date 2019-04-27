package uggroup.ugboard.main_view.fragments;

import android.support.v4.app.FragmentManager;

import java.util.List;

import uggroup.ugboard.GeneralView;

public interface OptionsMenuView extends GeneralView {
    void setOptionsList(List<String> options);

    void setOnOptionClickListener(OptionsMenuDialog.OnOptionClickListener listener);
}

