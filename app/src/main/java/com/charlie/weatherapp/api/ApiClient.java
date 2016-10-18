package com.charlie.weatherapp.api;

import com.charlie.weatherapp.model.CitiesFindResponse;

import java.io.IOException;
import java.util.logging.Level;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class ApiClient {

    private static ApiClient sInstance = new ApiClient();

    private Retrofit retrofit;

    public static ApiClient getInstance() {
        return sInstance;
    }

    private ApiClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        //Adding interceptors
        httpClient.addInterceptor(logging);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("APPID", ApiConstant.WEATHER_API_KEY)
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
       retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    /**
     *  Returns a service for getting data from cities laid within definite circle that is specified
     *  by center point ('lat', 'lon') and expected number of cities ('cnt') around this point.
     * @param lat City geo location, latitude
     * @param lon City geo location, longitude
     * @param counter number of cities around the point that should be returned
     */
    public Call<CitiesFindResponse> getCityListService(String lat, String lon, int counter) {
        FindService service = retrofit.create(FindService.class);
        return service.listCities(lat,lon,counter);
    }
}
