package hu.bme.aut.debter.data.api;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DebterAPI {
    private static final String API = "https://debter-app.herokuapp.com";
    private static DebterAPI instance;
    private APIRoutes debter;

    private DebterAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://debter-app.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        debter = retrofit.create(APIRoutes.class);
    }

    public static DebterAPI getInstance() {
        if (instance == null)
            instance = new DebterAPI();
        return instance;
    }

    public APIRoutes getDebter() {
        return debter;
    }


    public static class DebterCallback<T extends APIRoutes.Response> implements Callback<T> {
        private OnResponse<T> callback;
        private OnError<T> error;

        public DebterCallback(OnResponse<T> callback) {
            this.callback = callback;
        }

        public DebterCallback(OnResponse<T> callback, OnError<T> error) {
            this.callback = callback;
            this.error = error;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.code() == 200)
                callback.onResponse(call, response);
            else {
                handleError(null, null);
                Log.d("develop", call.request().toString());
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable throwable) {
            handleError(call, throwable);
        }

        private void handleError(Call<T> call, Throwable throwable) {
            if (error != null)
                error.onError(call, throwable);
            Log.e("develop", throwable == null ? "error" : throwable.getMessage());
        }

        public interface OnResponse<T> {
            void onResponse(Call<T> call, Response<T> response);
        }

        public interface OnError<T> {
            void onError(Call<T> call, Throwable throwable);
        }
    }
}
