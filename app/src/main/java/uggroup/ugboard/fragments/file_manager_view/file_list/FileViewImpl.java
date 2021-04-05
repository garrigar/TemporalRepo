package uggroup.ugboard.fragments.file_manager_view.file_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private ImageView icon;

    FileViewImpl(Context context, ViewGroup container) {
        this.context = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.file_item, container, false);
        this.fileName = this.rootView.findViewById(R.id.fileNameTxt);
        this.icon = this.rootView.findViewById(R.id.fileIcon);
    }

    @Override
    public void setName(String name) {
        this.fileName.setText(name);
    }

    @Override
    public void setType(String type) {
        if (type == null){
            this.icon.setImageResource(R.drawable.icon_file);
            return;
        }
        switch (type) {
            case "dir":
                this.icon.setImageResource(R.drawable.icon_folder);
                break;
            case "image/jpeg": case "image/png":
                this.icon.setImageResource(R.drawable.icon_image);
                break;
            case "video/mp4":
                this.icon.setImageResource(R.drawable.icon_video);
                break;
            case "application/pdf":
                this.icon.setImageResource(R.drawable.icon_pdf);
                break;
            case "text/plain":
                this.icon.setImageResource(R.drawable.icon_text);
                break;
            default:
                this.icon.setImageResource(R.drawable.icon_file);
                break;
        }
    }

    @Override
    public View getRootView() {
        return this.rootView;
    }
}
