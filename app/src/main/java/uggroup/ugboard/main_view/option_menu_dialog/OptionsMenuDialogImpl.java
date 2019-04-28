package uggroup.ugboard.main_view.option_menu_dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uggroup.ugboard.R;
import uggroup.ugboard.main_view.option_menu_dialog.view.OptionsMenuView;
import uggroup.ugboard.main_view.option_menu_dialog.view.OptionsMenuViewImpl;

public class OptionsMenuDialogImpl extends DialogFragment implements OptionsMenuDialog {

    private Context context;
    private FragmentManager manager;
    private OptionsMenuView optionsMenuView;
    private List<String> options;

    static OptionsMenuDialog newInstance() {
        return new OptionsMenuDialogImpl();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Options");

        CharSequence[] a = new CharSequence[this.options.size()];
        this.options.toArray(a);

        builder.setItems(a, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });

        return builder.create();
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

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(this.optionsMenuView == null) {
            this.optionsMenuView = new OptionsMenuViewImpl(inflater, container);
            this.optionsMenuView.setOptionsList(this.options);
        }
        return this.optionsMenuView.getRootView();
        TimePickerDialog
    }*/
}
