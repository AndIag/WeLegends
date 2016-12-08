package es.coru.andiag.welegends.common.entities

import java.io.Serializable
import java.util.*

/**
 * Created by Canalejas on 08/12/2016.
 */

class Image : Serializable {

    var full: String? = null
    var sprite: String? = null
    var group: String? = null
    private var additionalProperties: MutableMap<String, Any>? = null

    fun getAdditionalProperties(): MutableMap<String, Any>? {
        return this.additionalProperties
    }

    fun setAdditionalProperties(additionalProperties: MutableMap<String, Any>) {
        this.additionalProperties = additionalProperties
    }

    fun addAdditionalProperty(name: String, value: Any) {
        if (this.additionalProperties == null) {
            this.additionalProperties = HashMap<String, Any>()
        }
        this.additionalProperties!!.put(name, value)
    }

}
