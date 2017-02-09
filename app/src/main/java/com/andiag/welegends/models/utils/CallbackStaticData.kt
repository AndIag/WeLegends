package com.andiag.welegends.models.utils

import android.util.Log
import com.andiag.commons.interfaces.presenters.AIInterfaceErrorHandlerPresenter
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.database.static_data.generics.KeyInMapTypeAdapter
import com.andiag.welegends.models.utils.dto.GenericStaticData
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.structure.BaseModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Canalejas on 11/12/2016.
 * Generic callback to simplify all StaticData Requests
 * @constructor [semaphore] used to allow concurrency
 * @constructor [locale] needed to know if a reload to {@link RestClient.DEFAULT_LOCALE} is required
 * @constructor [caller] called when callback process ends
 * @constructor [runnable] method to run when reload is required
 */
class CallbackStaticData<T : BaseModel>(
        private var locale: String,
        private var semaphore: CallbackSemaphore,
        private var caller: AIInterfaceErrorHandlerPresenter,
        private var runnable: Runnable)
    : Callback<GenericStaticData<String, T>> {

    private val TAG: String = CallbackStaticData::class.java.simpleName

    override fun onResponse(call: Call<GenericStaticData<String, T>>?, response: Response<GenericStaticData<String, T>>?) {
        if (!response!!.isSuccessful || response.body() == null) {
            if (locale != RestClient.DEFAULT_LOCALE) {
                runnable.run()
                return
            }
            Log.e(TAG, "ERROR: onResponse: %s".format(response.errorBody().string()))
            Log.i(TAG, "Semaphore released with errors")
            semaphore.release(1)
            caller.onLoadError(null)
        } else {
            doAsync {
                try {
                    val clazz: Class<T> = response.body().data!!.values.first().javaClass
                    Log.i(TAG, "Loaded %s: %s".format(clazz.simpleName, response.body().data!!.keys))
                    if (clazz.interfaces.contains(KeyInMapTypeAdapter::class.java)) {
                        for ((k, v) in response.body().data!!) {
                            (v as KeyInMapTypeAdapter).setKey(k)
                        }
                    }
                    FlowManager.getModelAdapter(clazz).saveAll(response.body().data!!.values)
                } catch (e: Exception) {
                    Log.e(TAG, "Error saving %s".format(e.message))
                    uiThread {
                        caller.onLoadError(null)
                    }
                } finally {
                    Log.i(TAG, "Semaphore released")
                    semaphore.release(1)
                }
            }
        }
    }

    override fun onFailure(call: Call<GenericStaticData<String, T>>?, t: Throwable?) {
        if (locale != RestClient.DEFAULT_LOCALE) {
            runnable.run()
            return
        }
        Log.e(TAG, "ERROR: onFailure: %s".format(t!!.message))
        Log.i(TAG, "Semaphore released with errors")
        semaphore.release(1)
        caller.onLoadError(t.message)
    }

}