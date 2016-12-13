package es.coru.andiag.welegends.common.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.utils.ConverterStringList
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
    @Column(typeConverter = ConverterStringList::class) var description: Array<String>? = null

}
