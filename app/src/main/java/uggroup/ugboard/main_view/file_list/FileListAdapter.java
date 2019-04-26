package uggroup.ugboard.main_view.file_list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Set;

public class FileListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> fileNames;
    private Set<Integer> pickedFiles;

    public FileListAdapter(Context context) {
        // Fill out the field with empty list for the first time in order to avoid exception
        this.fileNames = new ArrayList<String>();
        this.context = context;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }

    @Override
    public int getCount() {
        return this.fileNames.size();
    }

    @Override
    public Object getItem(int position) {
        return this.fileNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileView view = new FileViewImpl(this.context, null);
        view.setName(this.fileNames.get(position));
        return view.getRootView();
    }
}
