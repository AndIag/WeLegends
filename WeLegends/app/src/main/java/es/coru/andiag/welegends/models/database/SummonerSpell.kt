package es.coru.andiag.welegends.models.database

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.welegends.WeLegendsDatabase
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
//    @Column var range: List<Int>? = null
//    @Column var modes: List<String>? = null
//    @Column var vars: List<Any>? = null
//    @Column var effectBurn: List<Any>? = null
//    @Column var effect: List<Any>? = null
//    @Column var cost: List<Int>? = null
//    @Column var cooldown: List<Int>? = null

}