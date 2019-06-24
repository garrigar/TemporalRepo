package uggroup.ugboard.models.offline_model;

import java.util.List;

public interface OfflineModel {

    String LOCAL_FOLDER_NAME = "UGBoard"; // keep in mind also an entry in file_paths.xml

    List<String> getFileList();
    void getAccess(String filename);
}
