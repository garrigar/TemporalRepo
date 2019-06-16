package uggroup.ugboard.models.online_model.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private Retrofit object;

    private static final RetrofitClient ourInstance = new RetrofitClient();

    public static Retrofit getInstance() {
        return ourInstance.object;
    }

    private RetrofitClient() {
        object = new Retrofit.Builder()
                .baseUrl("https://ugd-backend.herokuapp.com/cloud/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
