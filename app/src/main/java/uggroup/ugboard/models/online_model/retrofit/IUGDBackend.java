package uggroup.ugboard.models.online_model.retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import uggroup.ugboard.models.online_model.FileItem;

public interface IUGDBackend {

    String BASE_URL = "https://ugd-backend.herokuapp.com/cloud/";

    @GET("data")
    Observable<List<FileItem>> getFileList();

    String DOWNLOAD_BASE_URL = BASE_URL + "data/";

}
