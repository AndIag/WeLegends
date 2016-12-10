package es.coru.andiag.welegends.models.entities.database

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.Model
import es.coru.andiag.welegends.WeLegendsDatabase
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class Champion() : BaseModel(), Serializable, Model {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Column var name: String? = null
    @Column var version: String? = null
    @Unique @Column var key: String? = null
    @Column var title: String? = null
    @Column var blurb: String? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null
    @Column var partype: String? = null

}
