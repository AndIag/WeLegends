package es.coru.andiag.welegends.models.static_data.dbflow_converters

import com.google.gson.Gson
import com.raizlabs.android.dbflow.converter.TypeConverter

/**
 * Created by Canalejas on 13/12/2016.
 */

@com.raizlabs.android.dbflow.annotation.TypeConverter
class ConverterIntList : TypeConverter<String, Array<Int>>() {

    override fun getDBValue(model: Array<Int>?): String? {
        return if (model == null) null else gson!!.toJson(model)
    }

    override fun getModelValue(data: String): Array<Int> {
        return gson!!.fromJson(data, Array<Int>::class.java)
    }

    companion object {

        private var gson: Gson? = null

        init {
            gson = Gson()
        }
    }
}