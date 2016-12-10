package es.coru.andiag.welegends.models.rest

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import es.coru.andiag.welegends.BuildConfig
import es.coru.andiag.welegends.common.utils.SummonerTypeAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Canalejas on 08/12/2016.
 */

object RestClient {

    val DEFAULT_LOCALE = "en_GB"

    private val WELEGENDS_PROXY_ENDPOINT = "http://andiag-prod.apigee.net/v1/welegends/"
    private val DDRAGON_DATA_ENDPOINT = "https://ddragon.leagueoflegends.com/cdn/"
    private val STATIC_DATA_ENDPOINT = "https://global.api.pvp.net/api/lol/static-data/"

    private var REST_CLIENT: API? = null

    private var STATIC_CLIENT: Static? = null
    private var staticEndpoint: String? = null

    private fun getLoggingLevel(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        when (BuildConfig.HttpLoggingInterceptorLevel) {
            "NONE" -> {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }
            "BASIC" -> {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            }
            "HEADERS" -> {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            }
            "BODY" -> {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            else -> {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }
        return httpLoggingInterceptor
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            isAvailable = true
        }

        return isAvailable
    }

    fun getWeLegendsData(): API {
        if (REST_CLIENT == null) {
            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(12, TimeUnit.SECONDS)
                    .readTimeout(12, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(getLoggingLevel())
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(WELEGENDS_PROXY_ENDPOINT)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            REST_CLIENT = retrofit.create(API::class.java)
        }
        return REST_CLIENT!!
    }

    fun getWeLegendsData(name: String): API {
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(SummonerTypeAdapterFactory(name))
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(getLoggingLevel())
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(WELEGENDS_PROXY_ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit.create(API::class.java)
    }

    fun getDdragonStaticData(version: String, locale: String): Static {
        val endpoint = "$DDRAGON_DATA_ENDPOINT$version/data/$locale/"
        if (STATIC_CLIENT == null) {
            staticEndpoint = endpoint
            val retrofit = Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            STATIC_CLIENT = retrofit.create(Static::class.java)
        } else if (endpoint != staticEndpoint) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            STATIC_CLIENT = retrofit.create(Static::class.java)
        }

        return STATIC_CLIENT!!
    }

    fun getProfileIconEndpoint(version: String): String {
        return DDRAGON_DATA_ENDPOINT + version + "/img/profileicon/"
    }

    val loadingImgEndpoint: String
        get() = DDRAGON_DATA_ENDPOINT + "img/champion/loading/"

    val splashImgEndpoint: String
        get() = DDRAGON_DATA_ENDPOINT + "img/champion/splash/"

}
