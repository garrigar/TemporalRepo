package uggroup.ugboard.main_view.option_menu_dialog;

import android.support.v4.app.FragmentManager;

import java.util.List;

import uggroup.ugboard.LoggedInstance;

public interface OptionsMenuDialog extends LoggedInstance {
    void setOptionsList(List<String> options);
    void setOptionsTitle(String title);
    void setOnOptionClickListener(OnOptionClickListener listener);
    void showOptionsMenu(FragmentManager manager, String tag);

    interface OnOptionClickListener {
        void onOptionClicked(String option);
    }
}
