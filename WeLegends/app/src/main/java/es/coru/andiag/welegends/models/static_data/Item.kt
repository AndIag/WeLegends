package es.coru.andiag.welegends.models.static_data

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.static_data.generics.KeyInMapTypeAdapter
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
    @Column var into: JsonArray? = null
    @Column var tags: JsonArray? = null
    @Column var maps: JsonObject? = null
    @Column var stats: JsonObject? = null


    override fun setKey(key: String) {
        this.riotId = key.toLong()
    }

}
