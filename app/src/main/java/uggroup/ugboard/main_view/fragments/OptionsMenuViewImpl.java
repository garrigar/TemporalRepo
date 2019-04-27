package uggroup.ugboard.main_view.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import uggroup.ugboard.R;

public class OptionsMenuViewImpl implements OptionsMenuView {
    private Context context;
    private View rootView;
    private ListView optionsListView;
    private List<String> options;

    public OptionsMenuViewImpl(Context context, ViewGroup parent) {
        this.rootView = LayoutInflater.from(context).inflate(R.layout.option_menu_fragment, parent);
        this.context = context;
        this.optionsListView = this.rootView.findViewById(R.id.optionsListView);
    }

    @Override
    public void setOptionsList(List<String> options) {

    }

    @Override
    public void setOnOptionClickListener(OptionsMenuDialog.OnOptionClickListener listener) {

    }

    @Override
    public View getRootView() {
        return this.rootView;
    }


}
