package com.andiag.welegends.models.database.static_data

import android.util.Log
import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.common.utils.CallbackData
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.database.converters.ConverterJsonArray
import com.andiag.welegends.models.database.static_data.generics.OrmBaseModel
import com.andiag.welegends.models.database.static_data.utils.CallbackSemaphore
import com.andiag.welegends.models.database.static_data.utils.CallbackStaticData
import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import java.io.Serializable

/**
 * Created by Canalejas on 13/12/2016.
 */
@Table(database = WeLegendsDatabase::class)
class Mastery : OrmBaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Unique @Column var riotId: Int? = null
    @Unique @Column var name: String? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null
    @Column var ranks: Int? = null
    @Column var prereq: String? = null
    @Column(typeConverter = ConverterJsonArray::class) var description: JsonArray? = null

    companion object {
        private val TAG: String = Mastery::class.java.simpleName

        fun loadFromServer(callback: CallbackData<*>, semaphore: CallbackSemaphore, version: String, locale: String) {
            val call = RestClient.getDdragonStaticData(version, locale).masteries()
            call.enqueue(CallbackStaticData<Mastery>(locale, semaphore, callback, Runnable {
                Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(Mastery::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                loadFromServer(callback, semaphore, version, RestClient.DEFAULT_LOCALE)
            }))
        }

    }

}
