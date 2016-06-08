package andiag.coru.es.welegends.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import andiag.coru.es.welegends.rest.utils.SummonerTypeAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Canalejas on 31/05/2016.
 */
public class RestClient {

    private static final String WELEGENDS_PROXY_ENDPOINT = "http://andiag-prod.apigee.net/v1/welegends/";
    private static final String STATIC_DATA_ENDPOINT = "https://ddragon.leagueoflegends.com/cdn/";
    private static Api REST_CLIENT;

    static {
        setupRestClient();
    }

    private RestClient() {}

    public static Api get() {
        return REST_CLIENT;
    }

    public static Api get(String name){
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new SummonerTypeAdapterFactory(name))
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WELEGENDS_PROXY_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(Api.class);
    }

    public static ApiStatic getStatic(String version, String locale){

        String endpoint = STATIC_DATA_ENDPOINT+version+"/data/"+locale+"/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiStatic.class);
    }

    private static void setupRestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WELEGENDS_PROXY_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        REST_CLIENT = retrofit.create(Api.class);
    }



}
