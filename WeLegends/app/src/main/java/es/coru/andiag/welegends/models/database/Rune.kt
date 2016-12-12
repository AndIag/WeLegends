package es.coru.andiag.welegends.models.database

import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.rest.utils.KeyInMapTypeAdapter
import java.io.Serializable

/**
 * Created by Canalejas on 13/12/2016.
 */
@Table(database = WeLegendsDatabase::class)
class Rune : BaseModel(), Serializable, KeyInMapTypeAdapter {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @Expose(serialize = false, deserialize = false) @Unique @Column var riotId: String? = null
    @Column var name: String? = null
    @Column var description: String? = null
    @ForeignKey(tableClass = Image::class) var image: Image? = null
    @ForeignKey(tableClass = RuneType::class) var rune: RuneType? = null
    @Column var colloq: String? = null
    @Column var plaintext: String? = null

//    private val stats: Map<String, Int>? = null
//    private val tags: List<String>? = null

    override fun setKey(key: String) {
        this.riotId = key
    }

}
