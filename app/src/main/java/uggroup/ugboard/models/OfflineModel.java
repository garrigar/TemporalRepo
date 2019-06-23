package uggroup.ugboard.models;

import java.util.List;

public interface OfflineModel {
    List<String> getFileList();
    void getAccess(String filename);
}
