package uggroup.ugboard.models.offline_model;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.core.content.FileProvider;
import uggroup.ugboard.BuildConfig;
import uggroup.ugboard.presenter.OfflinePresenter;

public class OfflineModelImpl implements OfflineModel {

    private OfflinePresenter presenter;

    private ArrayList<String> fileNames;
    private ArrayList<String> fileTypes;

    private static final OfflineModelImpl ourInstance = new OfflineModelImpl();
    private final File workDir;

    public static OfflineModelImpl getInstance() {
        return ourInstance;
    }

    private OfflineModelImpl() {
        fileNames = new ArrayList<>();
        fileTypes = new ArrayList<>();
        workDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + LOCAL_FOLDER_NAME);
    }


    @Override
    public void setOfflinePresenter(OfflinePresenter offlinePresenter) {
        presenter = offlinePresenter;
    }

    @Override
    public void startExploring() {
        update();
    }

    @Override
    public void getAccess(String filename) {
        File targetFile = new File(workDir, filename);
        if (targetFile.exists()){
            // Get URI and MIME type of file
            Uri uri = FileProvider.getUriForFile(presenter.getContext(),
                    BuildConfig.APPLICATION_ID + ".fileProvider", targetFile);
            String mime = presenter.getContext().getContentResolver().getType(uri);

            // Open file with user selected app
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            presenter.getContext().startActivity(intent);

            /*Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType( FileProvider.getUriForFile(presenter.getContext(),
                    BuildConfig.APPLICATION_ID + ".fileProvider", targetFile),
                    fileTypes.get(fileNames.indexOf(filename)));
            presenter.getContext().startActivity(i);*/
        } else {
            presenter.showToast("File does not exist!", false);
        }
    }

    @Override
    public void delete(String filename) {
        File targetFile = new File(workDir, filename);
        if (targetFile.exists()){
            if (targetFile.delete()){
                presenter.showToast("Successfully deleted \"" + filename + "\"", false);
                update();
            } else {
                presenter.showToast("Deletion \"" + "\" failed", false);
            }
        } else {
            presenter.showToast("File does not exist!", false);
        }
    }

    @Override
    public void rename(String filename, String newName) {
        // NOT YET IMPLEMENTED
        presenter.showToast("Not yet implemented\n"+
                "GOTTA be renaming file \"" + filename + "\" to \"" + newName + "\" in future!", true);
    }

    @Override
    public void update() {
        presenter.setUpdatingState(true);
        refresh();
        presenter.updateContentsLocal(fileNames, fileTypes);
        presenter.setUpdatingState(false);
    }

    private void refresh(){
        fileTypes.clear();
        fileNames.clear();
        File[] files = workDir.listFiles();
        Arrays.stream(files).sorted((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified())).forEach(file -> {
            fileNames.add(file.getName());
            fileTypes.add(getMimeType(file.getName()));
        });
    }

    private static String getMimeType(String filename) {
        String type = "*/*";
        String extension = /*MimeTypeMap.getFileExtensionFromUrl(url)*/
                (filename.lastIndexOf('.') != -1 ? filename.substring(filename.lastIndexOf(".") + 1) : null);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
