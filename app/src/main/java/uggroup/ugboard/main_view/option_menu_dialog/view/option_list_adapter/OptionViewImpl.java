package uggroup.ugboard.main_view.option_menu_dialog.view.option_list_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import uggroup.ugboard.R;

public class OptionViewImpl implements OptionView {

    Context context;
    private View rootView;
    private TextView optionNameTxt;

    public OptionViewImpl(Context context, ViewGroup parent) {
        this.rootView = LayoutInflater.from(context).inflate(R.layout.option_item, parent);
        this.context = context;
        this.optionNameTxt = this.rootView.findViewById(R.id.optionNameTxt);
    }

    @Override
    public void setName(String name) {
        this.optionNameTxt.setText(name);
    }

    @Override
    public View getRootView() {
        return this.rootView;
    }
}
