package es.coru.andiag.welegends.models.wrapped.database.static_data

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.andiag_mvp.presenters.AIInterfaceLoaderPresenter
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.utils.CallbackSemaphore
import es.coru.andiag.welegends.models.utils.CallbackStaticData
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import es.coru.andiag.welegends.models.wrapped.database.converters.ConverterJsonArray
import es.coru.andiag.welegends.models.wrapped.database.converters.ConverterJsonObject
import es.coru.andiag.welegends.models.wrapped.database.static_data.generics.KeyInMapTypeAdapter
import java.io.Serializable

/**
 * Created by Canalejas on 12/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class Item : BaseModel(), Serializable, KeyInMapTypeAdapter {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @Expose(serialize = false, deserialize = false) @Unique @Column var riotId: Long? = null
    @Column var name: String? = null
    @Column var description: String? = null
    @Column var colloq: String? = null
    @Column var plaintext: String? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null
    @ForeignKey(tableClass = Gold::class) var gold: Gold? = null
    @Column(typeConverter = ConverterJsonArray::class) var into: JsonArray? = null
    @Column(typeConverter = ConverterJsonArray::class) var tags: JsonArray? = null
    @Column(typeConverter = ConverterJsonObject::class) var maps: JsonObject? = null
    @Column(typeConverter = ConverterJsonObject::class) var stats: JsonObject? = null


    override fun setKey(key: String) {
        this.riotId = key.toLong()
    }

    companion object {
        private val TAG: String = Item::class.java.simpleName

        fun loadFromServer(caller: AIInterfaceLoaderPresenter<*>, semaphore: CallbackSemaphore, version: String, locale: String) {
            val call = RestClient.getDdragonStaticData(version, locale).items()
            call.enqueue(CallbackStaticData(Item::class.java, locale, semaphore, caller,
                    Runnable {
                        Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(Item::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                        Item.loadFromServer(caller, semaphore, version, RestClient.DEFAULT_LOCALE)
                    }))
        }

    }

}
