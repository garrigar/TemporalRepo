package uggroup.ugboard.main_view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OptionsMenuDialogImpl extends DialogFragment implements OptionsMenuDialog {

    private Context context;
    private FragmentManager manager;
    private OptionsMenuView optionsMenuView;
    private List<String> options;

    static OptionsMenuDialog newInstance() {
        return new OptionsMenuDialogImpl();
    }

    @Override
    public void setOptionsList(List<String> options) {
        this.options = options;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = inflater.getContext();
        OptionsMenuView menuView = new OptionsMenuViewImpl(context, null);
        menuView.setOptionsList(this.options);
        return menuView.getRootView();
    }
}
