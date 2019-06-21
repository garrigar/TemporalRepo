package uggroup.ugboard.fragments.file_manager_view.option_menu_dialog;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import uggroup.ugboard.LoggedInstance;

public interface OptionsMenuDialog extends LoggedInstance {
    // Options list will be hardcoded cause server
    // doesn't provide this list.
    void setOptionsList(List<String> options);
    // This method won't be used in presenter,
    // there is no need in changing the options title.
    void setOptionsTitle(String title);
    // This method will be called only from FileManagerView,
    // there is no need in calling it from any place but FileManagerView
    void showOptionsMenu(FragmentManager manager, String tag);

    void setOnOptionClickListener(OnOptionClickListener listener);



    interface OnOptionClickListener {
        void onOptionClicked(String option);
    }
}
