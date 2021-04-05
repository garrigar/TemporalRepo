package uggroup.ugboard.presenter;

import java.util.List;

public interface OfflinePresenter extends BaseFileManagerPresenter {

    void updateContentsLocal(List<String> fileNames, List<String> fileTypes);

}
