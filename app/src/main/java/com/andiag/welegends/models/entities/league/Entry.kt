package com.andiag.welegends.models.entities.league

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Canalejas on 30/12/2016.
 */

class Entry {

    @SerializedName("playerOrTeamId")
    @Expose
    var playerOrTeamId: String? = null
    @SerializedName("playerOrTeamName")
    @Expose
    var playerOrTeamName: String? = null
    @SerializedName("division")
    @Expose
    var division: String? = null
    @SerializedName("leaguePoints")
    @Expose
    var leaguePoints: Int? = null
    @SerializedName("wins")
    @Expose
    var wins: Int? = null
    @SerializedName("losses")
    @Expose
    var losses: Int? = null
    @SerializedName("isHotStreak")
    @Expose
    var isHotStreak: Boolean? = null
    @SerializedName("isVeteran")
    @Expose
    var isVeteran: Boolean? = null
    @SerializedName("isFreshBlood")
    @Expose
    var isFreshBlood: Boolean? = null
    @SerializedName("isInactive")
    @Expose
    var isInactive: Boolean? = null

}
