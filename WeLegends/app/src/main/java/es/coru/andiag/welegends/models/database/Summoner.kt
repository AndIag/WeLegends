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
class Summoner() : BaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Unique @Column var riotId: Long? = null
    @Column var name: String? = null
    @Column var region: String? = null
    @SerializedName("profileIconId") @ForeignKey(tableClass = ProfileIcon::class) var profileIcon: ProfileIcon? = null
    @Column var lastUpdate: Long? = null
    @Column var summonerLevel: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val summoner = other as Summoner?

        return riotId == summoner!!.riotId && profileIcon!!.riotId == summoner.profileIcon!!.riotId
                && summonerLevel == summoner.summonerLevel
                && name == summoner.name && region == summoner.region
    }

    override fun hashCode(): Int {
        var result = (riotId!! xor riotId!!.ushr(32)).toInt()
        result = 31 * result + name!!.hashCode()
        result = 31 * result + region!!.hashCode()
        result = 31 * result + (profileIcon!!.riotId!! xor profileIcon!!.riotId!!.ushr(32)).toInt()
        result = 31 * result + summonerLevel
        return result
    }
}
