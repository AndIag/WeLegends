package es.coru.andiag.welegends.models.static_data.dbflow_converters

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.raizlabs.android.dbflow.converter.TypeConverter


/**
 * Created by Canalejas on 13/12/2016.
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
class JsonObjectConverter : TypeConverter<String, JsonObject>() {

    override fun getModelValue(data: String?): JsonObject {
        return gson!!.fromJson(data, JsonObject::class.java)
    }

    override fun getDBValue(model: JsonObject?): String? {
        return model?.toString()
    }

    companion object {

        private var gson: Gson? = null

        init {
            gson = Gson()
        }
    }

}