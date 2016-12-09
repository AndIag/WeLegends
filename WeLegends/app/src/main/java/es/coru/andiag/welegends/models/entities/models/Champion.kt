package es.coru.andiag.welegends.models.entities.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.orm.SugarRecord
import com.orm.dsl.Unique
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

class Champion : SugarRecord(), Serializable {

    @SerializedName("id") @Expose var name: String? = null
    @Expose var version: String? = null
    @Unique @Expose var key: String? = null
    @Expose var title: String? = null
    @Expose var blurb: String? = null
    @Expose var image: Image? = null
    @Expose var partype: String? = null

}
