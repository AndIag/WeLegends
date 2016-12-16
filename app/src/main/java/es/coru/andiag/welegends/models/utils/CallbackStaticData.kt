package es.coru.andiag.welegends.models.utils

import android.util.Log
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.andiag_mvp.presenters.AIInterfaceLoaderPresenter
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import es.coru.andiag.welegends.models.wrapped.api.dto.GenericStaticData
import es.coru.andiag.welegends.models.wrapped.database.static_data.generics.KeyInMapTypeAdapter
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
 * @constructor [clazz] class to save in database
 * @constructor [runnable] method to run when reload is required
 */
class CallbackStaticData<T : BaseModel>(
        private var clazz: Class<T>,
        private var locale: String,
        private var semaphore: CallbackSemaphore,
        private var caller: AIInterfaceLoaderPresenter<*>,
        private var runnable: Runnable)
    : Callback<GenericStaticData<String, T>> {

    private val TAG: String = CallbackStaticData::class.java.simpleName

    override fun onResponse(call: Call<GenericStaticData<String, T>>?, response: Response<GenericStaticData<String, T>>?) {
        if (!response!!.isSuccessful || response.body() == null) {
            if (locale != RestClient.DEFAULT_LOCALE) {
                runnable.run()
                return
            }
            Log.e(TAG, "ERROR: Loading %s - onResponse: %s".format(clazz.simpleName, response.errorBody().string()))
            Log.i(TAG, "Semaphore released with error for: %s".format(clazz.simpleName))
            semaphore.release(1)
            caller.onLoadError(null)
        } else {
            doAsync {
                try {
                    Log.i(TAG, "Loaded %s: %s".format(clazz.simpleName, response.body().data!!.keys))
                    if (clazz.interfaces.contains(KeyInMapTypeAdapter::class.java)) {
                        for ((k, v) in response.body().data!!) {
                            (v as KeyInMapTypeAdapter).setKey(k)
                        }
                    }
                    FlowManager.getModelAdapter(clazz).saveAll(response.body().data!!.values)
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating %s: %s".format(clazz.simpleName, e.message))
                    uiThread {
                        caller.onLoadError(null)
                    }
                } finally {
                    Log.i(TAG, "Semaphore released for: %s".format(clazz.simpleName))
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
        Log.e(TAG, "ERROR: Loading %s - onFailure: %s".format(clazz.simpleName, t!!.message))
        Log.i(TAG, "Semaphore released with error for: %s".format(clazz.simpleName))
        semaphore.release(1)
        caller.onLoadError(null)
    }

}