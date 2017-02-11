package com.andiag.welegends.models.api

import com.andiag.welegends.BuildConfig
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

    private val WELEGENDS_PROXY_ENDPOINT = BuildConfig.AndIagApi
    private val DDRAGON_DATA_ENDPOINT = "http://ddragon.leagueoflegends.com/cdn/"

    private var REST_CLIENT: API.Riot? = null
    private var VERSION_CLIENT: API.Version? = null

    private var STATIC_CLIENT: API.Static? = null
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

    fun getWeLegendsData(): API.Riot {
        if (REST_CLIENT == null) {
            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(12, TimeUnit.SECONDS)
                    .readTimeout(12, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(getLoggingLevel())

            // Add api key to all requests if required
            if (!BuildConfig.ApiKey.isEmpty()) {
                okHttpClient.addInterceptor({ chain ->
                    var request = chain!!.request()
                    val url = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.ApiKey).build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                })
            }

            val retrofit = Retrofit.Builder()
                    .baseUrl(WELEGENDS_PROXY_ENDPOINT)
                    .client(okHttpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            REST_CLIENT = retrofit.create(API.Riot::class.java)
        }
        return REST_CLIENT!!
    }

    fun getDdragonStaticData(version: String, locale: String): API.Static {
        val endpoint = "$DDRAGON_DATA_ENDPOINT$version/data/$locale/"
        if (STATIC_CLIENT == null) {
            staticEndpoint = endpoint
            val retrofit = Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            STATIC_CLIENT = retrofit.create(API.Static::class.java)
        } else if (endpoint != staticEndpoint) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            STATIC_CLIENT = retrofit.create(API.Static::class.java)
        }

        return STATIC_CLIENT!!
    }

    fun getVersion(): API.Version {
        if (VERSION_CLIENT == null) {
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://ddragon.leagueoflegends.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            VERSION_CLIENT = retrofit.create(API.Version::class.java)
        }
        return VERSION_CLIENT!!
    }

    fun getProfileIconEndpoint(version: String): String {
        return DDRAGON_DATA_ENDPOINT + version + "/img/profileicon/"
    }

    val loadingImgEndpoint: String
        get() = DDRAGON_DATA_ENDPOINT + "img/champion/loading/"

    val splashImgEndpoint: String
        get() = DDRAGON_DATA_ENDPOINT + "img/champion/splash/"

}
