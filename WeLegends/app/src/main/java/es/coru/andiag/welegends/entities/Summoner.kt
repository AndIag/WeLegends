package es.coru.andiag.welegends.entities

import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

class Summoner : Serializable {

    var id: Long = 0
    var localId: Long? = null
    var name: String? = null
    var region: String? = null
    var profileIconId: Long = 0
    var lastUpdate: Long? = null
    var summonerLevel: Int = 0

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
