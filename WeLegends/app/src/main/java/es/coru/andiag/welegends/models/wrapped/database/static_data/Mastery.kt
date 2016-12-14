package es.coru.andiag.welegends.models.wrapped.database.static_data

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.andiag_mvp.interfaces.DataLoaderPresenter
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.utils.CallbackSemaphore
import es.coru.andiag.welegends.models.utils.StaticDataCallback
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import es.coru.andiag.welegends.models.wrapped.database.static_data.dbflow_converters.JsonArrayConverter
import java.io.Serializable

/**
 * Created by Canalejas on 13/12/2016.
 */
@Table(database = WeLegendsDatabase::class)
class Mastery : BaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Unique @Column var riotId: Int? = null
    @Unique @Column var name: String? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null
    @Column var ranks: Int? = null
    @Column var prereq: String? = null
    @Column(typeConverter = JsonArrayConverter::class) var description: JsonArray? = null

    companion object {
        private val TAG: String = Mastery::class.java.simpleName

        fun loadFromServer(caller: DataLoaderPresenter<*>, semaphore: CallbackSemaphore, version: String, locale: String) {
            val call = RestClient.getDdragonStaticData(version, locale).masteries()
            call.enqueue(StaticDataCallback(Mastery::class.java, locale, semaphore, caller,
                    Runnable {
                        Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(Mastery::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                        ProfileIcon.loadFromServer(caller, semaphore, version, RestClient.DEFAULT_LOCALE)
                    }))
        }

    }

}
