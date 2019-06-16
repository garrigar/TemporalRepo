package uggroup.ugboard.fragments.file_manager_view.file_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uggroup.ugboard.R;


/*
Represents how file actually looks. Can be modified in the future for different
types of files: images, folders, etc.
 */
public class FileViewImpl implements FileView {
    private Context context;
    private View rootView;
    private TextView fileName;

    FileViewImpl(Context context, ViewGroup container) {
        this.context = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.file_item, container, false);
        this.fileName = this.rootView.findViewById(R.id.fileNameTxt);
    }

    @Override
    public void setName(String name) {
        this.fileName.setText(name);
    }

    @Override
    public View getRootView() {
        return this.rootView;
    }
}
