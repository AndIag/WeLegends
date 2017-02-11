package com.andiag.welegends.models.database

import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.models.database.static_data.generics.OrmBaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class Summoner : OrmBaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Unique @Column var riotId: Long? = null
    @Column var name: String? = null
    @Column var region: String? = null
    @Column var profileIconId: Long? = null
    @Column var lastUpdate: Long? = null
    @Column var summonerLevel: Int = 0

}
