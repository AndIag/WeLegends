package es.coru.andiag.welegends.models.entities.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.orm.SugarRecord
import com.orm.dsl.Unique
import java.io.Serializable
import java.security.Timestamp

/**
 * Created by Canalejas on 08/12/2016.
 */

class Summoner : SugarRecord(), Serializable {

    @SerializedName("id") @Unique @Expose var riotId: Long = 0
    @Expose var name: String? = null
    @Expose var region: String? = null
    @Expose var profileIconId: Long = 0
    @Expose var lastUpdate: Timestamp? = null
    @Expose var summonerLevel: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val summoner = other as Summoner?

        return id == summoner!!.id && profileIconId == summoner.profileIconId
                && summonerLevel == summoner.summonerLevel
                && name == summoner.name && region == summoner.region
    }

    override fun hashCode(): Int {
        var result = (id xor id.ushr(32)).toInt()
        result = 31 * result + name!!.hashCode()
        result = 31 * result + region!!.hashCode()
        result = 31 * result + (profileIconId xor profileIconId.ushr(32)).toInt()
        result = 31 * result + summonerLevel
        return result
    }
}
