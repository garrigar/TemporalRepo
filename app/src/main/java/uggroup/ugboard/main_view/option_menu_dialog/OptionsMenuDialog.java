package uggroup.ugboard.main_view.option_menu_dialog;

import android.support.v4.app.FragmentManager;

import java.util.List;

public interface OptionsMenuDialog {
    void setOptionsList(List<String> options);
    void setOnOptionClickListener(OnOptionClickListener listener);
    void showMenu(FragmentManager manager, String tag);

    interface OnOptionClickListener {
        void onOptionClicked(String option);
    }
}
