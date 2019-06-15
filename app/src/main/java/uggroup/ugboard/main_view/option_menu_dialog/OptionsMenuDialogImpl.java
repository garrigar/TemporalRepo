package uggroup.ugboard.main_view.option_menu_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uggroup.ugboard.LoggedInstance;


public class OptionsMenuDialogImpl extends DialogFragment implements OptionsMenuDialog {

    static OptionsMenuDialog newInstance() {
        return new OptionsMenuDialogImpl();
    }

    private String defaultLogTag = "OptionsMenuDialogImpl";
    private String logTag = this.defaultLogTag;
    private List<String> options;
    private String optionsTitle;
    private final String defaultOptionsTitle = "Options";
    private OnOptionClickListener onOptionClickListener;
    private AlertDialog dialog;

    public OptionsMenuDialogImpl() {
        this.options = new ArrayList<>();
        this.optionsTitle = this.defaultOptionsTitle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(this.dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(this.optionsTitle);

            // For more convenient usage in the anonymous DialogInterface.OnClickListener
            final List<String> optionsList = this.options;
            final OnOptionClickListener listener = this.onOptionClickListener;

            // Convert options strings to char sequences as it required by setItems method
            CharSequence[] charSequencesOptions = new CharSequence[this.options.size()];
            this.options.toArray(charSequencesOptions);

            // Create anonymous DialogInterface.OnClickListener and delegate
            // all the responsibility to the OnOptionClickListener
            builder.setItems(charSequencesOptions,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i(OptionsMenuDialogImpl.this.logTag,
                                "options item in dialog is clicked");

                            if(listener == null) {
                                Log.w(OptionsMenuDialogImpl.this.logTag,
                                        "onOptionClickListener is null");
                                return;
                            }

                            listener.onOptionClicked(optionsList.get(which));
                            Log.i(OptionsMenuDialogImpl.this.logTag+
                                    ".OnOptionClickListener",
                                    "onOptionClicked call");
                        }
                    });
            this.dialog = builder.create();
            Log.i(this.logTag, "options dialog created");
        }

        Log.i(this.logTag, "onCreateDialog call");
        return this.dialog;
    }

    @Override
    public void setOptionsList(List<String> options) {
        Log.i(this.logTag, "setOptionsList call");
        this.options = options;
    }

    @Override
    public void setOptionsTitle(String title) {
        Log.i(this.logTag, "setOptionsTitle call");
        this.optionsTitle = title;
    }

    @Override
    public void setOnOptionClickListener(OnOptionClickListener listener) {
        Log.i(this.logTag, "setOnOptionClickListener call");
        this.onOptionClickListener = listener;
    }

    @Override
    public void showOptionsMenu(FragmentManager manager, String tag) {
        Log.i(this.logTag, "showOptionsMenu call");
        this.show(manager, tag);
    }

    @Override
    public String getDefaultLogTag() {
        Log.i(this.logTag, "getDefaultLogTag call");
        return this.defaultLogTag;
    }

    @Override
    public void setLogTag(String tag) {
        this.logTag = tag;
        Log.i(this.logTag, "setLogTag call");
    }
}
