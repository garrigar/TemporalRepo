package uggroup.ugboard.models.online_model.retrofit;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uggroup.ugboard.models.online_model.FileItem;

public interface IUGDBackend {

    String BASE_URL = "https://ugd-backend.herokuapp.com/cloud/";

    String DOWNLOAD_BASE_URL = BASE_URL + "download?id=";

    String UPLOAD_BASE_URL = BASE_URL + "upload";

    @GET("data")
    Observable<List<FileItem>> getFileList();

    @DELETE("data")
    Call<ResponseBody> deleteFileItem(@Query("id") String id);
}
