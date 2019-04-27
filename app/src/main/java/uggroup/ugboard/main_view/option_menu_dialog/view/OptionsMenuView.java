package uggroup.ugboard.main_view.option_menu_dialog.view;

import java.util.List;

import uggroup.ugboard.GeneralView;
import uggroup.ugboard.main_view.option_menu_dialog.OptionsMenuDialog;

public interface OptionsMenuView extends GeneralView {
    void setOptionsList(List<String> options);
    void setOnOptionClickListener(OptionsMenuDialog.OnOptionClickListener listener);
}

