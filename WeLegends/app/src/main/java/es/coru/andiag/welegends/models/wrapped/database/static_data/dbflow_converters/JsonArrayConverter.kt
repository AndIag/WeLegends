package es.coru.andiag.welegends.models.wrapped.database.static_data.dbflow_converters

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.raizlabs.android.dbflow.converter.TypeConverter

/**
 * Created by Canalejas on 14/12/2016.
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
class JsonArrayConverter : TypeConverter<String, JsonArray>() {

    override fun getModelValue(data: String?): JsonArray {
        return gson!!.fromJson(data, JsonArray::class.java)
    }

    override fun getDBValue(model: JsonArray?): String? {
        return model?.toString()
    }

    companion object {

        private var gson: Gson? = null

        init {
            gson = Gson()
        }
    }
}