package com.andiag.welegends.models.database.static_data

import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.models.utils.converters.OrmBaseModel
import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class Image : OrmBaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @Column var full: String? = null
    @Column var sprite: String? = null
    @Column var group: String? = null

}
