package es.coru.andiag.welegends.models.entities.dto

import com.google.gson.annotations.Expose
import java.io.Serializable

/**
 * Created by Canalejas on 08/12/2016.
 */

class GenericStaticData<K, E> : Serializable {
    @Expose var type: String? = null
    @Expose var version: String? = null
    @Expose var format: String? = null
    @Expose var data: Map<K, E>? = null
}
