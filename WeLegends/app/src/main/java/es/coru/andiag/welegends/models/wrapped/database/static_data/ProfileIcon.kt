package es.coru.andiag.welegends.models.wrapped.database.static_data

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.andiag_mvp.presenters.AIInterfaceLoaderPresenter
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.utils.CallbackSemaphore
import es.coru.andiag.welegends.models.utils.CallbackStaticData
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import java.io.Serializable

/**
 * Created by Canalejas on 09/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class ProfileIcon() : BaseModel(), Serializable {
    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Unique @Column var riotId: Long? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null

    companion object {
        private val TAG: String = ProfileIcon::class.java.simpleName

        fun loadFromServer(caller: AIInterfaceLoaderPresenter<*>, semaphore: CallbackSemaphore, version: String, locale: String) {
            val call = RestClient.getDdragonStaticData(version, locale).profileIcons()
            call.enqueue(CallbackStaticData(ProfileIcon::class.java, locale, semaphore, caller,
                    Runnable {
                        Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(ProfileIcon::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                        loadFromServer(caller, semaphore, version, RestClient.DEFAULT_LOCALE)
                    }))
        }

    }

}
