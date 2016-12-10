package es.coru.andiag.welegends.models.entities.database

import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.Model
import es.coru.andiag.welegends.WeLegendsDatabase
import java.io.Serializable

/**
 * Created by Canalejas on 09/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class ProfileIcon() : BaseModel(), Serializable, Model {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @Unique @Column var id: Long? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null

}
