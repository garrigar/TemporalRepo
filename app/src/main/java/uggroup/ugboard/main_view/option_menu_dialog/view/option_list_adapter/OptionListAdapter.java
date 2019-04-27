package uggroup.ugboard.main_view.option_menu_dialog.view.option_list_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class OptionListAdapter extends BaseAdapter {

    private Context context;
    private List<String> options;

    public OptionListAdapter(Context context) {
        // Fill out the field with empty list for the first time in order to avoid exception
        this.context = context;
        this.options = new ArrayList<String>();
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public int getCount() {
        return this.options.size();
    }

    @Override
    public Object getItem(int position) {
        return this.options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OptionView view = new OptionViewImpl(this.context, null);
        view.setName(this.options.get(position));
        return view.getRootView();
    }
}
