package andiag.coru.es.welegends.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import andiag.coru.es.welegends.rest.utils.SummonerTypeAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Canalejas on 31/05/2016.
 */
public class ApiClient {

    private static Api REST_CLIENT;
    private static final String WELEGENDS_PROXY = "http://andiag-prod.apigee.net/v1/welegends/";

    static {
        setupRestClient();
    }

    private ApiClient() {}

    public static Api get() {
        return REST_CLIENT;
    }

    public static Api getWithTypeAdapter(String name){
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new SummonerTypeAdapterFactory(name))
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WELEGENDS_PROXY)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(Api.class);
    }

    private static void setupRestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WELEGENDS_PROXY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        REST_CLIENT = retrofit.create(Api.class);
    }



}
