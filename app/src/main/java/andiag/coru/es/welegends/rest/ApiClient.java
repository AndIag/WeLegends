package andiag.coru.es.welegends.rest;

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

    private static void setupRestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WELEGENDS_PROXY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        REST_CLIENT = retrofit.create(Api.class);
    }

}
