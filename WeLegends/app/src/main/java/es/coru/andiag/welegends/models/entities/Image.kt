package es.coru.andiag.welegends.models.entities

import com.google.gson.annotations.Expose
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

class Image : Serializable {

    @Expose var full: String? = null
    @Expose var sprite: String? = null
    @Expose var group: String? = null

}
