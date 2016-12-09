package es.coru.andiag.welegends.models.entities.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.orm.SugarRecord
import com.orm.dsl.Unique
import java.io.Serializable

/**
 * Created by Canalejas on 09/12/2016.
 */

class ProfileIcon : SugarRecord(), Serializable {

    @SerializedName("id") @Unique @Expose var key: Number? = null
    @Expose var image: Image? = null

}
