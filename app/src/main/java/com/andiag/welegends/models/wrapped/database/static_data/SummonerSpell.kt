package com.andiag.welegends.models.wrapped.database.static_data

import android.util.Log
import com.andiag.libraryutils.interfaces.AIInterfaceErrorHandlerPresenter
import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.models.utils.CallbackSemaphore
import com.andiag.welegends.models.utils.CallbackStaticData
import com.andiag.welegends.models.wrapped.api.RestClient
import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

/**
 * Created by Canalejas on 12/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class SummonerSpell : BaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Unique @Column var riotId: String? = null
    @Unique @Column var name: String? = riotId
    @Column var description: String? = null
    @Column var tooltip: String? = null
    @Column var maxrank: Int? = null
    @Column var cooldownBurn: String? = null
    @Column var costBurn: String? = null
    @Column var key: String? = null
    @Column var summonerLevel: Int? = null
    @Column var costType: String? = null
    @Column var maxammo: String? = null
    @Column var rangeBurn: String? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null
    @Column var resource: String? = null
    @Column var range: JsonArray? = null
    @Column var modes: JsonArray? = null
    @Column var cost: JsonArray? = null
    @Column var cooldown: JsonArray? = null

    companion object {
        private val TAG: String = SummonerSpell::class.java.simpleName

        fun loadFromServer(caller: AIInterfaceErrorHandlerPresenter, semaphore: CallbackSemaphore, version: String, locale: String) {
            val call = RestClient.getDdragonStaticData(version, locale).summonerSpells()
            call.enqueue(CallbackStaticData<SummonerSpell>(locale, semaphore, caller, Runnable {
                Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(SummonerSpell::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                loadFromServer(caller, semaphore, version, RestClient.DEFAULT_LOCALE)
            }))
        }

    }

}