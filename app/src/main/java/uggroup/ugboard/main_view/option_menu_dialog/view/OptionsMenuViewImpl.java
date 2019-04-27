package uggroup.ugboard.main_view.option_menu_dialog.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import uggroup.ugboard.R;
import uggroup.ugboard.main_view.option_menu_dialog.OptionsMenuDialog;
import uggroup.ugboard.main_view.option_menu_dialog.view.option_list_adapter.OptionListAdapter;

public class OptionsMenuViewImpl implements OptionsMenuView {
    private Context context;
    private View rootView;
    private ListView optionsListView;
    private OptionListAdapter optionListAdapter;

    public OptionsMenuViewImpl(Context context, ViewGroup parent) {
        this.rootView = LayoutInflater.from(context).inflate(R.layout.option_menu_fragment, parent);
        this.context = context;
        this.optionsListView = this.rootView.findViewById(R.id.optionsListView);
        this.optionListAdapter = new OptionListAdapter(context);
    }

    @Override
    public void setOptionsList(List<String> options) {
        this.optionListAdapter.setOptions(options);
    }

    @Override
    public void setOnOptionClickListener(final OptionsMenuDialog.OnOptionClickListener listener) {
        this.optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView optionNameTxt = view.findViewById(R.id.optionNameTxt);
                listener.onOptionClicked(optionNameTxt.getText().toString());
            }
        });
    }

    @Override
    public View getRootView() {
        return this.rootView;
    }


}
