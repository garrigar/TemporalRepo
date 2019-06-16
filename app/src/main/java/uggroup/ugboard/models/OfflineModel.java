package uggroup.ugboard.models;

import java.util.List;

public interface OfflineModel {
    List<Object> getFileList();
    void getAccess(Object file);
}
