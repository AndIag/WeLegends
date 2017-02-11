package com.andiag.welegends.models.database.static_data

import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.models.utils.converters.OrmBaseModel
import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

/**
 * Created by Canalejas on 12/12/2016.
 */
@Table(database = WeLegendsDatabase::class)
class Gold : OrmBaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @Column var base: Int? = null
    @Column(getterName = "getPurchasable") var purchasable: Boolean? = null
    @Column var total: Int? = null
    @Column var sell: Int? = null

}
