package com.andiag.welegends.models.entities.static_data

import android.util.Log
import com.andiag.commons.interfaces.AIInterfaceErrorHandlerPresenter
import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.utils.CallbackSemaphore
import com.andiag.welegends.models.utils.CallbackStaticData
import com.andiag.welegends.models.utils.converters.ConverterJsonArray
import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
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
    @Column(typeConverter = ConverterJsonArray::class) var description: JsonArray? = null

    companion object {
        private val TAG: String = Mastery::class.java.simpleName

        fun loadFromServer(caller: AIInterfaceErrorHandlerPresenter, semaphore: CallbackSemaphore, version: String, locale: String) {
            val call = RestClient.getDdragonStaticData(version, locale).masteries()
            call.enqueue(CallbackStaticData<Mastery>(locale, semaphore, caller, Runnable {
                Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(Mastery::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                loadFromServer(caller, semaphore, version, RestClient.DEFAULT_LOCALE)
            }))
        }

    }

}
