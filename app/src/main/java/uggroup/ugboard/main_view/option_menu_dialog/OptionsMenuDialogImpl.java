package uggroup.ugboard.main_view.option_menu_dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uggroup.ugboard.main_view.option_menu_dialog.view.OptionsMenuView;
import uggroup.ugboard.main_view.option_menu_dialog.view.OptionsMenuViewImpl;

public class OptionsMenuDialogImpl extends DialogFragment implements OptionsMenuDialog {

    private Context context;
    private FragmentManager manager;
    private OptionsMenuView optionsMenuView;

    static OptionsMenuDialog newInstance() {
        return new OptionsMenuDialogImpl();
    }

    @Override
    public void setOptionsList(List<String> options) {
        this.optionsMenuView.setOptionsList(options);
    }

    @Override
    public void setOnOptionClickListener(OnOptionClickListener listener) {
        this.optionsMenuView.setOnOptionClickListener(listener);
    }

    @Override
    public void showMenu(FragmentManager manager, String tag) {
        this.show(manager, tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.optionsMenuView = new OptionsMenuViewImpl(getContext(), null);
        this.manager = getFragmentManager();
        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return this.optionsMenuView.getRootView();
    }
}
