package es.coru.andiag.welegends.models.database.static_data

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.andiag_mvp.interfaces.DataLoaderPresenter
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.api.RestClient
import es.coru.andiag.welegends.models.database.static_data.dbflow_converters.JsonArrayConverter
import es.coru.andiag.welegends.models.database.static_data.dbflow_converters.JsonObjectConverter
import es.coru.andiag.welegends.models.database.static_data.generics.KeyInMapTypeAdapter
import es.coru.andiag.welegends.models.utils.CallbackSemaphore
import es.coru.andiag.welegends.models.utils.StaticDataCallback
import java.io.Serializable

/**
 * Created by Canalejas on 13/12/2016.
 */
@Table(database = WeLegendsDatabase::class)
class Rune : BaseModel(), Serializable, KeyInMapTypeAdapter {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @Expose(serialize = false, deserialize = false) @Unique @Column var riotId: Long? = null
    @Column var name: String? = null
    @Column var description: String? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null
    @ForeignKey(tableClass = RuneType::class) var rune: RuneType? = null
    @Column var colloq: String? = null
    @Column var plaintext: String? = null
    @Column(typeConverter = JsonArrayConverter::class) var tags: JsonArray? = null
    @Column(typeConverter = JsonObjectConverter::class) var stats: JsonObject? = null

    override fun setKey(key: String) {
        this.riotId = key.toLong()
    }

    companion object {
        private val TAG: String = Rune::class.java.simpleName

        fun loadFromServer(caller: DataLoaderPresenter<*>, semaphore: CallbackSemaphore, version: String, locale: String) {
            val call = RestClient.getDdragonStaticData(version, locale).runes()
            call.enqueue(StaticDataCallback(Rune::class.java, locale, semaphore, caller,
                    Runnable {
                        Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(Rune::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                        ProfileIcon.loadFromServer(caller, semaphore, version, RestClient.DEFAULT_LOCALE)
                    }))
        }

    }

}
