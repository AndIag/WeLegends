package es.coru.andiag.welegends.models.database

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.welegends.WeLegendsDatabase
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
open class Champion() : BaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
        get set
    @SerializedName("id") @Column var name: String? = null
        get set
    @Column var version: String? = null
        get set
    @Unique @Column var key: String? = null
        get set
    @Column var title: String? = null
        get set
    @Column var blurb: String? = null
        get set
    @ForeignKey(tableClass = Image::class) var image: Image? = null
        get set
    @Column var partype: String? = null
        get set

    override fun toString(): String {
        return "%d -> %s".format(mid, name)
    }
}
