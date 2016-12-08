package es.coru.andiag.welegends.models.entities

import java.io.Serializable
import java.util.*

/**
 * Created by Canalejas on 08/12/2016.
 */

class Champion : Serializable {
    var version: String? = null
    var id: String? = null
    var key: String? = null
    var name: String? = null
    var blurb: String? = null
    var image: Image? = null
    var partype: String? = null
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
