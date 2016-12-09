package es.coru.andiag.welegends.models.entities.database

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.Model
import es.coru.andiag.welegends.common.WeLegendsDatabase
import java.io.Serializable
import java.security.Timestamp

/**
 * Created by Canalejas on 08/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class Summoner : BaseModel(), Serializable, Model {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @Unique @Column var id: Long? = null
    @Column var name: String? = null
    @Column var region: String? = null
    @SerializedName("profileIconId") @ForeignKey(tableClass = ProfileIcon::class) var profileIcon: ProfileIcon? = null
    @Column var lastUpdate: Timestamp? = null
    @Column var summonerLevel: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val summoner = other as Summoner?

        return id == summoner!!.id && profileIcon!!.id == summoner.profileIcon!!.id
                && summonerLevel == summoner.summonerLevel
                && name == summoner.name && region == summoner.region
    }

    override fun hashCode(): Int {
        var result = (id!! xor id!!.ushr(32)).toInt()
        result = 31 * result + name!!.hashCode()
        result = 31 * result + region!!.hashCode()
        result = 31 * result + (profileIcon!!.id!! xor profileIcon!!.id!!.ushr(32)).toInt()
        result = 31 * result + summonerLevel
        return result
    }
}
