package es.coru.andiag.welegends.models.wrapped.database

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.wrapped.database.static_data.ProfileIcon
import es.coru.andiag.welegends.models.wrapped.database.static_data.ProfileIcon_Table
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

@Table(database = WeLegendsDatabase::class)
class Summoner() : BaseModel(), Serializable {

    @Expose(serialize = false, deserialize = false) @PrimaryKey(autoincrement = true) var mid: Int = 0
    @SerializedName("id") @Unique @Column var riotId: Long? = null
    @Column var name: String? = null
    @Column var region: String? = null
    @Expose(serialize = false, deserialize = false) @ForeignKey(tableClass = ProfileIcon::class) var profileIcon: ProfileIcon? = null
    @Column var lastUpdate: Long? = null
    @Column var summonerLevel: Int = 0

    var profileIconId: Long?
        set(value) {
            profileIcon = SQLite.select().from<ProfileIcon>(ProfileIcon::class.java)
                    .where(ProfileIcon_Table.riotId.eq(value)).querySingle()
        }
        get() = if (profileIcon != null) profileIcon!!.riotId else null

}