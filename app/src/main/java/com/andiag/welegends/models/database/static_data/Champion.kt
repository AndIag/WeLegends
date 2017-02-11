package com.andiag.welegends.models.database.static_data

import android.util.Log
import com.andiag.commons.interfaces.presenters.AIInterfaceErrorHandlerPresenter
import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.utils.CallbackSemaphore
import com.andiag.welegends.models.utils.CallbackStaticData
import com.andiag.welegends.models.utils.converters.OrmBaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class Champion : OrmBaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Unique @Column var riotId: String? = null
    @Expose(serialize = false, deserialize = false) @Unique @Column var name: String? = riotId
    @Column var version: String? = null
    @Unique @Column var key: String? = null
    @Column var title: String? = null
    @Column var blurb: String? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null
    @Column var partype: String? = null

    companion object {
        private val TAG: String = Champion::class.java.simpleName

        fun loadFromServer(caller: AIInterfaceErrorHandlerPresenter, semaphore: CallbackSemaphore, version: String, locale: String) {
            val call = RestClient.getDdragonStaticData(version, locale).champions()
            call.enqueue(CallbackStaticData<Champion>(locale, semaphore, caller, Runnable {
                Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(Champion::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                loadFromServer(caller, semaphore, version, RestClient.DEFAULT_LOCALE)
            }))
        }

    }

}
