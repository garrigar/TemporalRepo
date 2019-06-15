package uggroup.ugboard.main_view.file_list;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uggroup.ugboard.LoggedInstance;

/*
Responsible for the controlling the list of the files in th application. No need in
changing this class, it's sufficient.
 */
public class FileListAdapter extends BaseAdapter implements LoggedInstance {

    private Context context;
    private List<String> fileNames;
    private final String defaultLogTag = "FileListAdapter";
    private String logTag = this.defaultLogTag;

    public FileListAdapter(Context context) {
        // Fill out the field with empty list for the first time in order to avoid exception
        this.fileNames = new ArrayList<String>();
        this.context = context;

        Log.i(this.logTag, "FileListAdapter is created");
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
        Log.i(this.logTag, "setFileNames call");
    }

    @Override
    public int getCount() {
        Log.i(this.logTag, "getCount call");
        return this.fileNames.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i(this.logTag, "getItem call, position " + String.valueOf(position));
        return this.fileNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(this.logTag, "getItemId call");
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(this.logTag, "getView call, position " + String.valueOf(position));
        FileView view = new FileViewImpl(this.context, null);
        view.setName(this.fileNames.get(position));
        return view.getRootView();
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
