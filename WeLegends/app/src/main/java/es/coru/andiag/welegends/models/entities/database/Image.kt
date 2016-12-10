package es.coru.andiag.welegends.models.entities.database

import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.Model
import es.coru.andiag.welegends.WeLegendsDatabase
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class Image() : BaseModel(), Serializable, Model {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @Column var full: String? = null
    @Column var sprite: String? = null
    @Column var group: String? = null

}
